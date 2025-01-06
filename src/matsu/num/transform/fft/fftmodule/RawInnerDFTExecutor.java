/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.fftmodule;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;

/**
 * 直接的な離散Fourier変換/逆変換を扱う.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class RawInnerDFTExecutor implements InnerDFTExecutor {

    /**
     * このクラスのインスタンスを構築する.
     */
    public RawInnerDFTExecutor() {
        super();
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer) {

        //ここでbasisComputerのサイズが整合しないことに対する例外をスロー
        FourierBasis basis = basisComputer.getBasis(data.length);

        int N = data.length;

        ComplexNumber[] A = new ComplexNumber[N];

        for (int k = 0; k < N; k++) {
            ComplexNumber[] w = new ComplexNumber[N];

            int jk_mod_N = 0;
            for (int j = 0; j < N; j++) {
                w[j] = basis.valueAt(jk_mod_N);

                jk_mod_N += k;
                if (jk_mod_N >= N) {
                    jk_mod_N -= N;
                }
            }

            A[k] = ComplexNumber.sumProduct(data, w);
        }

        return A;
    }
}
