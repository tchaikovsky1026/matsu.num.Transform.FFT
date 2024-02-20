/**
 * 2024.2.20
 */
package matsu.num.transform.fft.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import matsu.num.transform.fft.GenericDFTExecutor;
import matsu.num.transform.fft.GenericIDFTExecutor;
import matsu.num.transform.fft.convolution.GenericCyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor;
import matsu.num.transform.fft.dctdst.GenericDCT1Executor;
import matsu.num.transform.fft.dctdst.GenericDCT2Executor;
import matsu.num.transform.fft.dctdst.GenericDCT3Executor;
import matsu.num.transform.fft.dctdst.GenericDCT4Executor;
import matsu.num.transform.fft.dctdst.GenericDST1Executor;
import matsu.num.transform.fft.dctdst.GenericDST2Executor;
import matsu.num.transform.fft.dctdst.GenericDST3Executor;
import matsu.num.transform.fft.dctdst.GenericDST4Executor;
import matsu.num.transform.fft.service.convolution.GenericCyclicConvolutionExecutorImpl;
import matsu.num.transform.fft.service.convolution.Power2CyclicConvolutionExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDCT1ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDCT2ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDCT3ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDCT4ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDST1ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDST2ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDST3ExecutorImpl;
import matsu.num.transform.fft.service.dctdst.GenericDST4ExecutorImpl;
import matsu.num.transform.fft.service.dft.GenericDFTExecutorImpl;
import matsu.num.transform.fft.service.dft.GenericIDFTExecutorImpl;

/**
 * {@link ExecutorType} 型の定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 */
public final class ExecutorTypes {

    private ExecutorTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * このクラスが管理するエグゼキュータタイプのコレクションを返す.
     * 
     * @return エグゼキュータタイプのコレクション
     */
    static Collection<ExecutorType<?>> values() {
        List<ExecutorType<?>> list = new ArrayList<>();

        list.addAll(CyclicConvolution.values());
        list.addAll(DCTDST.values());
        list.addAll(DFT.values());

        return Collections.unmodifiableList(list);
    }

    /**
     * 巡回畳み込みに関する.
     * 
     * @author Matsuura Y.
     * @version 18.0
     */
    public static final class CyclicConvolution {

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
                            GenericCyclicConvolutionExecutor.class,
                            lib -> new GenericCyclicConvolutionExecutorImpl(lib),
                            "GENERIC_CYCLIC_CONVOLUTION_EXECUTOR");
            list.add(GENERIC_CYCLIC_CONVOLUTION_EXECUTOR);

            POWER2_CYCLIC_CONVOLUTION_EXECUTOR =
                    new ExecutorType<>(
                            Power2CyclicConvolutionExecutor.class,
                            lib -> new Power2CyclicConvolutionExecutorImpl(lib),
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

        private CyclicConvolution() {
            //インスタンス化不可
            throw new AssertionError();
        }

    }

    /**
     * 離散cosine/sine変換に関する.
     * 
     * @author Matsuura Y.
     * @version 18.0
     */
    public static final class DCTDST {

        /**
         * 任意サイズに対応するDCT-1の実行手段を表す.
         */
        public static final ExecutorType<GenericDCT1Executor> GENERIC_DCT1_EXECUTOR;

        /**
         * 任意サイズに対応するDCT-2の実行手段を表す.
         */
        public static final ExecutorType<GenericDCT2Executor> GENERIC_DCT2_EXECUTOR;

        /**
         * 任意サイズに対応するDCT-3の実行手段を表す.
         */
        public static final ExecutorType<GenericDCT3Executor> GENERIC_DCT3_EXECUTOR;

        /**
         * 任意サイズに対応するDCT-4の実行手段を表す.
         */
        public static final ExecutorType<GenericDCT4Executor> GENERIC_DCT4_EXECUTOR;

        /**
         * 任意サイズに対応するDST-1の実行手段を表す.
         */
        public static final ExecutorType<GenericDST1Executor> GENERIC_DST1_EXECUTOR;

        /**
         * 任意サイズに対応するDST-1の実行手段を表す.
         */
        public static final ExecutorType<GenericDST2Executor> GENERIC_DST2_EXECUTOR;

