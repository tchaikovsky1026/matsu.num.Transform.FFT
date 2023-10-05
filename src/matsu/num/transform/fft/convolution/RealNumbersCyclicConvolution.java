/**
 * 2023.9.29
 */
package matsu.num.transform.fft.convolution;

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
 * ただし, <i>i</i> &gt; <i>j</i>のとき,
 * <i>g</i><sub><i>j</i> - <i>i</i></sub> =
 * <i>g</i><sub><i>N</i> + <i>j</i> - <i>i</i></sub> とする.
 * </p>
 * 
 * <p>
 * {@linkplain #apply(double[], double[])} メソッドにより変換を実行する. <br>
 * このとき, 出力の長さは入力の長さと必ず同一値になる. <br>
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
 * <li>例: 入力データの長さが大きすぎて, アルゴリズムが対応できない. </li>
 * <li>例: 入力データの長さは2の累乗でなければならない. </li>
 * </ul>
 * 
 * <p>
 * このインターフェースのサブタイプはメソッドの振る舞いに関わる変更可能な内部属性を持たず,
 * 全てのインスタンスメソッドは関数的でスレッドセーフである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.8
 */
public interface RealNumbersCyclicConvolution {

    /**
     * 与えた2系列の実数列に対し, 巡回畳み込みを実行する.
     * 
     * @param f 順方向に走査する実数列 f
     * @param g 逆方向に走査する実数列 g
     * @return 巡回畳み込みの結果
     * @throws IllegalArgumentException 引数の長さが整合しない場合, 引数の長さが0の場合, サブタイプで記載される条件の場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public abstract double[] apply(double[] f, double[] g);

}
