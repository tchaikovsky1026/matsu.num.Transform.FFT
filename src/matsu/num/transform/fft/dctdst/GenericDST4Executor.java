/**
 * 2023.12.5
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.AbstractScalingRealLinear;

/**
 * 汎用的に使えるDST-4の実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain DST4Executor} は,
 * 任意のデータサイズのDST-4に対応する.
 * </p>
 * 
 * <p>
 * {@linkplain DST4Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE}
 * {@code を超える場合}</li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public final class GenericDST4Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DST4Executor INSTANCE = new GenericDST4ExecutorImpl();

    private GenericDST4Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DST-4の実行インスタンスを返す.
     * 
     * @return DST-4実行インスタンス
     */
    public static DST4Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DST-4の実装.
     */
    private static final class GenericDST4ExecutorImpl
            extends AbstractScalingRealLinear implements DST4Executor {

        private static final String CLASS_STRING = "Generic-DST-4";

        private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

        /**
         * Generic DST-4を生成する.
         */
        GenericDST4ExecutorImpl() {
            super();
        }

        /**
         * @throws IllegalArgumentException サイズが{@link #MAX_DATA_SIZE}を超える場合
         */
        @Override
        protected double[] applyInner(double[] data) {
            int size = data.length;
            if (size > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            /*
             * DST-4は2N個の実数データ点a,
             * a[j] = x[j] * exp(-i * 2pi * j/(4N)) * exp(-i * pi/(4N))
             * (j=0,...,N-1),
             * a[2N - 1 - j] = x[j] * exp(-i * 2pi *(2N - j)/(4N)) * exp(i *
             * pi/(4N)) (j=0,...,N-1),
             * に対してFFTを実行し,
             * X[k] = (i/2) * exp[-i*2pi*k/(4N)] * A[k]
             * とすればよい.
             */

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
                a[fftSize - 1 - j] = invRot_quarter
                        .times(dftBasis_4N.valueAt(fftSize - j))
                        .timesReal(data[j]);
            }

            /* FFT実行 */
            ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

            /* 結果をDST-4に変換 */
            double[] result = new double[size];
            for (int k = 0; k < size; k++) {
                result[k] = -0.5 * A[k].times(dftBasis_4N.valueAt(k)).imaginary();
            }

            return result;
        }

        /**
         * このインスタンスの文字列表現を提供する.
         * 
         * <p>
         * おそらく次の形式が適切であろうが, 確実ではなく,
         * バージョン間の整合性も担保されていない. <br>
         * {@code %変換名}
         * </p>
         */
        @Override
        public String toString() {
            return CLASS_STRING;
        }
    }
}
