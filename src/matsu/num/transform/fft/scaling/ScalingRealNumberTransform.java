/**
 * 2023.9.29
 */
package matsu.num.transform.fft.scaling;

import java.util.Arrays;
import java.util.Objects;

import matsu.num.commons.ArraysUtil;
import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;

/**
 * 引数をスケーリングすることで計算安定性を高めるデコレータ.
 * 
 * <p>
 * デコレートされた線形変換の{@linkplain #apply(double[])}メソッドは内部で配列の複製を行っている. <br>
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
 */
public final class ScalingRealNumberTransform implements RealNumbersLinearBijectiveTransform {

    private final RealNumbersLinearBijectiveTransform transform;

    private ScalingRealNumberTransform(RealNumbersLinearBijectiveTransform transform) {
        this.transform = Objects.requireNonNull(transform);
    }

    @Override
    public double[] apply(double[] data) {

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

        double[] out = this.transform.apply(cloneData);
        for (int k = 0; k < size; k++) {
            out[k] *= scale;
        }

        return out;
    }

    /**
     * 与えた線形変換にスケーリング安定性を付与する.
     * 
     * @param transform 元の線形変換
     * @return 計算安定性を高めた線形変換
     */
    public static RealNumbersLinearBijectiveTransform decorate(RealNumbersLinearBijectiveTransform transform) {
        return new ScalingRealNumberTransform(transform);
    }

}
