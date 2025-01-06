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

import matsu.num.transform.fft.GenericDFTExecutor;
import matsu.num.transform.fft.GenericIDFTExecutor;
import matsu.num.transform.fft.dft.impl.GenericDFTExecutorImpl;
import matsu.num.transform.fft.dft.impl.GenericIDFTExecutorImpl;

/**
 * {@link ExecutorType} 型の離散Fourier変換と逆変換に関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 * @version 22.1
 */
public final class DftTypes {

    private DftTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

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
                "GENERIC_DFT_EXECUTOR", GenericDFTExecutor.class,
                p -> new GenericDFTExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
        list.add(GENERIC_DFT_EXECUTOR);

        GENERIC_IDFT_EXECUTOR = new ExecutorType<>(
                "GENERIC_IDFT_EXECUTOR", GenericIDFTExecutor.class,
                p -> new GenericIDFTExecutorImpl(p.lib().trigonometry(), p.lib().arrayUtil()));
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
}
