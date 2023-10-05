/**
 * 2023.9.29
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.fftmodule.InnerDFTExecutor;
import matsu.num.transform.fft.scaling.ScalingComplexNumberTransform;

/**
 * 汎用的に使えるDFTの実行手段を提供する.
 * 
 * <p>
 * このクラスが提供する {@linkplain DFTExecutor} は,
 * 任意のデータサイズのDFTに対応する.
 * </p>
 * 
 * <p>
 * {@linkplain DFTExecutor#apply(ComplexNumberArrayDTO)} メソッドで追加でスローされる条件は次のとおりである.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@linkplain #MAX_DATA_SIZE} {@code を超える場合} </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public final class GenericDFTExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = GenericInnerFFTExecutor.MAX_DATA_SIZE;

    private static final DFTExecutor INSTANCE = new DFTExecutorImpl();

    private GenericDFTExecutor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * DFTの実行インスタンスを返す.
     * 
     * @return DFT実行インスタンス
     */
    public static DFTExecutor instance() {
        return INSTANCE;
    }

    private static final class DFTExecutorImpl implements DFTExecutor {

        private static final String CLASS_STRING = "Generic-DFT";

        private static final FourierType TYPE = FourierType.DFT;

        private final ComplexNumbersLinearBijectiveTransform dft =
                ScalingComplexNumberTransform.decorate(new NonScalingDFT());

        /**
         * DFTExecutorを構築する.
         */
        DFTExecutorImpl() {

        }

        @Override
        public ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArrayDto) {
            if (complexNumberArrayDto.size == 0) {
                throw new IllegalArgumentException("データサイズが0である");
            }
            if (complexNumberArrayDto.size > MAX_DATA_SIZE) {
                throw new IllegalArgumentException("サイズが大きすぎる");
            }

            return this.dft.apply(complexNumberArrayDto);
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
         * スケーリング, 事前条件チェック, 防御的コピーのないのDFT.
         */
        private static final class NonScalingDFT implements DFTExecutor {

            private final InnerDFTExecutor innerDFTExecutor = GenericInnerFFTExecutor.instance();

            @Override
            public ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray) {

                //複素数の配列
                ComplexNumber[] data =
                        ComplexNumber.from(complexNumberArray.realPart, complexNumberArray.imaginaryPart);

                //変換を実行
                ComplexNumber[] result = this.innerDFTExecutor
                        .compute(data, FourierBasisComputer.covering(data.length, DFTExecutorImpl.TYPE));
                double[][] resultArray = ComplexNumber.separateToArrays(result);

                //DTOに
                int size = result.length;
                ComplexNumberArrayDTO out = ComplexNumberArrayDTO.zeroFilledOf(size);
                System.arraycopy(resultArray[0], 0, out.realPart, 0, size);
                System.arraycopy(resultArray[1], 0, out.imaginaryPart, 0, size);

                return out;
            }

        }
    }
}
