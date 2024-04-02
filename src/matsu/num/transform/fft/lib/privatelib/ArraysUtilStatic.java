/**
 * 2024.4.2
 */
package matsu.num.transform.fft.lib.privatelib;

/**
 * 配列に対するユーティリティ.
 *
 * @author Matsuura Y.
 * @deprecated このユーティリティは使用されておらず, 公開もされていない.
 *                 削除しても良い.
 */
@Deprecated(forRemoval = true)
public final class ArraysUtilStatic {

    private ArraysUtilStatic() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * <p>
     * 最大値ノルムを計算する:
     * ||<b>v</b>||<sub>&infin;</sub>.
     * </p>
     *
     * <p>
     * <b>v</b> のサイズが0の場合, 0が返る.
     * </p>
     *
     * @param vector ベクトル <b>v</b>
     * @return 最大ノルム ||<b>v</b>||<sub>&infin;</sub>
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static final double normMax(double[] vector) {
        double outputValue = 0.0;
        int index;
        for (index = vector.length - 1; index >= 3; index -= 4) {
            double v0 = Math.abs(vector[index]);
            double v1 = Math.abs(vector[index - 1]);
            double v2 = Math.abs(vector[index - 2]);
            double v3 = Math.abs(vector[index - 3]);
            double v01 = Math.max(v0, v1);
            double v23 = Math.max(v2, v3);
            outputValue = Math.max(outputValue, Math.max(v01, v23));
        }
        for (; index >= 0; index--) {
            double v0 = Math.abs(vector[index]);
            outputValue = Math.max(outputValue, v0);
        }
        return outputValue;
    }
}
