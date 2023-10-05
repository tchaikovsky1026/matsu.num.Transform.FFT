/**
 * 2023.9.29
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 全単射の複素数列の変換を扱う. <br>
 * 例えば離散Fourier変換/逆変換である.
 * 
 * <p>
 * このインターフェースの引数(入力)としての複素数列は, {@linkplain ComplexNumberArrayDTO}により表現する. <br>
 * 出力の長さは入力の長さと必ず同一になる.
 * </p>
 * 
 * <p>
 * 入力に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
 * </p>
 * 
 * <p>
 * 入力データは長さが1以上必要であることが共通だが, その他の条件は具象クラスにゆだねられる. <br>
 * 変換が不可能な場合は {@linkplain IllegalArgumentException} のサブクラスをスローする. <br>
 * 例外を投げる条件は具象クラスあるいは具象クラスのファクトリクラスの説明文に記載する.
 * </p>
 * 
 * <p>
 * 追加で課せられる条件とは例えば次のようなものが挙げられる.
 * </p>
 * 
 * <ul>
 * <li>例: 入力の長さが大きすぎて, アルゴリズムが対応できない. </li>
 * <li>例: 入力の長さは2の累乗でなければならない. </li>
 * </ul>
 * 
 * <p>
 * このインターフェースのサブタイプはメソッドの振る舞いに関わる変更可能な内部属性を持たず,
 * 全てのインスタンスメソッドは関数的でスレッドセーフである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.6
 * @deprecated このモジュール内では線形変換しか扱わないので, {@link ComplexNumbersLinearBijectiveTransform}を使用するように. version 13以降で削除
 * @see ComplexNumberArrayDTO
 * @see ComplexNumbersLinearBijectiveTransform
 */
@Deprecated(forRemoval = true)
public interface ComplexNumbersBijectiveTransform {

    /**
     * 与えた複素数列に対し, 変換を実行する. <br>
     * 引数, 戻り値の複素数列の表現方法はインターフェース説明文を参照.
     * 
     * @param complexNumberArray 複素数列
     * @return 変換後の複素数列
     * @throws IllegalArgumentException 引数の長さが0の場合, サブタイプで記載される条件の場合,
     * 実部と虚部の配列長さが一致しない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray);

    /**
     * 与えた実数列に対し, 変換を実行する.
     * 
     * <p>
     * このメソッドは, {@linkplain #apply(ComplexNumberArrayDTO)}に対して虚部を0とした複素数列を与えるのと同等の振る舞いを提供する.
     * </p>
     * 
     * @param realNumberData 実数列
     * @return 変換後の複素数列
     * @throws IllegalArgumentException 引数の長さが0の場合, サブタイプで記載される条件の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public default ComplexNumberArrayDTO applyReal(double[] realNumberData) {

        ComplexNumberArrayDTO complexNumberArray = ComplexNumberArrayDTO.zeroFilledOf(realNumberData.length);
        System.arraycopy(realNumberData, 0, complexNumberArray.realPart, 0, realNumberData.length);
        return this.apply(complexNumberArray);
    }
}
