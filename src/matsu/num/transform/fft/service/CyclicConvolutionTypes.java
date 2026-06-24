/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2026.6.24
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.convolution.CyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.impl.GenericCyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.impl.Power2CyclicConvolutionExecutor;

/**
 * {@link ExecutorType} 型の巡回畳み込みに関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 * @deprecated このクラスは v25以降に削除される. {@link CyclicConvolutionExecutors}
 *                 が全く同じ定数を提供する.
 */
@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
public final class CyclicConvolutionTypes {

    private CyclicConvolutionTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 任意サイズの実数列に対応する巡回畳み込みの実行手段を表す.
     * 
     * <p>
     * {@link CyclicConvolutionExecutor#accepts(double[], double[])}
     * で受け入れられる入力は, <br>
     * {@link CyclicConvolutionExecutor} と同一である.
     * </p>
     */
    @Deprecated(forRemoval = true)
    public static final ExecutorType<
            matsu.num.transform.fft.convolution.GenericCyclicConvolutionExecutor> GENERIC_CYCLIC_CONVOLUTION_EXECUTOR;

    /**
     * 2の累乗サイズの実数列に対応する巡回畳み込みの実行手段を表す.
     * 
     * <p>
     * {@link CyclicConvolutionExecutor#accepts(double[], double[])}
     * で受け入れられる入力は, <br>
     * {@link CyclicConvolutionExecutor} に加えて
     * 2の累乗サイズでなければならない.
     * </p>
     */
    @Deprecated(forRemoval = true)
    public static final ExecutorType<
            matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor> POWER2_CYCLIC_CONVOLUTION_EXECUTOR;

    static {
        GENERIC_CYCLIC_CONVOLUTION_EXECUTOR =
                new ExecutorType<>(
                        "GENERIC_CYCLIC_CONVOLUTION_EXECUTOR",
                        matsu.num.transform.fft.convolution.GenericCyclicConvolutionExecutor.class,
                        p -> new GenericCyclicConvolutionExecutor(
                                p.lib().trigonometry(), p.lib().arrayUtil()));

        POWER2_CYCLIC_CONVOLUTION_EXECUTOR =
                new ExecutorType<>(
                        "POWER2_CYCLIC_CONVOLUTION_EXECUTOR",
                        matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor.class,
                        p -> new Power2CyclicConvolutionExecutor(
                                p.lib().trigonometry(), p.lib().arrayUtil()));
    }
}
