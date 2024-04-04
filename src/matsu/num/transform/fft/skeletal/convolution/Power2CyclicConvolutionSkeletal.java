/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.skeletal.convolution;

import matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.number.Power2Util;
import matsu.num.transform.fft.skeletal.BiLinearByScalingStability;
import matsu.num.transform.fft.validation.DataSizeNotMismatchException;
import matsu.num.transform.fft.validation.StructureRejected;

/**
 * {@link Power2CyclicConvolutionExecutor} の骨格実装を扱う.
 * 
 * <p>
 * {@link Power2CyclicConvolutionExecutor} で追加されたreject条件を扱うため,
 * {@link #acceptsSize(int)} をオーバーライドし, finalとした.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public abstract class Power2CyclicConvolutionSkeletal
        extends BiLinearByScalingStability implements Power2CyclicConvolutionExecutor {

    /**
     * 唯一のコンストラクタ.
     * 
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    protected Power2CyclicConvolutionSkeletal(ArraysUtil arraysUtil) {
        super(MAX_DATA_SIZE, arraysUtil);
        this.addRejectionContract(
                size -> !Power2Util.isPowerOf2(size),
                StructureRejected
                        .by(() -> new DataSizeNotMismatchException("データサイズが2の累乗でない"), "REJECT_BY_NOT_POWER_OF_2"));
    }

    @Override
    public String toString() {
        return "Power2CyclicConvolutionExecutor";
    }

}
