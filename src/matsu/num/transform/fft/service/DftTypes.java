/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.6.20
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.DFTExecutor;
import matsu.num.transform.fft.IDFTExecutor;
import matsu.num.transform.fft.dft.impl.GenericDFTExecutor;
import matsu.num.transform.fft.dft.impl.GenericIDFTExecutor;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * {@link ExecutorType} 型の離散Fourier変換と逆変換に関する定数を取りまとめるクラス.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class DftTypes {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private DftTypes() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 任意サイズに対応するDFTの実行手段を表す.
     * 
     * <p>
     * {@link DFTExecutor#accepts(ComplexNumberArrayDTO)}
     * で受け入れられる入力は, <br>
     * {@link DFTExecutor} と同一である.
     * </p>
     */
    public static final ExecutorType<matsu.num.transform.fft.GenericDFTExecutor> GENERIC_DFT_EXECUTOR;

    /**
     * 任意サイズに対応するIDFTの実行手段を表す.
     * 
     * <p>
     * {@link IDFTExecutor#accepts(ComplexNumberArrayDTO)}
     * で受け入れられる入力は, <br>
     * {@link IDFTExecutor} と同一である.
     * </p>
     */
    public static final ExecutorType<matsu.num.transform.fft.GenericIDFTExecutor> GENERIC_IDFT_EXECUTOR;

    static {
        GENERIC_DFT_EXECUTOR = new ExecutorType<>(
                "GENERIC_DFT_EXECUTOR", matsu.num.transform.fft.GenericDFTExecutor.class,
                p -> new GenericDFTExecutor(p.lib().trigonometry(), p.lib().arrayUtil()));

        GENERIC_IDFT_EXECUTOR = new ExecutorType<>(
                "GENERIC_IDFT_EXECUTOR", matsu.num.transform.fft.GenericIDFTExecutor.class,
                p -> new GenericIDFTExecutor(p.lib().trigonometry(), p.lib().arrayUtil()));
    }
}
