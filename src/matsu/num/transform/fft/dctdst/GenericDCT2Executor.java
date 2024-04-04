/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能なDCT-2.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[])}
 * のreject条件は,
 * {@link DCT2Executor} と同等である.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public interface GenericDCT2Executor extends DCT2Executor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericDCT2Executor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] data);
}
