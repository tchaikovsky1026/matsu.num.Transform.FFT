/**
 * 2023.9.30
 */
package matsu.num.transform.fft.convolution;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.fftmodule.CyclicConvolutionModule;

/**
 * 汎用的に使える巡回畳み込みの実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain RealNumbersCyclicConvolution} は,
 * 任意のデータサイズの巡回畳み込みに対応する.
 * </p>
 * 
 * <p>
 * {@linkplain RealNumbersCyclicConvolution#apply(double[], double[])} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.8
 */
public final class GenericCyclicConvolutionExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = CyclicConvolutionModule.MAX_SEQUENCE_SIZE;

    private static final RealNumbersCyclicConvolution INSTANCE = new CyclicConvImpl();

    private GenericCyclicConvolutionExecutor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * このクラスが提供する機能を実行するインスタンスを返す.
     * 
     * @return 機能インスタンス
     */
    public static RealNumbersCyclicConvolution instance() {
        return INSTANCE;
    }

    private static final class CyclicConvImpl extends ScalingRealNumbersCyclicConvolution
            implements RealNumbersCyclicConvolution {

        private static final String CLASS_STRING = "Generic-CyclicConvolution";

        private final CyclicConvolutionModule module = CyclicConvolutionModule.instance();

        @Override
        protected double[] applyInner(double[] f, double[] g) {
            int size = f.length;

            if (size > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            double[] imaginaryZero = new double[size];
            ComplexNumber[] complexF = ComplexNumber.from(f, imaginaryZero);
            ComplexNumber[] complexG = ComplexNumber.from(g, imaginaryZero);

            ComplexNumber[] complexResult = this.module.compute(complexF, complexG);
            return ComplexNumber.separateToArrays(complexResult)[0];
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

    }

}
