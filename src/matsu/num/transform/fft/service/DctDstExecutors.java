/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.6.21
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.dctdst.DCT1Executor;
import matsu.num.transform.fft.dctdst.DCT2Executor;
import matsu.num.transform.fft.dctdst.DCT3Executor;
import matsu.num.transform.fft.dctdst.DCT4Executor;
import matsu.num.transform.fft.dctdst.DST1Executor;
import matsu.num.transform.fft.dctdst.DST2Executor;
import matsu.num.transform.fft.dctdst.DST3Executor;
import matsu.num.transform.fft.dctdst.DST4Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDCT1Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDCT2Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDCT3Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDCT4Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDST1Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDST2Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDST3Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDST4Executor;

/**
 * {@link ExecutorType} 型の離散cosine/sine変換に関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 */
public final class DctDstExecutors {

    private DctDstExecutors() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 任意サイズに対応するDCT-1の実行手段を表す.
     * 
     * <p>
     * {@link DCT1Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DCT1Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DCT1Executor> GENERIC_DCT1_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-2の実行手段を表す.
     * 
     * <p>
     * {@link DCT2Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DCT2Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DCT2Executor> GENERIC_DCT2_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-3の実行手段を表す.
     * 
     * <p>
     * {@link DCT3Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DCT3Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DCT3Executor> GENERIC_DCT3_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-4の実行手段を表す.
     * 
     * <p>
     * {@link DCT4Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DCT4Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DCT4Executor> GENERIC_DCT4_EXECUTOR;

    /**
     * 任意サイズに対応するDST-1の実行手段を表す.
     * 
     * <p>
     * {@link DST1Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DST1Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DST1Executor> GENERIC_DST1_EXECUTOR;

    /**
     * 任意サイズに対応するDST-2の実行手段を表す.
     * 
     * <p>
     * {@link DST2Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DST2Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DST2Executor> GENERIC_DST2_EXECUTOR;

    /**
     * 任意サイズに対応するDST-3の実行手段を表す.
     * 
     * <p>
     * {@link DST3Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DST3Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DST3Executor> GENERIC_DST3_EXECUTOR;

    /**
     * 任意サイズに対応するDST-4の実行手段を表す.
     * 
     * <p>
     * {@link DST4Executor#accepts(double[])}
     * で受け入れられる入力は, <br>
     * {@link DST4Executor} と同一である.
     * </p>
     */
    public static final ExecutorType<DST4Executor> GENERIC_DST4_EXECUTOR;

    static {
        GENERIC_DCT1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT1_EXECUTOR", DCT1Executor.class,
                p -> new GenericDCT1Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DCT2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT2_EXECUTOR", DCT2Executor.class,
                p -> new GenericDCT2Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DCT3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT3_EXECUTOR", DCT3Executor.class,
                p -> new GenericDCT3Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DCT4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT4_EXECUTOR", DCT4Executor.class,
                p -> new GenericDCT4Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DST1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST1_EXECUTOR", DST1Executor.class,
                p -> new GenericDST1Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DST2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST2_EXECUTOR", DST2Executor.class,
                p -> new GenericDST2Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DST3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST3_EXECUTOR", DST3Executor.class,
                p -> new GenericDST3Executor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_DST4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST4_EXECUTOR", DST4Executor.class,
                p -> new GenericDST4Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
    }
}
