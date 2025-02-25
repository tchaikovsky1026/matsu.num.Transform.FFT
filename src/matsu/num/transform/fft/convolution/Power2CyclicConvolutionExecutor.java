/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.26
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
 * に対して次が追加される. <br>
 * このインターフェースのサブタイプではこれ以上reject条件を緩めて (accept条件を強めて) いないことを保証する.
 * </p>
 * 
 * <ul>
 * <li>データサイズが2の累乗でない場合</li>
 * </ul>
 * 
 * <p>
 * <i><u>
 * このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * モジュールの外部で実装することを禁止する.
 * </u></i>
 * </p>
 * 
 * @author Matsuura Y.
 */
public non-sealed interface Power2CyclicConvolutionExecutor extends CyclicConvolutionExecutor {

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
