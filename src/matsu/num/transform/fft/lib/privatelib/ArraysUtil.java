/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.lib.privatelib;

/**
 * <p>
 * このモジュール内での処理における, 配列の計算を扱う. <br>
 * 外部のモジュールからこのインターフェースのメソッドを呼ぶことは想定されていない.
 * </p>
 *
 * @author Matsuura Y.
 * @version 20.0
 */
public interface ArraysUtil {

    /**
     * <p>
     * 最大値ノルムを計算する:
     * ||<b>v</b>||<sub>&infin;</sub>.
     * </p>
     *
     * <p>
     * <b>v</b> のサイズが0の場合, 0が返る.
     * </p>
     *
     * @param vector ベクトル <b>v</b>
     * @return 最大ノルム ||<b>v</b>||<sub>&infin;</sub>
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract double normMax(double[] vector);

}
