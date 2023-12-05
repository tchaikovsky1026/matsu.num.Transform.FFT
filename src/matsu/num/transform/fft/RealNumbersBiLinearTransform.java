/**
 * 2023.12.5
 */
package matsu.num.transform.fft;

/**
 * 2系列の実数列から1系列への, 双線形な変換を扱う. <br>
 * 例えば, 巡回畳み込みである.
 * 
 * <p>
 * {@linkplain #apply(double[], double[])} メソッドにより変換を実行する. <br>
 * 入力の長さは等しい必要があり, 出力の長さは入力と等しい. <br>
 * 実数列に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
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
 * <li>例: 入力データの長さが大きすぎて, アルゴリズムが対応できない.</li>
 * <li>例: 入力データの長さは2の累乗でなければならない.</li>
 * </ul>
 * 
 * <p>
 * このインターフェースのサブタイプはメソッドの振る舞いに関わる変更可能な内部属性を持たず,
 * 全てのインスタンスメソッドは関数的でスレッドセーフである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public interface RealNumbersBiLinearTransform {

    /**
     * 与えた2系列の実数列に対し, 変換を実行する.
     * 
     * @param f 実数列f
     * @param g 実数列g
     * @return 変換後の実数列(入力とサイズが等しい)
     * @throws IllegalArgumentException 引数の長さが整合しない場合, 引数の長さが0の場合,
     *             サブタイプで記載される条件に違反する場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract double[] apply(double[] f, double[] g);

}
