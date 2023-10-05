/**
 * 2023.9.26
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.transform.fft.RealNumbersLinearBijectiveTransform;

/**
 * タイプ3の離散cosine変換 (DCT-3) を扱う. 
 * 
 * <p>
 * DCT-3は次式で与えられる. <br>
 * 標本サイズを <i>N</i> とし, <i>k</i> = 0, ... , <i>N</i> - 1 として, <br>
 * <i>X</i><sub><i>k</i></sub> = (1/2)<i>x</i><sub>0</sub> +
 * &Sigma;<sub><i>j</i> = 1</sub><sup><i>N</i> - 1</sup>
 * <i>x</i><sub><i>j</i></sub> cos[&pi;<i>j</i>(<i>k</i> + (1/2))/<i>N</i>].
 * </p>
 * 
 * 
 * <p>
 * 細かい規約はスーパーインターフェースに従う.
 * </p>
 * 
 * <p>
 * <u><i>技術的補足</i></u>
 * </p>
 * 
 * <p>
 * DCT-3の逆変換はDCT-2である. <br>
 * <i>x</i><sub>0</sub>, ... , <i>x</i><sub><i>N</i> - 1</sub> をDCT-3変換した結果を
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub>, <br>
 * <i>X</i><sub>0</sub>, ... , <i>X</i><sub><i>N</i> - 1</sub> をDCT-2変換した結果を
 * <i>x</i>'<sub>0</sub>, ... , <i>x</i>'<sub><i>N</i> - 1</sub> とすると, <br>
 * <i>x</i>'<sub><i>j</i></sub> = (<i>N</i>/2) <i>x</i><sub><i>j</i></sub> <br>
 * が成立する.
 * (see {@linkplain DCT2Executor})
 * </p>
 * 
 * <p>
 * DCT-3は離散Fourier変換 (DFT) を用いて表記可能である. <br>
 * DFTを行うための標本サイズ 2<i>N</i> の実数列 <i>a</i><sub><i>j</i></sub> (<i>j</i> = 0, ... , 2<i>N</i> - 1) を用意し, <br>
 * 
 * <i>a</i><sub>0</sub> = <i>x</i><sub>0</sub>, <br>
 * 
 * <i>a</i><sub>1</sub> = <i>x</i><sub>1</sub>
 * exp[-i&pi;/(2<i>N</i>)], ... ,
 * <i>a</i><sub><i>N</i> - 1</sub> = <i>x</i><sub><i>N</i> - 1</sub>
 * exp[-i&pi;(<i>N</i> - 1)/(2<i>N</i>)], <br>
 * 
 * <i>a</i><sub><i>N</i></sub> = 0, <br>
 * 
 * <i>a</i><sub><i>N</i> + 1</sub> = <i>x</i><sub><i>N</i> - 1</sub>
 * exp[i&pi;(<i>N</i> - 1)/(2<i>N</i>)], ... ,
 * <i>a</i><sub>2<i>N</i> - 1</sub> = <i>x</i><sub>1</sub>
 * exp[i&pi;/(2<i>N</i>)]<br>
 * 
 * と定める.
 * <i>a</i><sub><i>j</i></sub> に対しDFTを行った結果を <i>A</i><sub><i>k</i></sub> とする.
 * すなわち, <i>k</i> = 0, ... , 2<i>N</i> - 1として, <br>
 * 
 * <i>A</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup>2<i>N</i> - 1</sup>
 * <i>a</i><sub><i>j</i></sub> exp[-i(2&pi;<i>j</i><i>k</i>)/(2<i>N</i>)] <br>
 * 
 * と定める.
 * このとき, <i>k</i> = 0, ... , <i>N</i> - 1 に対して, <br>
 * 
 * <i>X</i><sub><i>k</i></sub> = (1/2) <i>A</i><sub><i>k</i></sub> <br>
 * 
 * が成立する.
 * </p>
 * 
 * 
 * @author Matsuura Y.
 * @version 12.4
 */
public interface DCT3Executor extends RealNumbersLinearBijectiveTransform {

    /**
     * {@inheritDoc } 
     * 
     * <p>
     * スローされる例外は, スーパーインターフェースに従う. 
     * </p>
     */
    @Override
    public abstract double[] apply(double[] data);
}
