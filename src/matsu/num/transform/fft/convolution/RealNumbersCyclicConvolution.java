/**
 * 2023.12.4
 */
package matsu.num.transform.fft.convolution;

import matsu.num.transform.fft.RealNumbersBiLinearTransform;

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
 * {@linkplain #apply(double[], double[])} メソッドにより変換を実行する. <br>
 * このとき, 出力の長さは入力の長さと必ず同一値になる. <br>
 * 実数列に無限大や非数 (NaN) を含む場合, 戻り値は全て {@code Double.NaN} となる.
 * </p>
 * 
 * <p>
 * 細かい規約はスーパーインターフェースに従う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public interface RealNumbersCyclicConvolution extends RealNumbersBiLinearTransform {

    /**
     * {@inheritDoc }
     * 
     * <p>
     * スローされる例外は, スーパーインターフェースに従う.
     * </p>
     * 
     * @param f 順方向に走査する実数列 f
     * @param g 逆方向に走査する実数列 g
     */
    @Override
    public abstract double[] apply(double[] f, double[] g);

}
