/**
 * 2023.9.29
 */
package matsu.num.transform.fft.scaling;

import java.util.Arrays;
import java.util.Objects;

import matsu.num.commons.ArraysUtil;
import matsu.num.transform.fft.ComplexNumbersLinearTransform;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 引数をスケーリングすることで計算安定性を高めるデコレータ.
 * 
 * <p>
 * デコレートされた線形変換の{@linkplain #apply(ComplexNumberArrayDTO)}メソッドは内部で配列の複製を行っている. <br>
 * したがって, 呼び出し元で配列を複製する必要はない. <br>
 * また, デコレート前の線形変換において, 次のことも許される.
 * </p>
 * 
 * <ul>
 * <li> 引数と同じ参照を持つオブジェクトを返しても良い. </li>
 * <li> 引数の内部の値を書き換えても良い. </li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 12.7
 * @deprecated 抽象クラスによるテンプレートメソッドによる代替({@linkplain AbstractScalingComplexLinear})
 */
@Deprecated
public final class ScalingComplexNumberTransform implements ComplexNumbersLinearTransform {

    private final ComplexNumbersLinearTransform transform;

    private ScalingComplexNumberTransform(ComplexNumbersLinearTransform transform) {
        this.transform = Objects.requireNonNull(transform);
    }

    @Override
    public ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray) {

        if (complexNumberArray.size == 0) {
            throw new IllegalArgumentException("データサイズが0である");
        }

        int size = complexNumberArray.size;

        //データのクローン
        ComplexNumberArrayDTO cloneData = ComplexNumberArrayDTO.zeroFilledOf(size);
        System.arraycopy(complexNumberArray.realPart, 0, cloneData.realPart, 0, size);
        System.arraycopy(complexNumberArray.imaginaryPart, 0, cloneData.imaginaryPart, 0, size);

        double[] cloneRealPart = cloneData.realPart;
        double[] cloneImaginaryPart = cloneData.imaginaryPart;

        double scale = Math.max(
                ArraysUtil.normMax(cloneRealPart), ArraysUtil.normMax(cloneImaginaryPart));

        //不正な値が入っている場合はNaNにしてreturn
        if (!Double.isFinite(scale)) {
            Arrays.fill(cloneRealPart, Double.NaN);
            Arrays.fill(cloneImaginaryPart, Double.NaN);
            return cloneData;
        }
        //スケールを正規化し, dataをスケーリング
        scale = scale == 0d ? 1d : scale;
        for (int j = 0; j < size; j++) {
            cloneRealPart[j] /= scale;
            cloneImaginaryPart[j] /= scale;
        }

        ComplexNumberArrayDTO out = this.transform.apply(cloneData);
        double[] outRealPart = out.realPart;
        double[] outImaginaryPart = out.imaginaryPart;
        for (int k = 0; k < size; k++) {
            outRealPart[k] *= scale;
            outImaginaryPart[k] *= scale;
        }

        return out;
    }

    /**
     * 与えた線形変換にスケーリング安定性を付与する.
     * 
     * @param transform 元の線形変換
     * @return 計算安定性を高めた線形変換
     */
    public static ComplexNumbersLinearTransform decorate(ComplexNumbersLinearTransform transform) {
        return new ScalingComplexNumberTransform(transform);
    }

}
