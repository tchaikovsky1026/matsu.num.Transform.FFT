/**
 * 2023.12.4
 */
package matsu.num.transform.fft.convolution;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.fftmodule.Power2CyclicConvolutionModule;
import matsu.num.transform.fft.number.Power2Util;
import matsu.num.transform.fft.scaling.AbstractScalingRealBiLinear;

/**
 * 2の累乗のデータサイズの巡回畳み込みの実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain RealNumbersCyclicConvolution} は,
 * 2の累乗のデータサイズにのみ対応する.
 * </p>
 * 
 * <p>
 * {@linkplain RealNumbersCyclicConvolution#apply(double[], double[])}
 * メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが2の累乗でない場合}</li>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE}
 * {@code を超える場合}</li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public final class Power2CyclicConvolutionExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>30</sup>
     */
    public static final int MAX_DATA_SIZE = Power2CyclicConvolutionModule.MAX_SEQUENCE_SIZE;

    private static final RealNumbersCyclicConvolution INSTANCE = new Power2CyclicConvImpl();

    private Power2CyclicConvolutionExecutor() {
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

    private static final class Power2CyclicConvImpl
            extends AbstractScalingRealBiLinear
            implements RealNumbersCyclicConvolution {

        private static final String CLASS_STRING = "Power2-CyclicConvolution";

        private final Power2CyclicConvolutionModule module = Power2CyclicConvolutionModule.instance();

        Power2CyclicConvImpl() {
            super();
        }

        /**
         * @throws IllegalArgumentException サイズが2の累乗でない場合,
         *             サイズが{@link #MAX_DATA_SIZE}を超える場合
         */
        @Override
        protected double[] applyInner(double[] f, double[] g) {
            int size = f.length;

            if (!Power2Util.isPowerOf2(size)) {
                throw new IllegalArgumentException("サイズが2の累乗でない");
            }

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
         * {@code %変換名}
         * </p>
         */
        @Override
        public String toString() {
            return CLASS_STRING;
        }

    }

}
