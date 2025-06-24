/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.6.20
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.LinearTransform;

/**
 * タイプ2の離散sine変換 (DST-2) を扱う.
 * 
 * <p>
 * DST-2は次式で与えられる. <br>
 * 標本サイズを <i>N</i> とし, <i>k</i> = 0, ... , <i>N</i> - 1 として, <br>
 * <i>X</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup><i>N</i> -
 * 1</sup>
 * <i>x</i><sub><i>j</i></sub>
 * sin[&pi; (<i>j</i> + (1/2)) (<i>k</i> + 1) / <i>N</i>].
 * </p>
 * 
 * <p>
 * このインターフェースでは,
 * {@link #accepts(double[])}
 * のreject条件は,
 * {@link LinearTransform}
 * に対して次が追加される.
 * </p>
 * 
 * <ul>
 * <li>データサイズが {@link #MAX_DATA_SIZE} を超過する場合</li>
 * </ul>
 *
 * 
 * <hr>
 * <h2>技術的補足</h2>
 * 
 * <p>
 * DST-2の逆変換はDST-3である. <br>
 * <i>x</i><sub>0</sub>, ... , <i>x</i><sub><i>N</i> - 1</sub> をDST-2変換した結果を
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub>, <br>
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub> をDST-3変換した結果を
 * <i>x</i>'<sub>0</sub>, ... , <i>x</i>'<sub><i>N</i> - 1</sub> とすると, <br>
 * <i>x</i>'<sub><i>j</i></sub> = (<i>N</i> / 2) <i>x</i><sub><i>j</i></sub>
 * <br>
 * が成立する.
 * (see {@link DST3Executor})
 * </p>
 * 
 * <p>
 * DST-2は離散Fourier変換 (DFT) を用いて表記可能である. <br>
 * DFTを行うための標本サイズ 2<i>N</i> の実数列 <i>a</i><sub><i>j</i></sub>
 * (<i>j</i> = 0, ... , 2<i>N</i> - 1) を用意し, <br>
 * 
 * <i>a</i><sub>0</sub> = <i>x</i><sub>0</sub>, ... ,
 * <i>a</i><sub><i>N</i> - 1</sub> = <i>x</i><sub><i>N</i> - 1</sub>, <br>
 * 
 * <i>a</i><sub><i>N</i></sub> = -<i>x</i><sub><i>N</i> - 1</sub>, ... ,
 * <i>a</i><sub>2<i>N</i> - 1</sub> = -<i>x</i><sub>0</sub> <br>
 * 
 * と定める.
 * <i>a</i><sub><i>j</i></sub> に対しDFTを行った結果を <i>A</i><sub><i>k</i></sub> とする.
 * すなわち, <i>k</i> = 0, ... , 2<i>N</i> - 1として, <br>
 * 
 * <i>A</i><sub><i>k</i></sub> =
 * &Sigma;<sub><i>j</i> = 0</sub><sup>2<i>N</i> - 1</sup>
 * <i>a</i><sub><i>j</i></sub> exp[-i (2&pi;<i>j</i><i>k</i>) / (2<i>N</i>)]
 * <br>
 * 
 * と定める.
 * このとき, <i>k</i> = 0, ... , <i>N</i> - 1 に対して, <br>
 * 
 * <i>X</i><sub><i>k</i></sub> =
 * (1/2) exp[-i&pi; (<i>k</i> + 1) / (2<i>N</i>)]
 * <i>A</i><sub><i>k</i> + 1</sub> <br>
 * 
 * が成立する.
 * </p>
 * 
 * @implSpec
 *               このインターフェースをモジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 */
public interface DST2Executor extends LinearTransform {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>27</sup>
     */
    public static final int MAX_DATA_SIZE = 0x1000_0000 / 2;

}
