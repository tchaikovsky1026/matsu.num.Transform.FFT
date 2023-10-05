/**
 * 2023.9.29
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.ScalingRealNumberTransform;

/**
 * 汎用的に使えるDST-2の実行手段を提供する. 
 * 
 * <p>
 * このクラスが提供する {@linkplain DST2Executor} は, 
 * 任意のデータサイズのDST-2に対応する. 
 * </p>
 * 
 * <p>
 * {@linkplain DST2Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public final class GenericDST2Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DST2Executor INSTANCE = new GenericDST2ExecutorImpl();

    private GenericDST2Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DST-2の実行インスタンスを返す. 
     * 
     * @return DST-2実行インスタンス
     * @deprecated バージョン 12.4 までは新しいインスタンスを返していたが,
     * 現在のバージョンはnewしていないかもしれない.
     * {@code new***}というメソッド名は実体に合っていない可能性があるので非推奨.
     * バージョン 13 以降で削除
     * @see #instance()
     */
    @Deprecated(forRemoval = true)
    public static DST2Executor newInstance() {
        return INSTANCE;
    }

    /**
     * DST-2の実行インスタンスを返す. 
     * 
     * @return DST-2実行インスタンス
     */
    public static DST2Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DST-2の実装.
     */
    private static final class GenericDST2ExecutorImpl implements DST2Executor {

        private static final String CLASS_STRING = "Generic-DST-2";

        private final RealNumbersLinearBijectiveTransform dst2 =
                ScalingRealNumberTransform.decorate(new NonScalingDST2());

        /**
         * Generic DST-2を生成する.
         */
        GenericDST2ExecutorImpl() {
        }

        /**
         * @throws IllegalArgumentException サイズが0の場合, 
         * サイズが{@link #MAX_DATA_SIZE}を超える場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        @Override
        public double[] apply(double[] data) {

            if (data.length == 1) {
                throw new IllegalArgumentException("データサイズが0である");
            }
            if (data.length > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            return this.dst2.apply(data);
        }

        /**
         * このインスタンスの文字列表現を提供する. 
         * 
         * <p>
         * おそらく次の形式が適切であろうが, 確実ではなく, 
         * バージョン間の整合性も担保されていない. <br>
         * {@code [変換名]}
         * </p>
         */
        @Override
        public String toString() {
            return String.format("[%s]", CLASS_STRING);
        }

        /**
         * スケーリング, 事前条件チェック, 防御的コピーのないのDST2.
         */
        private static final class NonScalingDST2 implements DST2Executor {

            private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

            NonScalingDST2() {
            }

            @Override
            public double[] apply(double[] data) {
                int size = data.length;

                int fftSize = size * 2;
                FourierBasisComputer dftBasisComputer =
                        FourierBasisComputer.covering(2 * fftSize, FourierType.DFT);

                /* DST-2は2N個の実数データ点a, 
                 * a[j] = x[j] (j=0,...,N-1)
                 * a[2N - 1 - j] = -x[j] (j=0,...,N-1)
                 * に対してFFTを実行し, 
                 * X[k] = (i/2) * exp[-i*2pi*(k+1)/(4N)] * A[k + 1]
                 * とすればよい.
                 * */

                /* FFT用のデータ作成 */
                //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
                ComplexNumber[] a = new ComplexNumber[fftSize];
                for (int i = 0; i < size; i++) {
                    a[i] = ComplexNumber.of(data[i], 0);
                }
                for (int i = 0; i < size; i++) {
                    a[fftSize - 1 - i] = ComplexNumber.of(-data[i], 0);
                }

                /* FFT実行 */
                //前処理/後処理のための係数を得るために必要.
                ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

                /* 結果をDST-2に変換 */
                //exp[-i*2pi*(k+1)/(4N)]の計算をするため, 4NサイズのDFT基底を得る
                FourierBasis dftBasis_4N = dftBasisComputer.getBasis(2 * fftSize);
                double[] result = new double[size];
                for (int k = 0; k < size; k++) {
                    result[k] = -0.5 * A[k + 1].times(dftBasis_4N.valueAt(k + 1)).imaginary();
                }

                return result;
            }
        }
    }
}
