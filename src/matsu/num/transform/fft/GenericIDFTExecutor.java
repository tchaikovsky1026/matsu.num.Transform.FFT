/**
 * 2024.2.17
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能なIDFT.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(ComplexNumberArrayDTO)}
 * のreject条件は, 
 * {@link IDFTExecutor} と同等である.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface GenericIDFTExecutor extends IDFTExecutor{

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericIDFTExecutor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(ComplexNumberArrayDTO complexNumberArray);
}
