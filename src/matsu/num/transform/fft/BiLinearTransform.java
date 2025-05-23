/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.5.8
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 2系列の実数列から1系列への, 双線形な変換を扱う. <br>
 * 例えば, 巡回畳み込みである.
 * 
 * <p>
 * {@link #apply(double[], double[])} メソッドにより変換を実行する. <br>
 * 入力の長さは等しい必要があり, 出力の長さは入力と等しい. <br>
 * 実数列に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
 * </p>
 * 
 * <p>
 * 入力データのサイズがこのインスタンスに対応可能かどうかを判定するために,
 * {@link #accepts(double[], double[])} が用意されている. <br>
 * 仕様上, 2系列のサイズが整合しない場合, サイズが1以上でない場合はrejectされるが,
 * その他のreject条件は具象クラスにゆだねられる. <br>
 * rejectされる条件と, {@link #apply(double[], double[])} が例外をスローする条件は等価である.
 * </p>
 * 
 * <p>
 * 追加のreject条件は例えば次のようなものが挙げられる.
 * </p>
 * 
 * <ul>
 * <li>例: 入力の長さが大きすぎて, アルゴリズムが対応できない.</li>
 * <li>例: 入力の長さは2の累乗でなければならない.</li>
 * </ul>
 * 
 * @implSpec
 *               このインターフェースをモジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 */
public interface BiLinearTransform extends FFTModuleExecutor {

    /**
     * <p>
     * 与えた2系列の実数列の構造(サイズ)が, 変換に対応しているかどうかを判定する. <br>
     * 2系列のサイズが整合しない場合, サイズが1以上でない場合は必ずrejectされることが保証される. <br>
     * 追加のreject条件は, サブタイプにゆだねられる.
     * </p>
     * 
     * @param f 実数列f
     * @param g 実数列g
     * @return 判定結果
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract StructureAcceptance accepts(double[] f, double[] g);

    /**
     * 与えた2系列の実数列に対し, 変換を実行する.
     * 
     * @param f 実数列f
     * @param g 実数列g
     * @return 変換後の実数列(入力とサイズが等しい)
     * @throws IllegalArgumentException 引数がacceptされない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract double[] apply(double[] f, double[] g);

}
