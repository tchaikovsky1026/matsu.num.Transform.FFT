/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft;

import org.junit.Ignore;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 実数列, 複素数列のダミーデータを作成するクラス.
 */
@Ignore
public final class NumberArrayDataCreator {

    private NumberArrayDataCreator() {
        //インスタンス化不可
        throw new AssertionError();
    }

    @SuppressWarnings(value = "exports")
    public static ComplexNumber[] createComplexArrayData(int size) {
        ComplexNumber[] data = new ComplexNumber[size];
        for (int i = 0; i < size; i++) {
            data[i] = ComplexNumber.of(i, Math.min(i, size - i));
        }
        return data;
    }

    public static ComplexNumberArrayDTO createComplexArrayDTO(int size) {
        ComplexNumberArrayDTO out = ComplexNumberArrayDTO.zeroFilledOf(size);
        for (int i = 0; i < size; i++) {
            out.realPart[i] = i;
            out.imaginaryPart[i] = Math.min(i, size - i);
        }
        return out;
    }

    public static double[] createArrayData(int size) {
        double[] data = new double[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }

        return data;
    }
}
