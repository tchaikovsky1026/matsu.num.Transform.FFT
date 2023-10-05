/**
 * 2023.9.28
 */
package matsu.num.transform.fft.component;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntFunction;

import matsu.num.commons.Trigonometry;
import matsu.num.transform.fft.number.Power2Util;

/**
 * 離散Fourier変換/逆変換の基底関数の生成を扱う. 
 * 
 * <p>
 * サポート標本サイズ<i>N</i><sub>support</sub>を指定してインスタンスを構築すると, <br>
 * <i>N</i><sub>support</sub>の任意の約数を標本数とする基底関数の構築が可能である. 
 * </p>
 * 
 * <p>
 * DFTの基底関数を<i>W</i><sub><i>N</i></sub> = exp[-i(2&pi;/<i>N</i>)]とする. <br>
 * <i>N</i>と<i>j</i>が公約数<i>d</i>を持つとき, <br>
 * <i>W</i><sub><i>N</i></sub><sup><i>j</i></sup> = <i>W</i><sub><i>N</i>/<i>d</i></sub><sup><i>j</i>/<i>d</i></sup> <br>
 * を満たす
 * (IDFTの場合も同様である). <br>
 * このクラスではこのことを利用して, <i>N</i>の約数である標本数における基底関数を同時に提供する. 
 * </p>
 * 
 * <p>
 * 公開された全ての振る舞いはスレッドセーフである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.5
 */
public final class FourierBasisComputer {

    /**
     * 扱うことができるシーケンスサイズの最大値: 2<sup>30</sup>.
     */
    public static final int MAX_SUPPORTED_SEQUENCE_SIZE;

    /**
     * 基底関数のタイプから基底関数コンピュータを得るためのマッピング
     */
    private static final Map<FourierType, IntFunction<FourierBasisComputer>> MAP_TO_BAISI_COMPUTER;

    /**
     * 2の累乗についてのキャッシュされる標本サイズの指数: 10,
     * キャッシュされる標本サイズは2^10.
     */
    private static final int CACHED_SEQUENCE_SIZE_EXPONENT = 10;

    static {
        // MAXサポート標本サイズは必ず2のべき乗でなければならない
        MAX_SUPPORTED_SEQUENCE_SIZE = 0x4000_0000;

        MAP_TO_BAISI_COMPUTER = new EnumMap<>(FourierType.class);
        MAP_TO_BAISI_COMPUTER
                .put(
                        FourierType.DFT,
                        supportedSequenceSize -> DFTBasisComputerSupplier.covering(supportedSequenceSize));
        MAP_TO_BAISI_COMPUTER.put(
                FourierType.IDFT, supportedSequenceSize -> IDFTBasisComputerSupplier.covering(supportedSequenceSize));

        //列挙型マップのバリデーション
        assert Arrays.stream(FourierType.values())
                .map(MAP_TO_BAISI_COMPUTER::get)
                .allMatch(Objects::nonNull) : "Bug:マップが網羅できていない";
    }

    private final FourierType type;

    private final int supportedSequenceSize;
    private final ComplexNumber[] values;

    private final Map<Integer, FourierBasis> basisCache;

    //排他処理用ロックオブジェクト
    private final Object lock = new Object();

    /**
     * 生成器を構築する. 
     * 
     * @param supportedSequenceSize サポート標本サイズ
     * @param type タイプ
     */
    private FourierBasisComputer(int supportedSequenceSize, FourierType type) {

        this.type = type;
        this.supportedSequenceSize = supportedSequenceSize;
        this.values = new ComplexNumber[supportedSequenceSize];
        this.basisCache = new ConcurrentHashMap<>();

        this.computeFullBasis();
    }

    private void computeFullBasis() {
        for (int j = 0; j < this.supportedSequenceSize; j++) {
            double argByPi = this.type.argByPi(this.supportedSequenceSize, j);
            this.values[j] = ComplexNumber.of(Trigonometry.cospi(argByPi), Trigonometry.sinpi(argByPi));
        }
    }

    /**
     * このコンピュータが扱う基底関数のタイプを返す.
     * 
     * @return 基底関数のタイプ
     */
    public FourierType tyoe() {
        return this.type;
    }

    /**
     * 指定した標本サイズがサポートされているかを判定する.
     * 
     * @param sequenceSize 標本サイズ
     * @return 標本サイズがサポートされているならtrue
     * @throws IllegalArgumentException 標本サイズが1以上でない場合
     */
    public boolean support(int sequenceSize) {
        if (sequenceSize <= 0) {
            throw new IllegalArgumentException("標本サイズが1以上でない");
        }

        return this.supportedSequenceSize % sequenceSize == 0;
    }

