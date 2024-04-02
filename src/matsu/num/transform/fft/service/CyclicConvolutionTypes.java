/**
 * 2024.4.2
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
import matsu.num.transform.fft.service.fuctionaltype.FunctionalTypeImpl;

/**
 * {@link ExecutorType} 型の巡回畳み込みに関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 * @version 19.0
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
                new FunctionalTypeImpl<>(
                        GenericCyclicConvolutionExecutor.class,
                        lib -> new GenericCyclicConvolutionExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                        "GENERIC_CYCLIC_CONVOLUTION_EXECUTOR");
        list.add(GENERIC_CYCLIC_CONVOLUTION_EXECUTOR);

        POWER2_CYCLIC_CONVOLUTION_EXECUTOR =
                new FunctionalTypeImpl<>(
                        Power2CyclicConvolutionExecutor.class,
                        lib -> new Power2CyclicConvolutionExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                        "POWER2_CYCLIC_CONVOLUTION_EXECUTOR");
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
