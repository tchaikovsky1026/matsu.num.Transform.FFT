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
 * 汎用的に使えるDCT-2の実行手段を提供する. 
 * 
 * <p>
 * このクラスが提供する {@linkplain DCT2Executor} は, 
 * 任意のデータサイズのDCT-2に対応する. 
 * </p>
 * 
 * <p>
 * {@linkplain DCT2Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public final class GenericDCT2Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DCT2Executor INSTANCE = new GenericDCT2ExecutorImpl();

    private GenericDCT2Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DCT-2の実行インスタンスを返す. 
     * 
     * @return DCT-2実行インスタンス
     * @deprecated バージョン 12.4 までは新しいインスタンスを返していたが,
     * 現在のバージョンはnewしていないかもしれない.
     * {@code new***}というメソッド名は実体に合っていない可能性があるので非推奨.
     * バージョン 13 以降で削除
     * @see #instance()
     */
    @Deprecated(forRemoval = true)
    public static DCT2Executor newInstance() {
        return INSTANCE;
    }

    /**
     * DCT-2の実行インスタンスを返す. 
     * 
     * @return DCT-2実行インスタンス
     */
    public static DCT2Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DCT-2の実装.
     */
    private static final class GenericDCT2ExecutorImpl implements DCT2Executor {

        private static final String CLASS_STRING = "Generic-DCT-2";

        private final RealNumbersLinearBijectiveTransform dct2 =
                ScalingRealNumberTransform.decorate(new NonScalingDCT2());

        /**
         * Generic DCT-2を生成する.
         */
        GenericDCT2ExecutorImpl() {
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

            return this.dct2.apply(data);
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
         * スケーリング, 事前条件チェック, 防御的コピーのないのDCT2.
         */
        private static final class NonScalingDCT2 implements DCT2Executor {

            private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

            NonScalingDCT2() {
            }

            @Override
            public double[] apply(double[] data) {
                int size = data.length;

                /* DCT-2は2N個の実数データ点a, 
                 * a[0] = x[0],..., a[N-1] = x[N-1],
                 * a[N] = x[N-1], ,..., x[2N-1] = x[0]
                 * に対してFFTを実行し, 
                 * X[k] = 0.5 * exp[-i*2pi*k/(4N)] * A[k]
                 * とすればよい.
                 * */

                /* FFT用のデータ作成 */
                //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
                int fftSize = 2 * size;
                ComplexNumber[] a = new ComplexNumber[fftSize];
                for (int i = 0; i < size; i++) {
                    a[i] = ComplexNumber.of(data[i], 0);
                }
                for (int i = 0; i < size; i++) {
                    a[fftSize - 1 - i] = a[i];
                }

                /* FFT実行 */
                //DCT2サイズの4倍 = fftSizeの2倍を表す(最大2^29). 
                //前処理/後処理のための係数を得るために必要.
                int N4 = 2 * fftSize;
                FourierBasisComputer dftBasisComputer = FourierBasisComputer.covering(N4, FourierType.DFT);
                ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

                /* 結果をDCT-2に変換 */
                //exp[-i*2pi*k/(4N)]の計算をするため, 4NサイズのDFT基底を得る
                FourierBasis dftBasis_4N = dftBasisComputer.getBasis(N4);
                double[] result = new double[size];
                for (int k = 0; k < size; k++) {
                    result[k] = 0.5 * A[k].times(dftBasis_4N.valueAt(k)).real();
                }

                return result;
            }
        }

    }

}