        /**
         * 任意サイズに対応するDST-1の実行手段を表す.
         */
        public static final ExecutorType<GenericDST3Executor> GENERIC_DST3_EXECUTOR;

        /**
         * 任意サイズに対応するDST-1の実行手段を表す.
         */
        public static final ExecutorType<GenericDST4Executor> GENERIC_DST4_EXECUTOR;

        private static final Collection<ExecutorType<?>> values;

        static {
            List<ExecutorType<?>> list = new ArrayList<>();

            GENERIC_DCT1_EXECUTOR = new ExecutorType<>(
                    GenericDCT1Executor.class, lib -> new GenericDCT1ExecutorImpl(lib),
                    "GENERIC_DCT1_EXECUTOR");
            list.add(GENERIC_DCT1_EXECUTOR);

            GENERIC_DCT2_EXECUTOR = new ExecutorType<>(
                    GenericDCT2Executor.class, lib -> new GenericDCT2ExecutorImpl(lib),
                    "GENERIC_DCT2_EXECUTOR");
            list.add(GENERIC_DCT2_EXECUTOR);

            GENERIC_DCT3_EXECUTOR = new ExecutorType<>(
                    GenericDCT3Executor.class, lib -> new GenericDCT3ExecutorImpl(lib),
                    "GENERIC_DCT3_EXECUTOR");
            list.add(GENERIC_DCT3_EXECUTOR);

            GENERIC_DCT4_EXECUTOR = new ExecutorType<>(
                    GenericDCT4Executor.class, lib -> new GenericDCT4ExecutorImpl(lib),
                    "GENERIC_DCT4_EXECUTOR");
            list.add(GENERIC_DCT4_EXECUTOR);

            GENERIC_DST1_EXECUTOR = new ExecutorType<>(
                    GenericDST1Executor.class, lib -> new GenericDST1ExecutorImpl(lib),
                    "GENERIC_DST1_EXECUTOR");
            list.add(GENERIC_DST1_EXECUTOR);

            GENERIC_DST2_EXECUTOR = new ExecutorType<>(
                    GenericDST2Executor.class, lib -> new GenericDST2ExecutorImpl(lib),
                    "GENERIC_DST2_EXECUTOR");
            list.add(GENERIC_DST2_EXECUTOR);

            GENERIC_DST3_EXECUTOR = new ExecutorType<>(
                    GenericDST3Executor.class, lib -> new GenericDST3ExecutorImpl(lib),
                    "GENERIC_DST3_EXECUTOR");
            list.add(GENERIC_DST3_EXECUTOR);

            GENERIC_DST4_EXECUTOR = new ExecutorType<>(
                    GenericDST4Executor.class, lib -> new GenericDST4ExecutorImpl(lib),
                    "GENERIC_DST4_EXECUTOR");
            list.add(GENERIC_DST4_EXECUTOR);

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

        private DCTDST() {
            //インスタンス化不可
            throw new AssertionError();
        }
    }

    /**
     * 離散Fourier変換と逆変換に関する.
     * 
     * @author Matsuura Y.
     * @version 18.0
     */
    public static final class DFT {

        /**
         * 任意サイズに対応するDFTの実行手段を表す.
         */
        public static final ExecutorType<GenericDFTExecutor> GENERIC_DFT_EXECUTOR;

        /**
         * 任意サイズに対応するIDFTの実行手段を表す.
         */
        public static final ExecutorType<GenericIDFTExecutor> GENERIC_IDFT_EXECUTOR;

        private static final Collection<ExecutorType<?>> values;

        static {
            List<ExecutorType<?>> list = new ArrayList<>();

            GENERIC_DFT_EXECUTOR = new ExecutorType<>(
                    GenericDFTExecutor.class, lib -> new GenericDFTExecutorImpl(lib),
                    "GENERIC_DFT_EXECUTOR");
            list.add(GENERIC_DFT_EXECUTOR);

            GENERIC_IDFT_EXECUTOR = new ExecutorType<>(
                    GenericIDFTExecutor.class, lib -> new GenericIDFTExecutorImpl(lib),
                    "GENERIC_IDFT_EXECUTOR");
            list.add(GENERIC_IDFT_EXECUTOR);

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

        private DFT() {
            //インスタンス化不可
            throw new AssertionError();
        }

    }

}
