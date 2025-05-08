/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.5.8
 */
package matsu.num.transform.fft.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
public final class DctDstTypes {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private DctDstTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 任意サイズに対応するDCT-1の実行手段を表す.
     */
    public static final ExecutorType<DCT1Executor> GENERIC_DCT1_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-2の実行手段を表す.
     */
    public static final ExecutorType<DCT2Executor> GENERIC_DCT2_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-3の実行手段を表す.
     */
    public static final ExecutorType<DCT3Executor> GENERIC_DCT3_EXECUTOR;

    /**
     * 任意サイズに対応するDCT-4の実行手段を表す.
     */
    public static final ExecutorType<DCT4Executor> GENERIC_DCT4_EXECUTOR;

    /**
     * 任意サイズに対応するDST-1の実行手段を表す.
     */
    public static final ExecutorType<DST1Executor> GENERIC_DST1_EXECUTOR;

    /**
     * 任意サイズに対応するDST-2の実行手段を表す.
     */
    public static final ExecutorType<DST2Executor> GENERIC_DST2_EXECUTOR;

    /**
     * 任意サイズに対応するDST-3の実行手段を表す.
     */
    public static final ExecutorType<DST3Executor> GENERIC_DST3_EXECUTOR;

    /**
     * 任意サイズに対応するDST-4の実行手段を表す.
     */
    public static final ExecutorType<DST4Executor> GENERIC_DST4_EXECUTOR;

    private static final Collection<ExecutorType<?>> values;

    static {
        List<ExecutorType<?>> list = new ArrayList<>();

        GENERIC_DCT1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT1_EXECUTOR", DCT1Executor.class,
                p -> new GenericDCT1Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT1_EXECUTOR);

        GENERIC_DCT2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT2_EXECUTOR", DCT2Executor.class,
                p -> new GenericDCT2Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT2_EXECUTOR);

        GENERIC_DCT3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT3_EXECUTOR", DCT3Executor.class,
                p -> new GenericDCT3Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT3_EXECUTOR);

        GENERIC_DCT4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DCT4_EXECUTOR", DCT4Executor.class,
                p -> new GenericDCT4Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DCT4_EXECUTOR);

        GENERIC_DST1_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST1_EXECUTOR", DST1Executor.class,
                p -> new GenericDST1Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST1_EXECUTOR);

        GENERIC_DST2_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST2_EXECUTOR", DST2Executor.class,
                p -> new GenericDST2Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST2_EXECUTOR);

        GENERIC_DST3_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST3_EXECUTOR", DST3Executor.class,
                p -> new GenericDST3Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DST3_EXECUTOR);

        GENERIC_DST4_EXECUTOR = new ExecutorType<>(
                "GENERIC_DST4_EXECUTOR", DST4Executor.class,
                p -> new GenericDST4Executor(p.lib().trigonometry(), p.lib().arrayUtil()));
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
