/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
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
import matsu.num.transform.fft.service.fuctionaltype.FunctionalTypeImpl;

/**
 * {@link ExecutorType} 型の離散cosine/sine変換に関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 * @version 20.0
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

        GENERIC_DCT1_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDCT1Executor.class, lib -> new GenericDCT1ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DCT1_EXECUTOR");
        list.add(GENERIC_DCT1_EXECUTOR);

        GENERIC_DCT2_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDCT2Executor.class, lib -> new GenericDCT2ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DCT2_EXECUTOR");
        list.add(GENERIC_DCT2_EXECUTOR);

        GENERIC_DCT3_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDCT3Executor.class, lib -> new GenericDCT3ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DCT3_EXECUTOR");
        list.add(GENERIC_DCT3_EXECUTOR);

        GENERIC_DCT4_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDCT4Executor.class, lib -> new GenericDCT4ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DCT4_EXECUTOR");
        list.add(GENERIC_DCT4_EXECUTOR);

        GENERIC_DST1_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDST1Executor.class, lib -> new GenericDST1ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DST1_EXECUTOR");
        list.add(GENERIC_DST1_EXECUTOR);

        GENERIC_DST2_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDST2Executor.class, lib -> new GenericDST2ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DST2_EXECUTOR");
        list.add(GENERIC_DST2_EXECUTOR);

        GENERIC_DST3_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDST3Executor.class, lib -> new GenericDST3ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
                "GENERIC_DST3_EXECUTOR");
        list.add(GENERIC_DST3_EXECUTOR);

        GENERIC_DST4_EXECUTOR = new FunctionalTypeImpl<>(
                GenericDST4Executor.class, lib -> new GenericDST4ExecutorImpl(lib.trigonometry(), lib.arrayUtil()),
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
}
