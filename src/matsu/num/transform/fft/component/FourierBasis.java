/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.component;

/**
 * 離散Fourier変換 (DFT) と逆変換 (IDFT) の基底関数.
 * 
 * <p>
 * DFTの基底関数である,
 * { 1, exp[-i(2&pi;/<i>N</i>)], exp[-i(4&pi;/<i>N</i>)], ... }
 * を扱うためのクラスである(<i>N</i>は標本サイズ). <br>
 * また, 同時にIDFTに関する基底:
 * { 1, exp[i(2&pi;/<i>N</i>)], exp[i(4&pi;/<i>N</i>)], ... }
 * も提供する.
 * </p>
 * 
 * <p>
 * 公開された全ての振る舞いはスレッドセーフである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class FourierBasis {

    private final int sequenceSize;
    private final ComplexNumber[] values;

    /**
     * 
     */
    FourierBasis(int sequenceNumber, ComplexNumber[] values, FourierType type) {

        this.sequenceSize = sequenceNumber;
        this.values = values;

        assert this.sequenceSize >= 1;
        assert this.values.length == this.sequenceSize;

    }

    /**
     * @return sequenceSize
     */
    public final int sequenceSize() {
        return this.sequenceSize;
    }

    /**
     * @param index index
     * @return 複素数
     * @throws ArrayIndexOutOfBoundsException indexが0以上N-1以下でない場合
     */
    public final ComplexNumber valueAt(int index) {
        return this.values[index];
    }

    /**
     * @return 複素数
     */
    public final ComplexNumber[] toArray() {
        return this.values.clone();
    }

}
