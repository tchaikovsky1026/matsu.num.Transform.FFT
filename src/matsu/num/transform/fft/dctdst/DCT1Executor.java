/**
 * 2023.9.26
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;

/**
 * タイプ1の離散cosine変換 (DCT-1) を扱う. <br>
 * DCT-1は標本サイズ (データサイズ) が2以上の実数列に適用される.
 * 
 * <p>
 * DCT-1は次式で与えられる. <br>
 * 標本サイズを <i>N</i> とし, <i>k</i> = 0, ... , <i>N</i> - 1 として, <br>
 * <i>X</i><sub><i>k</i></sub> = (1/2)<i>x</i><sub>0</sub> +
 * &Sigma;<sub><i>j</i> = 1</sub><sup><i>N</i> - 2</sup>
 * <i>x</i><sub><i>j</i></sub> cos[&pi;<i>j</i><i>k</i>/(<i>N</i> - 1)] +
 * (1/2)(-1)<sup><i>k</i></sup><i>x</i><sub><i>N</i> - 1</sub>.
 * </p>
 * 
 * 
 * <p>
 * 細かい規約はスーパーインターフェースに従うが, 加えて次の例外を
 * {@linkplain DCT1Executor#apply(double[])} メソッドでスローする.
 * </p>
 * 
 * <ul>
 * <li>{@code IllegalArgumentException 入力データの長さが} {@code 1以下の場合} </li>
 * </ul>
 * 
 * 
 * <p>
 * <u><i>技術的補足</i></u>
 * </p>
 * 
 * <p>
 * DCT-1の逆変換はDCT-1自身である. <br>
 * <i>x</i><sub>0</sub>, ... , <i>x</i><sub><i>N</i> - 1</sub> をDCT-1変換した結果を
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub>, <br>
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub> をDCT-1変換した結果を
 * <i>x</i>'<sub>0</sub>, ... , <i>x</i>'<sub><i>N</i> - 1</sub> とすると, <br>
 * <i>x</i>'<sub><i>j</i></sub> = [(<i>N</i> - 1)/2] <i>x</i><sub><i>j</i></sub> <br>
 * が成立する.
 * </p>
 * 
 * <p>
 * DCT-1は離散Fourier変換 (DFT) を用いて表記可能である. <br>
 * DFTを行うための標本サイズ 2<i>N</i> - 2 の実数列 <i>a</i><sub><i>j</i></sub> (<i>j</i> = 0, ... , 2<i>N</i> - 3) を用意し, <br>
 * 
 * <i>a</i><sub>0</sub> = <i>x</i><sub>0</sub>, <br>
 * 
 * <i>a</i><sub>1</sub> = <i>x</i><sub>1</sub>, ... ,
 * <i>a</i><sub><i>N</i> - 2</sub> = <i>x</i><sub><i>N</i> - 2</sub>, <br>
 * 
 * <i>a</i><sub><i>N</i> - 1</sub> = <i>x</i><sub><i>N</i> - 1</sub>, <br>
 * 
 * <i>a</i><sub><i>N</i></sub> = <i>x</i><sub><i>N</i> - 2</sub>, ... ,
 * <i>a</i><sub>2<i>N</i> - 3</sub> = <i>x</i><sub>1</sub> <br>
 * 
 * と定める.
 * <i>a</i><sub><i>j</i></sub> に対しDFTを行った結果を <i>A</i><sub><i>k</i></sub> とする.
 * すなわち, <i>k</i> = 0, ... , 2<i>N</i> - 3として, <br>
 * 
 * <i>A</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup>2<i>N</i> - 3</sup>
 * <i>a</i><sub><i>j</i></sub> exp[-i(2&pi;<i>j</i><i>k</i>)/(2<i>N</i> - 2)] <br>
 * 
 * と定める.
 * このとき, <i>k</i> = 0, ... , <i>N</i> - 1 に対して, <br>
 * 
 * <i>X</i><sub><i>k</i></sub> = (1/2)<i>A</i><sub><i>k</i></sub> <br>
 * 
 * が成立する.
 * </p>
 * 
 * 
 * @author Matsuura Y.
 * @version 12.4
 */
public interface DCT1Executor extends RealNumbersLinearBijectiveTransform {

    /**
     * {@inheritDoc } 
     * 
     * <p>
     * スローされる例外はスーパーインターフェースに加えて, このインターフェースで条件が追加される. 
     * </p>
     * 
     * @throws IllegalArgumentException 引数の長さが1以下の場合
     */
    @Override
    public abstract double[] apply(double[] data);
}
