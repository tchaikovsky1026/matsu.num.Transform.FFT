/**
 * 2024.2.17
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能なDST-1.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[])}
 * のreject条件は,
 * {@link DST1Executor} と同等である.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface GenericDST1Executor extends DST1Executor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericDST1Executor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] data);
}
