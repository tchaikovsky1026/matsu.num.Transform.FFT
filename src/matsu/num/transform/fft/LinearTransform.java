/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.1
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 実数列の線形変換を扱う. <br>
 * 例えば離散cosine変換や離散sine変換である.
 * 
 * <p>
 * {@link #apply(double[])} メソッドにより変換を実行する. <br>
 * このとき, 出力の長さは入力の長さと等しい. <br>
 * 実数列に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
 * </p>
 * 
 * <p>
 * 入力データのサイズがこのインスタンスに対応可能かどうかを判定するために,
 * {@link #accepts(double[])} が用意されている. <br>
 * 仕様上, サイズが1以上でない場合はrejectされるが,
 * その他のreject条件は具象クラスにゆだねられる. <br>
 * rejectされる条件と, {@link #apply(double[])} が例外をスローする条件は等価である.
 * </p>
 * 
 * <p>
 * 追加のreject条件は例えば次のようなものが挙げられる.
 * </p>
 * 
 * <ul>
 * <li>例: 入力データの長さが大きすぎて, アルゴリズムが対応できない</li>
 * <li>例: 入力データの長さは2の累乗でなければならない</li>
 * </ul>
 * 
 * <p>
 * このインターフェースのサブタイプは実質的にイミュータブルであり,
 * すべてのメソッドは副作用無し,
 * スレッドセーフ, 参照透過であることが保証される.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
public interface LinearTransform {

    /**
     * <p>
     * 与えた実数列の構造(サイズ)が, 変換に対応しているかどうかを判定する. <br>
     * サイズが1以上でない場合は必ずrejectされることが保証される. <br>
     * 追加のreject条件は, サブタイプにゆだねられる.
     * </p>
     * 
     * @param data 実数列
     * @return 判定結果
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract StructureAcceptance accepts(double[] data);

    /**
     * 与えた実数列に対し, 線形変換を実行する.
     * 
     * @param data 実数列
     * @return 変換後の実数列(入力とサイズが等しい)
     * @throws IllegalArgumentException 引数がacceptされない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract double[] apply(double[] data);

}
