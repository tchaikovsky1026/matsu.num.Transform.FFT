/**
 * 2024.2.18
 */
package matsu.num.transform.fft.skeletal.dctdst;

import matsu.num.transform.fft.dctdst.DCT1Executor;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * {@link DCT1Executor} の骨格実装を扱う.
 * 
 * <p>
 * {@link DCT1Executor} のデータサイズ制約である,
 * サイズが1以下の場合にrejectすることを達成することを目的として準備している. <br>
 * 同時に, データサイズの上限: {@link DCT1Executor#MAX_DATA_SIZE}
 * による制限も実装済みである. <br>
 * したがってこのクラス上では, {@link DCT1Executor} のデータサイズ制約条件は達成されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public abstract class DCT1ExecutorSkeletal
        extends LinearByScalingStability implements DCT1Executor {

    /**
     * 唯一のコンストラクタ.
     */
    protected DCT1ExecutorSkeletal() {
        super(2, MAX_DATA_SIZE);
    }

    @Override
    public String toString() {
        return "DCT1Executor";
    }
}
