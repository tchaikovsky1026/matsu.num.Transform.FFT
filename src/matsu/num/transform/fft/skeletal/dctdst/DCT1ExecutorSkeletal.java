/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.skeletal.dctdst;

import matsu.num.transform.fft.dctdst.DCT1Executor;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
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
 * @version 20.0
 */
public abstract class DCT1ExecutorSkeletal
        extends LinearByScalingStability implements DCT1Executor {

    /**
     * 唯一のコンストラクタ.
     * 
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    protected DCT1ExecutorSkeletal(ArraysUtil arraysUtil) {
        super(2, MAX_DATA_SIZE, arraysUtil);
    }

    @Override
    public String toString() {
        return "DCT1Executor";
    }
}