    /**
     * 与えた標本サイズに対応する基底関数を返す. <br>
     * 事前条件チェック(引数のバリデーション)として{@linkplain #support(int)}メソッドが利用できる.
     *  
     * @param sequenceSize 標本サイズ
     * @return 複素数データ
     * @throws IllegalArgumentException 標本サイズが正でない場合, サポートされていない場合
     * @see #support(int)
     */
    public FourierBasis getBasis(int sequenceSize) {
        if (!this.support(sequenceSize)) {
            throw new IllegalArgumentException("標本サイズがサポートされていない");
        }

        FourierBasis out = this.basisCache.get(sequenceSize);
        if (Objects.nonNull(out)) {
            return out;
        }

        //二重チェックイディオム
        //基底のcomputeが重い場合があるので排他処理
        synchronized (this.lock) {
            out = this.basisCache.get(sequenceSize);
            if (Objects.nonNull(out)) {
                return out;
            }

            out = this.computeBasis(sequenceSize);
            this.basisCache.put(sequenceSize, out);
            return out;
        }
    }

    private FourierBasis computeBasis(int sequenceSize) {

        int thinning = this.supportedSequenceSize / sequenceSize;

        ComplexNumber[] sub = new ComplexNumber[sequenceSize];
        for (int j = 0, jd = 0; j < sequenceSize; j++) {
            sub[j] = this.values[jd];
            jd += thinning;
        }

        return new FourierBasis(sequenceSize, sub, type);
    }

    /**
     * 与えたサポート標本サイズをカバーする生成器を返す. 
     * 
     * @param supportedSequenceSize サポート標本サイズ
     * @param type タイプ
     * @return 生成器
     * @throws IllegalArgumentException サポート標本サイズが正でない場合, 大きすぎる場合
     * @{@link Thread} NullPointerException 引数にnullが含まれる場合
     */
    public static FourierBasisComputer covering(int supportedSequenceSize, FourierType type) {
        if (supportedSequenceSize <= 0) {
            throw new IllegalArgumentException("サポート標本サイズが正でない");
        }
        if (supportedSequenceSize > FourierBasisComputer.MAX_SUPPORTED_SEQUENCE_SIZE) {
            throw new IllegalArgumentException("サポート標本サイズが大きすぎる");
        }

        return MAP_TO_BAISI_COMPUTER.get(Objects.requireNonNull(type)).apply(supportedSequenceSize);
    }

    /**
     * DFT基底関数に関する生成を担う.
     */
    private static final class DFTBasisComputerSupplier {

        /**
         * CACHED_SEQUENCE_SIZE以下の, 2の累乗のコンピュータはキャッシュしたものを返す.
         */
        private static final FourierBasisComputer POWER2_DFT_COMPUTER =
                new FourierBasisComputer(1 << CACHED_SEQUENCE_SIZE_EXPONENT, FourierType.DFT);

        /**
         * 与えたサポート標本サイズをカバーする生成器を返す.
         * 引数は正当である.
         * 
         * @param supportedSequenceSize サポート標本サイズ
         * @return 生成器
         */
        static FourierBasisComputer covering(int supportedSequenceSize) {
            if (Power2Util.isPowerOf2(supportedSequenceSize)
                    && Power2Util.floorLog2(supportedSequenceSize) <= CACHED_SEQUENCE_SIZE_EXPONENT) {
                return POWER2_DFT_COMPUTER;
            }

            return new FourierBasisComputer(supportedSequenceSize, FourierType.DFT);
        }

    }

    /**
     * IDFT基底関数に関する生成を担う.
     */
    private static final class IDFTBasisComputerSupplier {

        /**
         * CACHED_SEQUENCE_SIZE以下の, 2の累乗のコンピュータはキャッシュしたものを返す.
         */
        private static final FourierBasisComputer POWER2_IDFT_COMPUTER =
                new FourierBasisComputer(1 << CACHED_SEQUENCE_SIZE_EXPONENT, FourierType.IDFT);

        /**
         * 与えたサポート標本サイズをカバーする生成器を返す.
         * 引数は正当である.
         * 
         * @param supportedSequenceSize サポート標本サイズ
         * @return 生成器
         */
        static FourierBasisComputer covering(int supportedSequenceSize) {

            if (Power2Util.isPowerOf2(supportedSequenceSize)
                    && Power2Util.floorLog2(supportedSequenceSize) <= CACHED_SEQUENCE_SIZE_EXPONENT) {
                return POWER2_IDFT_COMPUTER;
            }

            return new FourierBasisComputer(supportedSequenceSize, FourierType.IDFT);
        }

    }
}
