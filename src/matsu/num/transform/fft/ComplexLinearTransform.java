/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.26
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.component.ComplexLinearByScalingStability;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 複素数列の線形変換を扱う. <br>
 * 例えば, 離散Fourier変換/逆変換である.
 * 
 * <p>
 * {@link #apply(ComplexNumberArrayDTO)} メソッドにより変換を実行する. <br>
 * このとき, 出力の長さは入力の長さと等しい. <br>
 * このインターフェースの引数(入力)としての複素数列は, {@link ComplexNumberArrayDTO}により表現する.
 * </p>
 * 
 * <p>
 * 入力に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
 * </p>
 * 
 * <p>
 * 入力データのサイズがこのインスタンスに対応可能かどうかを判定するために,
 * {@link #accepts(ComplexNumberArrayDTO)} が用意されている. <br>
 * 仕様上, サイズが1以上でない場合はrejectされるが, その他のreject条件は具象クラスにゆだねられる. <br>
 * rejectされる条件と, {@link #apply(ComplexNumberArrayDTO)} が例外をスローする条件は等価である.
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
 * <p>
 * このインターフェースのサブタイプは実質的にイミュータブルであり,
 * すべてのメソッドは副作用無し,
 * スレッドセーフ, 参照透過であることが保証される.
 * </p>
 * 
 * 
 * <p>
 * <i><u>
 * このインターフェース, およびサブインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * モジュールの外部で実装することを禁止する. <br>
 * バージョン間で継承階層に互換性があるとは限らないので,
 * シールインターフェースであるが {@code instanceof} 比較による分岐は危険である.
 * </u></i>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 * @see ComplexNumberArrayDTO
 */
public sealed interface ComplexLinearTransform permits DFTExecutor, IDFTExecutor, ComplexLinearByScalingStability {

    /**
     * <p>
     * 与えた複素数列の構造(サイズ)が, 変換に対応しているかどうかを判定する. <br>
     * サイズが1以上でない場合は必ずrejectされることが保証される. <br>
     * 追加のreject条件は, サブタイプにゆだねられる.
     * </p>
     * 
     * @param complexNumberArray 複素数列
     * @return 判定結果
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract StructureAcceptance accepts(ComplexNumberArrayDTO complexNumberArray);

    /**
     * <p>
     * 与えた実数列の構造(サイズ)が, 変換に対応しているかどうかを判定する. <br>
     * 契約は {@link #accepts(ComplexNumberArrayDTO)} に準拠する.
     * </p>
     * 
     * @param realNumberData 実数列
     * @return 判定結果
     */
    public abstract StructureAcceptance acceptsReal(double[] realNumberData);

    /**
     * 与えた複素数列に対し, 変換を実行する.
     * 
     * @param complexNumberArray 複素数列
     * @return 変換後の複素数列(入力とサイズが等しい)
     * @throws IllegalArgumentException 引数がacceptされない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray);

    /**
     * <p>
     * 与えた実数列に対し, 変換を実行する. <br>
     * このメソッドは,
     * {@link #apply(ComplexNumberArrayDTO)}
     * に対して虚部を0とした複素数列を与えるのと同等の振る舞いを提供する. <br>
     * 契約も
     * {@link #apply(ComplexNumberArrayDTO)}
     * に準拠する.
     * </p>
     * 
     * @param realNumberData 実数列
     * @return 変換後の複素数列(入力とサイズが等しい)
     */
    public abstract ComplexNumberArrayDTO applyReal(double[] realNumberData);
}
