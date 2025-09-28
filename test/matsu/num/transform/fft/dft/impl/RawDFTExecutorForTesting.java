/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.dft.impl;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;

/**
 * テスト用の, Rawな方法によるDFT/IDFT.
 * 
 * @author Matsuura Y.
 */
final class RawDFTExecutorForTesting {

    /**
     * このクラスのインスタンスを構築する.
     */
    RawDFTExecutorForTesting() {
        super();
    }

    /**
     * 与えられたデータに対してDFT/IDFTをcomputeする.
     * 
     * @param data データ
     * @param basisComputer Fourier基底生成器
     * @return DFT/IDFTの結果
     * @throws IllegalArgumentException サイズが0の場合, サイズが大きすぎる場合,
     *             基底生成器がdata.lengthに対応していない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
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
