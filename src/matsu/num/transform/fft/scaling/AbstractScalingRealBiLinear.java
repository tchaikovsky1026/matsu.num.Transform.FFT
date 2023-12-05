/**
 * 2023.12.5
 */
package matsu.num.transform.fft.scaling;

import java.util.Arrays;

import matsu.num.commons.ArraysUtil;
import matsu.num.transform.fft.RealNumbersBiLinearTransform;

/**
 * 実数列の巡回畳み込みにおいて,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで{@linkplain #apply(double[], double[])}メソッドを実装しており,
 * 引数のチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@linkplain #applyInner(double[], double[])}を素朴に実装すればよい
 * (詳細はメソッド説明を参照).
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public abstract class AbstractScalingRealBiLinear implements RealNumbersBiLinearTransform {

    protected AbstractScalingRealBiLinear() {
        super();
    }

    @Override
    public final double[] apply(double[] f, double[] g) {

        int size = f.length;
        if (size != g.length) {
            throw new IllegalArgumentException("サイズが整合しない");
        }
        if (size == 0) {
            throw new IllegalArgumentException("サイズが0である");
        }

        double[] cloneF = f.clone();
        double[] cloneG = g.clone();
        double scaleF = ArraysUtil.normMax(cloneF);
        double scaleG = ArraysUtil.normMax(cloneG);

        //不正な値が入っている場合,NaNで埋めてreturn
        if (!(Double.isFinite(scaleF) && Double.isFinite(scaleG))) {
            //cloneFの配列を使いまわす
            Arrays.fill(cloneF, Double.NaN);
            return cloneF;
        }

        //スケールを正規化し,cloneF,Gをスケーリング
        scaleF = scaleF == 0d ? 1d : scaleF;
        scaleG = scaleG == 0d ? 1d : scaleG;
        for (int j = 0; j < size; j++) {
            cloneF[j] /= scaleF;
            cloneG[j] /= scaleG;
        }

        double[] result = this.applyInner(cloneF, cloneG);
        double scaleFG = scaleF * scaleG;
        for (int j = 0; j < size; j++) {
            result[j] *= scaleFG;
        }

        return result;
    }

    /**
     * {@linkplain #apply(double[], double[])}から呼ばれる, 巡回畳み込みを実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数は必ず {@code null} でなく,
     * {@code length} は整合し, かつ1以上であることが保証されている. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * 
     * 巡回畳み込みの内部処理を記述する. <br>
     * このメソッドの引数には必ず正常値(以下)が渡される.
     * 
     * <ul>
     * <li>fとgの長さは整合し, かつ長さは1以上である.</li>
     * <li>fとgはスケーリングされている.</li>
     * </ul>
     * 
     * <p>
     * {@linkplain #apply(double[], double[])}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param f f
     * @param g g
     * @return 畳み込みの結果
     */
    protected abstract double[] applyInner(double[] f, double[] g);
}
