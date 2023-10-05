/**
 * 2023.9.29
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 離散Fourier変換 (DFT) を扱う. 
 * 
 * <p>
 * DFTは次式で与えられる. <br>
 * 標本サイズを <i>N</i> とし, <i>k</i> = 0, ... , <i>N</i> - 1 として, <br>
 * <i>A</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup><i>N</i> - 1</sup>
 * <i>a</i><sub><i>j</i></sub> exp[-i(2&pi;<i>jk</i>)/<i>N</i>].
 * </p>
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
 * DFTの逆変換はIDFTである. <br>
 * <i>a</i><sub>0</sub>, ... , <i>a</i><sub><i>N</i> - 1</sub> をDFT変換した結果を
 * <i>A</i><sub>0</sub>, ... , <i>A</i><sub><i>N</i> - 1</sub>, <br>
 * <i>A</i><sub>0</sub>, ... , <i>A</i><sub><i>N</i> - 1</sub> をIDFT変換した結果を
 * <i>a</i>'<sub>0</sub>, ... , <i>a</i>'<sub><i>N</i> - 1</sub> とすると, <br>
 * <i>a</i>'<sub><i>j</i></sub> = <i>N</i> <i>a</i><sub><i>j</i></sub> <br>
 * が成立する.
 * (see {@linkplain IDFTExecutor})
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.7
 */
public interface DFTExecutor extends ComplexNumbersLinearBijectiveTransform {

    /**
     * {@inheritDoc } 
     * 
     * <p>
     * スローされる例外はスーパーインターフェースに従う.
     * </p>
     */
    @Override
    public abstract ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray);

}
