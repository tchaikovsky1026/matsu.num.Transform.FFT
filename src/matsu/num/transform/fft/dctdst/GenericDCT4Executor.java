/**
 * 2023.9.29
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;
import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.ScalingRealNumberTransform;

/**
 * 汎用的に使えるDCT-4の実行手段を提供する. 
 * 
 * <p>
 * このクラスが提供する {@linkplain DCT4Executor} は, 
 * 任意のデータサイズのDCT-4に対応する. 
 * </p>
 * 
 * <p>
 * {@linkplain DCT4Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public final class GenericDCT4Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DCT4Executor INSTANCE = new GenericDCT4ExecutorImpl();

    private GenericDCT4Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DCT-4の実行インスタンスを返す. 
     * 
     * @return DCT-4実行インスタンス
     * @deprecated バージョン 12.4 までは新しいインスタンスを返していたが,
     * 現在のバージョンはnewしていないかもしれない.
     * {@code new***}というメソッド名は実体に合っていない可能性があるので非推奨.
     * バージョン 13 以降で削除
     * @see #instance()
     */
    @Deprecated(forRemoval = true)
    public static DCT4Executor newInstance() {
        return INSTANCE;
    }

    /**
     * DCT-4の実行インスタンスを返す. 
     * 
     * @return DCT-4実行インスタンス
     */
    public static DCT4Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DCT-4の実装.
     */
    private static final class GenericDCT4ExecutorImpl implements DCT4Executor {

        private static final String CLASS_STRING = "Generic-DCT-4";

        private final RealNumbersLinearBijectiveTransform dct4 =
                ScalingRealNumberTransform.decorate(new NonScalingDCT4());

        /**
         * Generic DCT-4を生成する.
         */
        GenericDCT4ExecutorImpl() {
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

            return this.dct4.apply(data);
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
         * スケーリング, 事前条件チェック, 防御的コピーのないのDCT4.
         */
        private static final class NonScalingDCT4 implements DCT4Executor {

            private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

            NonScalingDCT4() {
            }

            @Override
            public double[] apply(double[] data) {
                int size = data.length;

                int fftSize = size * 2;
                FourierBasisComputer dftBasisComputer = FourierBasisComputer.covering(2 * fftSize, FourierType.DFT);

                ComplexNumber rot_quarter;
                ComplexNumber invRot_quarter;
                {
                    //cos, sin (pi/(4N))
                    double phi = 0.5 / fftSize;
                    double cos = Trigonometry.cospi(phi);
                    double sin = Trigonometry.sinpi(phi);
                    rot_quarter = ComplexNumber.of(cos, -sin);
                    invRot_quarter = ComplexNumber.of(cos, sin);
                }

                /* DCT-4は2N個の実数データ点a, 
                 * a[j] = x[j] * exp(-i * 2pi * j/(4N)) * exp(-i * pi/(4N)) (j=0,...,N-1),
                 * a[2N - 1 - j] = x[j] * exp(-i * 2pi * (4N - j)/(4N)) * exp(i * pi/(4N)) (j=0,...,N-1),
                 * に対してFFTを実行し, 
                 * X[k] = 0.5 * exp[-i*2pi*k/(4N)] * A[k]
                 * とすればよい.
                 * */

                /* FFT用のデータ作成 */
                //fftSizeの2倍を表す(最大2^29). 
                //前処理/後処理のための係数を得るために必要.
                FourierBasis dftBasis_4N = dftBasisComputer.getBasis(fftSize * 2);

                ComplexNumber[] a = new ComplexNumber[fftSize];
                for (int j = 0; j < size; j++) {
                    a[j] = rot_quarter
                            .times(dftBasis_4N.valueAt(j))
                            .timesReal(data[j]);
                }
                for (int j = 0; j < size; j++) {
                    //exp(-i * 2pi * (4N - j)/(4N))が位相2πになる(IndexOutOfBoundsEx)のを回避するため, 
                    //x[j]を(-1)倍し, exp(-i * 2pi * (2N - j)/(4N))の回転にする
                    a[fftSize - 1 - j] = invRot_quarter
                            .times(dftBasis_4N.valueAt(fftSize - j))
                            .timesReal(-data[j]);
                }

                /* FFT実行 */
                ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

                /* 結果をDCT-4に変換 */
                double[] result = new double[size];
                for (int k = 0; k < size; k++) {
                    result[k] = 0.5 * A[k].times(dftBasis_4N.valueAt(k)).real();
                }

                return result;
            }
        }

    }

}
