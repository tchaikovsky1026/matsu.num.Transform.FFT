/**
 * 2023.12.5
 */
package matsu.num.transform.fft.scaling;

import java.util.Arrays;

import matsu.num.commons.ArraysUtil;
import matsu.num.transform.fft.ComplexNumbersLinearTransform;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 全単射の複素数列の線形変換において,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで{@linkplain #apply(ComplexNumberArrayDTO)}メソッドを実装しており,
 * 引数のチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@linkplain #applyInner(ComplexNumberArrayDTO)}を素朴に実装すればよい.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public abstract class AbstractScalingComplexLinear implements ComplexNumbersLinearTransform {

    protected AbstractScalingComplexLinear() {
        super();
    }

    @Override
    public final ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray) {

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

        ComplexNumberArrayDTO out = this.applyInner(cloneData);
        double[] outRealPart = out.realPart;
        double[] outImaginaryPart = out.imaginaryPart;
        for (int k = 0; k < size; k++) {
            outRealPart[k] *= scale;
            outImaginaryPart[k] *= scale;
        }

        return out;
    }

    /**
     * {@linkplain #apply(ComplexNumberArrayDTO)}から呼ばれる, 線形変換を実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数は必ず {@code null} でなく,
     * {@code size} は1以上であることが保証されている. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * <p>
     * {@linkplain #apply(ComplexNumberArrayDTO)}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param complexNumberArray 複素数列, sizeが1以上であることが保証された配列
     * @return 変換後の複素数列(入力と同じ長さである)
     */
    protected abstract ComplexNumberArrayDTO applyInner(ComplexNumberArrayDTO complexNumberArray);

}
