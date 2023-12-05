/**
 * 2023.12.5
 */
package matsu.num.transform.fft.dctdst;

import java.util.Arrays;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.AbstractScalingRealLinear;

/**
 * 汎用的に使えるDST-1の実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain DST1Executor} は,
 * 任意のデータサイズのDST-1に対応する.
 * </p>
 * 
 * <p>
 * {@linkplain DST1Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
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
public final class GenericDST1Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup> - 1
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2 - 1;

    private static final DST1Executor INSTANCE = new GenericDST1ExecutorImpl();

    private GenericDST1Executor() {
        throw new AssertionError();
    }

    /**
     * DST-1の実行インスタンスを返す.
     * 
     * @return DST-1実行インスタンス
     */
    public static DST1Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DST-1の実装
     */
    private static final class GenericDST1ExecutorImpl
            extends AbstractScalingRealLinear implements DST1Executor {

        private static final String CLASS_STRING = "Generic-DST-1";

        private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

        /**
         * DST-1の実装を生成する.
         */
        GenericDST1ExecutorImpl() {
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
             * DST-1は2N+2個の実数データ点a,
             * a[0] = 0, a[1] = x[0],..., a[N] = x[N-1],
             * a[N+1] = 0, a[N+2] = -x[N-1],..., x[2N+1] = -x[0]
             * に対してFFTを実行し,
             * X[k] = (-1/2)Im(A[k+1])
             * とすればよい.
             */

            /* FFT用のデータ作成 */
            //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
            int fftSize = 2 * size + 2;
            ComplexNumber[] a = new ComplexNumber[fftSize];
            Arrays.fill(a, ComplexNumber.ZERO);
            for (int i = 0; i < size; i++) {
                a[i + 1] = ComplexNumber.of(data[i], 0);
            }
            for (int i = 0; i < size; i++) {
                a[fftSize - i - 1] = ComplexNumber.of(-a[i + 1].real(), 0);
            }

            /* FFT実行 */
            ComplexNumber[] A = this.fftExecutor
                    .compute(a, FourierBasisComputer.covering(fftSize, FourierType.DFT));

            /* 結果をDST-1に変換 */
            double[] result = new double[size];
            for (int i = 0; i < size; i++) {
                result[i] = -0.5 * A[i + 1].imaginary();
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
