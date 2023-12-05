/**
 * 2023.12.5
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
 * 汎用的に使えるDCT-3の実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain DCT3Executor} は,
 * 任意のデータサイズのDCT-3に対応する.
 * </p>
 * 
 * <p>
 * {@linkplain DCT3Executor#apply(double[])} メソッドで追加でスローされる条件は次のとおりである.
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
public final class GenericDCT3Executor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE / 2;

    private static final DCT3Executor INSTANCE = new GenericDCT3ExecutorImpl();

    private GenericDCT3Executor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DCT-3の実行インスタンスを返す.
     * 
     * @return DCT-3実行インスタンス
     */
    public static DCT3Executor instance() {
        return INSTANCE;
    }

    /**
     * Generic DCT-3の実装.
     */
    private static final class GenericDCT3ExecutorImpl
            extends AbstractScalingRealLinear implements DCT3Executor {

        private static final String CLASS_STRING = "Generic-DCT-3";

        private final InnerDFTExecutor fftExecutor = GenericInnerFFTExecutor.instance();

        /**
         * Generic DCT-3を生成する.
         */
        GenericDCT3ExecutorImpl() {
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
             * DCT-3は2N個の実数データ点a,
             * a[0] = x[0],
             * a[j] = x[j] * exp(-i * 2pi * j/(4N)) (j=1,...,N-1),
             * a[N] = 0;
             * a[2N - j] = x[j] * exp(i * 2pi * j/(4N)) (j=1,...,N-1),
             * に対してFFTを実行し,
             * X[k] = 0.5 * A[k]
             * とすればよい.
             */

            /* FFT用のデータ作成 */
            //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
            int fftSize = 2 * size;
            //DCT3サイズの4倍 = fftSizeの2倍を表す(最大2^29). 
            //前処理/後処理のための係数を得るために必要.
            int N4 = fftSize * 2;
            FourierBasisComputer dftBasisComputer = FourierBasisComputer.covering(N4, FourierType.DFT);
            FourierBasis dftBasis_4N = dftBasisComputer.getBasis(N4);

            ComplexNumber[] a = new ComplexNumber[fftSize];
            a[0] = ComplexNumber.of(data[0], 0);
            for (int j = 1; j < size; j++) {
                a[j] = dftBasis_4N.valueAt(j).timesReal(data[j]);
            }
            a[size] = ComplexNumber.ZERO;
            for (int j = 1; j < size; j++) {
                a[fftSize - j] = dftBasis_4N.valueAt(N4 - j).timesReal(data[j]);
            }

            /* FFT実行 */
            ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

            /* 結果をDCT-3に変換 */
            double[] result = new double[size];
            for (int k = 0; k < size; k++) {
                result[k] = 0.5 * A[k].real();
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
