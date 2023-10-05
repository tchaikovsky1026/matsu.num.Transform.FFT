/**
 * 2023.9.29
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.ScalingRealNumberTransform;

/**
 * 汎用的に使えるDCT-1の実行手段を提供する. 
 * 
 * <p>
 * このクラスが提供する {@linkplain DCT1Executor} は, 
 * 任意のデータサイズのDCT-1に対応する. 
 * </p>
 * 
 * <p>
 * {@linkplain DCT1Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public final class GenericDCT1Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup> + 1
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2 + 1;

    private static final DCT1Executor INSTANCE = new GenericDCT1ExecutorImpl();

    private GenericDCT1Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DCT-1の実行インスタンスを返す. 
     * 
     * @return DCT-1実行インスタンス
     * @deprecated バージョン 12.4 までは新しいインスタンスを返していたが,
     * 現在のバージョンはnewしていないかもしれない.
     * {@code new***}というメソッド名は実体に合っていない可能性があるので非推奨.
     * バージョン 13 以降で削除
     * @see #instance()
     */
    @Deprecated(forRemoval = true)
    public static DCT1Executor newInstance() {
        return INSTANCE;
    }

    /**
     * DCT-1の実行インスタンスを返す. 
     * 
     * @return DCT-1実行インスタンス
     */
    public static DCT1Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DCT-1の実装
     */
    private static final class GenericDCT1ExecutorImpl implements DCT1Executor {

        private static final String CLASS_STRING = "Generic-DCT-1";

        private final RealNumbersLinearBijectiveTransform dct1 =
                ScalingRealNumberTransform.decorate(new NonScalingDCT1());

        /**
         * DST-1の実装を生成する.
         */
        GenericDCT1ExecutorImpl() {
        }

        /**
         * @throws IllegalArgumentException サイズが1以下の場合, 
         * サイズが{@link #MAX_DATA_SIZE}を超える場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        @Override
        public double[] apply(double[] data) {
            if (data.length <= 1) {
                throw new IllegalArgumentException("データサイズが1以下である");
            }
            if (data.length > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            return this.dct1.apply(data);
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
         * スケーリング, 事前条件チェック, 防御的コピーのないのDCT1.
         */
        private static final class NonScalingDCT1 implements DCT1Executor {

            private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

            NonScalingDCT1() {
            }

            @Override
            public double[] apply(double[] data) {
                int size = data.length;

                /* DCT-1は2N-2個の実数データ点a, 
                 * a[0] = x[0],..., a[N-1] = x[N-1],
                 * a[N] = x[N-2], ,..., x[2N-3] = x[1]
                 * に対してFFTを実行し, 
                 * X[k] = 0.5 * A[k] (A[k]は実数)
                 * とすればよい.
                 * */

                /* FFT用のデータ作成 */
                //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
                int fftSize = 2 * size - 2;
                ComplexNumber[] a = new ComplexNumber[fftSize];
                for (int i = 0; i < size; i++) {
                    a[i] = ComplexNumber.of(data[i], 0);
                }
                for (int i = 1; i < size - 1; i++) {
                    a[fftSize - i] = a[i];
                }

                /* FFT実行 */
                ComplexNumber[] A = this.fftExecutor
                        .compute(a, FourierBasisComputer.covering(fftSize, FourierType.DFT));

                /* 結果をDCT-1に変換 */
                double[] result = new double[size];
                for (int i = 0; i < size; i++) {
                    result[i] = 0.5 * A[i].real();
                }

                return result;
            }
        }
    }
}
