/**
 * 2023.12.5
 */
package matsu.num.transform.fft.scaling;

import java.util.Arrays;

import matsu.num.commons.ArraysUtil;
import matsu.num.transform.fft.RealNumbersLinearTransform;

/**
 * 全単射の実数列の線形変換において,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで{@linkplain #apply(double[])}メソッドを実装しており,
 * 引数のチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@linkplain #applyInner(double[])}を素朴に実装すればよい
 * (詳細はメソッド説明を参照).
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public abstract class AbstractScalingRealLinear implements RealNumbersLinearTransform {

    protected AbstractScalingRealLinear() {
        super();
    }

    /**
     * {@inheritDoc }
     * 
     * <p>
     * スローされる例外はインターフェースに従う.
     * </p>
     */
    @Override
    public final double[] apply(double[] data) {

        if (data.length == 0) {
            throw new IllegalArgumentException("データサイズが0である");
        }

        double[] cloneData = data.clone();
        int size = cloneData.length;
        double scale = ArraysUtil.normMax(cloneData);

        //不正な値が入っている場合はNaNにしてreturn
        if (!Double.isFinite(scale)) {
            //cloneData配列を使いまわす
            Arrays.fill(cloneData, Double.NaN);
            return cloneData;
        }
        //スケールを正規化し, dataをスケーリング
        scale = scale == 0d ? 1d : scale;
        for (int j = 0; j < size; j++) {
            cloneData[j] /= scale;
        }

        double[] out = this.applyInner(cloneData);
        for (int k = 0; k < size; k++) {
            out[k] *= scale;
        }

        return out;
    }

    /**
     * {@linkplain #apply(double[])}から呼ばれる, 線形変換を実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数は必ず {@code null} でなく,
     * {@code length} は1以上であることが保証されている. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * <p>
     * {@linkplain #apply(double[])}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param data 実数列, lengthが1以上であることが保証された配列
     * @return 変換後の実数列(入力と同じ長さである)
     */
    protected abstract double[] applyInner(double[] data);
}
