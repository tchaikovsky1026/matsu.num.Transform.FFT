/**
 * 2024.2.17
 */
package matsu.num.transform.fft.convolution;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 任意のデータサイズに適用可能な実数列の巡回畳み込み.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[], double[])}
 * のreject条件は,
 * {@link CyclicConvolutionExecutor} と同等である.
 * </p>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface GenericCyclicConvolutionExecutor extends CyclicConvolutionExecutor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link GenericCyclicConvolutionExecutor} でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] f, double[] g);
}
