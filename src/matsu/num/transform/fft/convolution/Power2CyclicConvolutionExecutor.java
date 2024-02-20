/**
 * 2024.2.17
 */
package matsu.num.transform.fft.convolution;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 2の累乗のデータサイズに特化した実数列の巡回畳み込み.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[], double[])}
 * のreject条件は,
 * {@link CyclicConvolutionExecutor}
 * に対して次が追加される.
 * </p>
 * 
 * <ul>
 * <li>データサイズが2の累乗でない場合</li>
 * </ul>
 * 
 * <p>
 * このインターフェースのサブタイプでは, これ以上reject条件を緩めては (accept条件を強めては) いけない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface Power2CyclicConvolutionExecutor extends CyclicConvolutionExecutor {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link Power2CyclicConvolutionExecutor}
     * ではデータサイズが2の累乗でない場合にrejectされる. <br>
     * {@link Power2CyclicConvolutionExecutor}
     * でaccept条件とreject条件が共に確定 (固定) される.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] f, double[] g);
}
