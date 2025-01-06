/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import matsu.num.transform.fft.lib.Trigonometry;

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
 * <i>W</i><sub><i>N</i></sub><sup><i>j</i></sup> =
 * <i>W</i><sub><i>N</i>/<i>d</i></sub><sup><i>j</i>/<i>d</i></sup> <br>
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
 * @version 20.0
 */
public final class FourierBasisComputer {

    /**
     * 扱うことができるシーケンスサイズの最大値: 2<sup>30</sup>.
     */
    public static final int MAX_SUPPORTED_SEQUENCE_SIZE;

    static {
        // MAXサポート標本サイズは必ず2のべき乗でなければならない
        MAX_SUPPORTED_SEQUENCE_SIZE = 0x4000_0000;

    }

    private final FourierType type;
    private final Trigonometry trigonometry;

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
    private FourierBasisComputer(int supportedSequenceSize, FourierType type, Trigonometry trigonometry) {
        super();
        this.type = type;
        this.trigonometry = trigonometry;
        this.supportedSequenceSize = supportedSequenceSize;
        this.values = new ComplexNumber[supportedSequenceSize];
        this.basisCache = new ConcurrentHashMap<>();

        this.computeFullBasis();
    }

    private void computeFullBasis() {
        for (int j = 0; j < this.supportedSequenceSize; j++) {
            double argByPi = this.type.argByPi(this.supportedSequenceSize, j);
            this.values[j] = ComplexNumber.of(this.trigonometry.cospi(argByPi), this.trigonometry.sinpi(argByPi));
        }
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
     * 事前条件チェック(引数のバリデーション)として{@link #support(int)}メソッドが利用できる.
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
     * {@link FourierBasisComputer} のサプライヤ.
     */
    public static final class Supplier {

        private final Trigonometry trigonometry;

        /**
         * 
         * @param trigonometry 三角関数ライブラリ
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public Supplier(Trigonometry trigonometry) {
            super();
            this.trigonometry = Objects.requireNonNull(trigonometry);
        }

        /**
         * 与えたサポート標本サイズをカバーする生成器を返す.
         * 
         * @param supportedSequenceSize サポート標本サイズ
         * @param type タイプ
         * @return 生成器
         * @throws IllegalArgumentException サポート標本サイズが正でない場合, 大きすぎる場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public FourierBasisComputer covering(int supportedSequenceSize, FourierType type) {
            if (supportedSequenceSize <= 0) {
                throw new IllegalArgumentException("サポート標本サイズが正でない");
            }
            if (supportedSequenceSize > FourierBasisComputer.MAX_SUPPORTED_SEQUENCE_SIZE) {
                throw new IllegalArgumentException("サポート標本サイズが大きすぎる");
            }

            return new FourierBasisComputer(
                    supportedSequenceSize, Objects.requireNonNull(type), this.trigonometry);
        }

    }
}
