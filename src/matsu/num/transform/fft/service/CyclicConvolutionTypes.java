/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.21
 */
package matsu.num.transform.fft.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import matsu.num.transform.fft.convolution.GenericCyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.impl.GenericCyclicConvolutionExecutorImpl;
import matsu.num.transform.fft.convolution.impl.Power2CyclicConvolutionExecutorImpl;

/**
 * {@link ExecutorType} 型の巡回畳み込みに関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 */
public final class CyclicConvolutionTypes {

    private CyclicConvolutionTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 任意サイズの実数列に対応する巡回畳み込みの実行手段を表す.
     */
    public static final ExecutorType<
            GenericCyclicConvolutionExecutor> GENERIC_CYCLIC_CONVOLUTION_EXECUTOR;

    /**
     * 2の累乗サイズの実数列に対応する巡回畳み込みの実行手段を表す.
     */
    public static final ExecutorType<
            Power2CyclicConvolutionExecutor> POWER2_CYCLIC_CONVOLUTION_EXECUTOR;

    private static final Collection<ExecutorType<?>> values;

    static {
        List<ExecutorType<?>> list = new ArrayList<>();

        GENERIC_CYCLIC_CONVOLUTION_EXECUTOR =
                new ExecutorType<>(
                        "GENERIC_CYCLIC_CONVOLUTION_EXECUTOR", GenericCyclicConvolutionExecutor.class,
                        p -> new GenericCyclicConvolutionExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_CYCLIC_CONVOLUTION_EXECUTOR);

        POWER2_CYCLIC_CONVOLUTION_EXECUTOR =
                new ExecutorType<>(
                        "POWER2_CYCLIC_CONVOLUTION_EXECUTOR", Power2CyclicConvolutionExecutor.class,
                        p -> new Power2CyclicConvolutionExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(POWER2_CYCLIC_CONVOLUTION_EXECUTOR);

        values = Collections.unmodifiableList(list);
    }

    /**
     * このクラスが管理するエグゼキュータタイプのコレクションを返す.
     * 
     * @return エグゼキュータタイプのコレクション
     */
    static Collection<ExecutorType<?>> values() {
        return values;
    }
}
