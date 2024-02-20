/**
 * 2024.2.17
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能なDST-2.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[])}
 * のreject条件は,
 * {@link DST2Executor} と同等である.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface GenericDST2Executor extends DST2Executor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericDST2Executor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] data);
}
