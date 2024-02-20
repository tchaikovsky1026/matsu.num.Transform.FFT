/**
 * 2024.2.17
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 離散Fourier変換 (DFT) を扱う.
 * 
 * <p>
 * DFTは次式で与えられる. <br>
 * 標本サイズを <i>N</i> とし, <i>k</i> = 0, ... , <i>N</i> - 1 として, <br>
 * <i>A</i><sub><i>k</i></sub> = &Sigma;<sub><i>j</i> = 0</sub><sup><i>N</i> -
 * 1</sup>
 * <i>a</i><sub><i>j</i></sub> exp[-i(2&pi;<i>jk</i>)/<i>N</i>].
 * </p>
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(ComplexNumberArrayDTO)}
 * のreject条件は,
 * {@link ComplexLinearTransform}
 * に対して次が追加される.
 * </p>
 * 
 * <ul>
 * <li>データサイズが {@link #MAX_DATA_SIZE} を超過する場合.</li>
 * </ul>
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
 * (see {@link IDFTExecutor})
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface DFTExecutor extends ComplexLinearTransform {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = 0x1000_0000;

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link DFTExecutor} ではデータサイズが {@link #MAX_DATA_SIZE}
     * を超過する場合にrejectされる.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public abstract StructureAcceptance accepts(ComplexNumberArrayDTO complexNumberArray);

}
