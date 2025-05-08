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

import matsu.num.transform.fft.BiLinearTransform;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 実数列の巡回畳み込みを扱う.
 * 
 * <p>
 * 標本サイズを <i>N</i> の2系列のデータ
 * {<i>f</i><sub>0</sub>, ... , <i>f</i><sub><i>N</i> - 1</sub>},
 * {<i>g</i><sub>0</sub>, ... , <i>g</i><sub><i>N</i> - 1</sub>}
 * を用意する. <br>
 * <i>f</i> と <i>g</i> の巡回畳み込み <i>f</i> &lowast; <i>g</i> は,
 * <i>j</i> = 0, ... , <i>N</i> - 1 として, <br>
 * (<i>f</i> &lowast; <i>g</i>)<sub><i>j</i></sub> =
 * &Sigma;<sub><i>i</i> = 0</sub><sup><i>N</i> - 1</sup>
 * <i>f</i><sub><i>i</i></sub> <i>g</i><sub><i>j</i> - <i>i</i></sub> <br>
 * で与えられる. <br>
 * ただし, <i>i</i> &gt; <i>j</i> のとき,
 * <i>g</i><sub><i>j</i> - <i>i</i></sub> =
 * <i>g</i><sub><i>N</i> + <i>j</i> - <i>i</i></sub> とする.
 * </p>
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[], double[])}
 * のreject条件は,
 * {@link BiLinearTransform}
 * に対して次が追加される.
 * </p>
 * 
 * <ul>
 * <li>データサイズが {@link #MAX_DATA_SIZE} を超過する場合</li>
 * </ul>
 * 
 * @implSpec
 *               このインターフェースをモジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 */
public interface CyclicConvolutionExecutor extends BiLinearTransform {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = 0x1000_0000;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link CyclicConvolutionExecutor} ではデータサイズが
     * {@link #MAX_DATA_SIZE}
     * を超過する場合にrejectされる.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(double[] f, double[] g);
}
