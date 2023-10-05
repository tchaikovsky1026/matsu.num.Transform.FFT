/**
 * 2023.9.30
 */
package matsu.num.transform.fft.convolution;

import java.util.Arrays;

import matsu.num.commons.ArraysUtil;

/**
 * スケーリングによる計算安定性を向上させた, 実数列の巡回畳み込みの骨格実装.
 * 
 * <p>
 * {@linkplain #apply(double[], double[])} メソッドはこの抽象クラス内で実装されており, <br>
 * 事前条件(以下)のチェックが行われた後にスケーリングを行い, 不正値が混入している場合は{@code Double#NaN}で埋めてreturnされる.
 * </p>
 * 
 * <ul>
 * <li> fとgの長さは整合し, かつ長さは1以上である. </li>
 * </ul>
 * 
 * <p>
 * この抽象クラスを継承したクラスでは, {@linkplain #applyInner(double[], double[])}を実装する. <br>
 * {@linkplain #applyInner(double[], double[])}は事前条件をクリアした状態で呼び出される. <br>
 * 実装規約はメソッドの説明文を参照のこと.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.8
 */
abstract class ScalingRealNumbersCyclicConvolution implements RealNumbersCyclicConvolution {

    protected ScalingRealNumbersCyclicConvolution() {

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
     * 巡回畳み込みの内部処理を記述する. <br>
     * このメソッドの引数には必ず正常値(以下)が渡される.
     * 
     * <ul>
     * <li> fとgの長さは整合し, かつ長さは1以上である. </li>
     * <li> fとgはスケーリングされている. </li>
     * </ul>
     * 
     * <p>
     * <i>実装規約:</i>
     * </p>
     * 
     * <p>
     * この抽象クラスで実装された{@linkplain #apply(double[], double[])}メソッドは, 内部で配列の複製を行っている. <br>
     * したがって, このメソッドの実装において, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li> 引数と同じ参照を持つオブジェクトを返しても良い. </li>
     * <li> 引数の内部の値を書き換えても良い. </li>
     * </ul>
     * 
     * @param f f
     * @param g g
     * @return 畳み込みの結果
     */
    protected abstract double[] applyInner(double[] f, double[] g);
}
