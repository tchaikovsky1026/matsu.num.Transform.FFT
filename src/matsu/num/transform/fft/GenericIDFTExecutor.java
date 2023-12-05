/**
 * 2023.12.5
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.AbstractScalingComplexLinear;

/**
 * 汎用的に使えるIDFTの実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain IDFTExecutor} は,
 * 任意のデータサイズのIDFTに対応する.
 * </p>
 * 
 * <p>
 * {@linkplain IDFTExecutor#apply(ComplexNumberArrayDTO)}
 * メソッドで追加でスローされる条件は次のとおりである.
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
public final class GenericIDFTExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE;

    private static final IDFTExecutor INSTANCE = new IDFTExecutorImpl();

    private GenericIDFTExecutor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * IDFTの実行インスタンスを返す.
     * 
     * @return IDFT実行インスタンス
     */
    public static IDFTExecutor instance() {
        return INSTANCE;
    }

    private static final class IDFTExecutorImpl
            extends AbstractScalingComplexLinear implements IDFTExecutor {

        private static final String CLASS_STRING = "Generic-IDFT";

        private static final FourierType TYPE = FourierType.IDFT;

        private final InnerDFTExecutor innerDFTExecutor = GenericInnerFFTExecutor.instance();

        /**
         * IDFTExecutorを構築する.
         */
        IDFTExecutorImpl() {
            super();
        }

        /**
         * @throws IllegalArgumentException サイズが{@link #MAX_DATA_SIZE}を超える場合
         */
        @Override
        protected ComplexNumberArrayDTO applyInner(ComplexNumberArrayDTO complexNumberArray) {
            int size = complexNumberArray.size;
            if (size > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            //複素数の配列
            ComplexNumber[] data =
                    ComplexNumber.from(complexNumberArray.realPart, complexNumberArray.imaginaryPart);

            //変換を実行
            ComplexNumber[] result = this.innerDFTExecutor
                    .compute(data, FourierBasisComputer.covering(data.length, IDFTExecutorImpl.TYPE));
            double[][] resultArray = ComplexNumber.separateToArrays(result);

            //DTOに
            ComplexNumberArrayDTO out = ComplexNumberArrayDTO.zeroFilledOf(size);
            System.arraycopy(resultArray[0], 0, out.realPart, 0, size);
            System.arraycopy(resultArray[1], 0, out.imaginaryPart, 0, size);

            return out;
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
