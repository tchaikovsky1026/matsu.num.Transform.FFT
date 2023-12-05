/**
 * 2023.12.4
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.AbstractScalingRealLinear;

/**
 * 汎用的に使えるDST-3の実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain DST3Executor} は,
 * 任意のデータサイズのDST-3に対応する.
 * </p>
 * 
 * <p>
 * {@linkplain DST3Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
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
public final class GenericDST3Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DST3Executor INSTANCE = new GenericDST3ExecutorImpl();

    private GenericDST3Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DST-3の実行インスタンスを返す.
     * 
     * @return DST-3実行インスタンス
     */
    public static DST3Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DST-3の実装.
     */
    private static final class GenericDST3ExecutorImpl
            extends AbstractScalingRealLinear implements DST3Executor {

        private static final String CLASS_STRING = "Generic-DST-3";

        private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

        /**
         * Generic DST-3を生成する.
         */
        GenericDST3ExecutorImpl() {
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
             * DST-3は2N個の実数データ点a,
             * a[0] = 0;
             * a[j + 1] = x[j]exp[-i * 2pi * (j + 1) / (4N)] (j=0,...,N-1)
             * a[2N - 1 - j] = x[j]exp[-i * 2pi * (2N - 1 - j) / (4N)]
             * (j=0,...,N-2)
             * に対してFFTを実行し,
             * X[k] = (i/2) * A[k]
             * とすればよい.
             */

            int fftSize = size * 2;
            FourierBasisComputer dftBasisComputer =
                    FourierBasisComputer.covering(2 * fftSize, FourierType.DFT);

            /* FFT用のデータ作成 */
            //exp[-i*2pi*j/(4N)]型の計算をするため, 4NサイズのDFT基底を得る
            FourierBasis dftBasis_4N = dftBasisComputer.getBasis(2 * fftSize);
            //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
            ComplexNumber[] a = new ComplexNumber[fftSize];
            a[0] = ComplexNumber.ZERO;
            for (int j = 0; j < size; j++) {
                a[j + 1] = dftBasis_4N.valueAt(j + 1).timesReal(data[j]);
            }
            for (int j = 0; j < size - 1; j++) {
                a[fftSize - 1 - j] = dftBasis_4N.valueAt(fftSize - 1 - j).timesReal(data[j]);
            }

            /* FFT実行 */
            //前処理/後処理のための係数を得るために必要.
            ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

            /* 結果をDST-3に変換 */
            double[] result = new double[size];
            for (int k = 0; k < size; k++) {
                result[k] = -0.5 * A[k].imaginary();
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
