/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.fftmodule;

import java.util.Objects;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.number.Power2Util;

/**
 * 2の累乗のデータサイズに特化した, 複素数列の巡回畳み込みを扱う.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class Power2CyclicConvolutionModule {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>30</sup>
     */
    public static final int MAX_SEQUENCE_SIZE = 0x4000_0000;

    private final InnerDFTExecutor power2FFT;
    private final FourierBasisComputer.Supplier computerSupplier;

    public Power2CyclicConvolutionModule(FourierBasisComputer.Supplier computerSupplier) {
        super();
        this.computerSupplier = Objects.requireNonNull(computerSupplier);
        this.power2FFT = new Power2InnerFFTExecutor();
    }

    /**
     * 巡回畳み込みを計算する.
     * 
     * @param f f
     * @param g g
     * @return 巡回畳み込みの結果
     * @throws IllegalArgumentException 引数の長さが一致しない場合, 長さが0の場合, 長さが大きすぎる場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ComplexNumber[] compute(ComplexNumber[] f, ComplexNumber[] g) {
        return new CyclicConvHelper(f, g, this.computerSupplier).compute();
    }

    private final class CyclicConvHelper {

        private final FourierBasisComputer dftComputer;
        private final FourierBasisComputer idftComputer;

        private final int length;

        private final ComplexNumber[] f;
        private final ComplexNumber[] g;

        /**
         * 巡回畳み込みを生成する. <br>
         * 2の累乗に対するDFTとIDFTの生成器キャッシュを渡す.
         * 
         * @param f f
         * @param g g
         * @param computerSupplier Fourier基底コンピュータのサプライヤ
         * @throws IllegalArgumentException 引数の長さが一致しない場合, 長さが2の累乗でない場合,
         *             長さが大きすぎる場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        CyclicConvHelper(ComplexNumber[] f, ComplexNumber[] g, FourierBasisComputer.Supplier computerSupplier) {
            if (f.length != g.length) {
                throw new IllegalArgumentException("長さが一致しない");
            }
            if (!Power2Util.isPowerOf2(f.length)) {
                throw new IllegalArgumentException("長さが2の累乗でない");
            }
            if (f.length > MAX_SEQUENCE_SIZE) {
                throw new IllegalArgumentException("長さが大きすぎる");
            }

            this.length = f.length;

            this.f = f;
            this.g = g;

            this.dftComputer = computerSupplier.covering(this.length, FourierType.DFT);
            this.idftComputer = computerSupplier.covering(this.length, FourierType.IDFT);

        }

        ComplexNumber[] compute() {

            //フーリエ変換
            ComplexNumber[] f_fourier = Power2CyclicConvolutionModule.this.power2FFT.compute(this.f, this.dftComputer);
            ComplexNumber[] g_fourier = Power2CyclicConvolutionModule.this.power2FFT.compute(this.g, this.dftComputer);

            //フーリエ係数の乗算
            ComplexNumber[] h_fourier = new ComplexNumber[this.length];
            for (int j = 0; j < this.length; j++) {
                h_fourier[j] = f_fourier[j].times(g_fourier[j]);
            }

            //フーリエ逆変換
            ComplexNumber[] h_ext = Power2CyclicConvolutionModule.this.power2FFT.compute(h_fourier, this.idftComputer);

            double invM = 1d / this.length;
            for (int j = 0; j < this.length; j++) {
                h_ext[j] = h_ext[j].timesReal(invM);
            }

            return h_ext;
        }

    }
}
