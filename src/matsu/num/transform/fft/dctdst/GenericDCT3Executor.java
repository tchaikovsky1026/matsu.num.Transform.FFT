/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.26
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能なDCT-3.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[])}
 * のreject条件は,
 * {@link DCT3Executor} と同等である. <br>
 * このインターフェースのサブタイプではこれ以上reject条件を緩めて (accept条件を強めて) いないことを保証する.
 * </p>
 * 
 * <p>
 * <i><u>
 * このインターフェース, およびサブインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * モジュールの外部で実装することを禁止する.
 * </u></i>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
public non-sealed interface GenericDCT3Executor extends DCT3Executor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericDCT3Executor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] data);
}
