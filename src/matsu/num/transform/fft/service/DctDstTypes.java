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

import matsu.num.transform.fft.dctdst.GenericDCT1Executor;
import matsu.num.transform.fft.dctdst.GenericDCT2Executor;
import matsu.num.transform.fft.dctdst.GenericDCT3Executor;
import matsu.num.transform.fft.dctdst.GenericDCT4Executor;
import matsu.num.transform.fft.dctdst.GenericDST1Executor;
import matsu.num.transform.fft.dctdst.GenericDST2Executor;
import matsu.num.transform.fft.dctdst.GenericDST3Executor;
import matsu.num.transform.fft.dctdst.GenericDST4Executor;
import matsu.num.transform.fft.dctdst.impl.GenericDCT1ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDCT2ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDCT3ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDCT4ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDST1ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDST2ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDST3ExecutorImpl;
import matsu.num.transform.fft.dctdst.impl.GenericDST4ExecutorImpl;

/**
 * {@link ExecutorType} 型の離散cosine/sine変換に関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 * @version 22.1
 */
public final class DctDstTypes {

    private DctDstTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

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
     * 任意サイズに対応するDST-2の実行手段を表す.
     */
    public static final ExecutorType<GenericDST2Executor> GENERIC_DST2_EXECUTOR;

    /**
     * 任意サイズに対応するDST-3の実行手段を表す.
     */
    public static final ExecutorType<GenericDST3Executor> GENERIC_DST3_EXECUTOR;

    /**
     * 任意サイズに対応するDST-4の実行手段を表す.
     */
    public static final ExecutorType<GenericDST4Executor> GENERIC_DST4_EXECUTOR;

    private static final Collection<ExecutorType<?>> values;

    static {
        List<ExecutorType<?>> list = new ArrayList<>();

        GENERIC_DCT1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT1_EXECUTOR", GenericDCT1Executor.class,
                p -> new GenericDCT1ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT1_EXECUTOR);

        GENERIC_DCT2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT2_EXECUTOR", GenericDCT2Executor.class,
                p -> new GenericDCT2ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT2_EXECUTOR);

        GENERIC_DCT3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT3_EXECUTOR", GenericDCT3Executor.class,
                p -> new GenericDCT3ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT3_EXECUTOR);

        GENERIC_DCT4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT4_EXECUTOR", GenericDCT4Executor.class,
                p -> new GenericDCT4ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT4_EXECUTOR);

        GENERIC_DST1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST1_EXECUTOR", GenericDST1Executor.class,
                p -> new GenericDST1ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST1_EXECUTOR);

        GENERIC_DST2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST2_EXECUTOR", GenericDST2Executor.class,
                p -> new GenericDST2ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST2_EXECUTOR);

        GENERIC_DST3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST3_EXECUTOR", GenericDST3Executor.class,
                p -> new GenericDST3ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST3_EXECUTOR);

        GENERIC_DST4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST4_EXECUTOR", GenericDST4Executor.class,
                p -> new GenericDST4ExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
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
}
