/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.fftmodule;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;

/**
 * 内部的なDFT/IDFTの実行を扱う. <br>
 * パッケージプライベート予定.
 * 
 * <p>
 * {@link #compute(ComplexNumber[], FourierBasisComputer)}メソッドでは,
 * 対応不可なdataを与えられた場合には,
 * {@link IllegalArgumentException}のサブクラスを追加でスローしても良い.
 * </p>
 * 
 * <p>
 * DFTでは, <i>k</i> = 0,...,<i>N</i> - 1として<br>
 * <i>A</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup><i>N</i> -
 * 1</sup>
 * <i>a</i><sub><i>j</i></sub>
 * exp[-i(2&pi;<i>jk</i>/<i>N</i>)], <br>
 * IDFTでは, <i>j</i> = 0,...,<i>N</i> - 1として<br>
 * <i>a</i><sub><i>j</i></sub> = &Sigma;<sub><i>k</i> = 0</sub><sup><i>N</i> -
 * 1</sup>
 * <i>A</i><sub><i>k</i></sub>
 * exp[i(2&pi;<i>jk</i>/<i>N</i>)] <br>
 * である. <br>
 * このインターフェースが扱う変換は, DFTとIDFTが逆関数になっておらず,
 * 両方を作用させると全体が<i>N</i>倍になる.
 * </p>
 * 
 * @author Matsuura Y.
 */
interface InnerDFTExecutor {

    /**
     * 与えられたデータに対してDFT/IDFTをcomputeする.
     * 
     * @param data データ
     * @param basisComputer Fourier基底生成器
     * @return DFT/IDFTの結果
     * @throws IllegalArgumentException サイズが0の場合, サイズが大きすぎる場合,
     *             基底生成器がdata.lengthに対応していない場合,
     *             その他クラスの説明文に定義された条件の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer);

}
