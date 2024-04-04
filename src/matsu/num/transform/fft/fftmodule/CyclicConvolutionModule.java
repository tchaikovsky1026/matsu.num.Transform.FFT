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

import java.util.Arrays;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.number.Power2Util;

/**
 * 複素数列の巡回畳み込みを扱う.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class CyclicConvolutionModule {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_SEQUENCE_SIZE = 0x1000_0000;

    private final Power2CyclicConvolutionModule power2CyclicConv;

    /**
     * このクラスの機能を実行するインスタンスを返す.
     * 
     * @param computerSupplier Fourier基底コンピュータのサプライヤ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public CyclicConvolutionModule(FourierBasisComputer.Supplier computerSupplier) {
        super();
        this.power2CyclicConv = new Power2CyclicConvolutionModule(computerSupplier);
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

        return new CyclicConvHelper(f, g).compute();
    }

    private final class CyclicConvHelper {

        private final int actualLength;
        private final int extendedLength;

        private final ComplexNumber[] f_ext;
        private final ComplexNumber[] g_ext;

        /**
         * 巡回畳み込みを生成する.
         * 
         * @param f f
         * @param g g
         * @throws IllegalArgumentException 引数の長さが一致しない場合, 長さが0の場合, 長さが大きすぎる場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        private CyclicConvHelper(ComplexNumber[] f, ComplexNumber[] g) {
            if (f.length != g.length) {
                throw new IllegalArgumentException("長さが一致しない");
            }
            if (f.length == 0) {
                throw new IllegalArgumentException("長さが0である");
            }
            if (f.length > MAX_SEQUENCE_SIZE) {
                throw new IllegalArgumentException("長さが大きすぎる");
            }

            this.actualLength = f.length;
            this.extendedLength = this.calcExtendedLength();

            this.f_ext = this.f_ext(f);
            this.g_ext = this.g_ext(g);
        }

        ComplexNumber[] compute() {
            return Arrays.copyOf(
                    CyclicConvolutionModule.this.power2CyclicConv.compute(this.f_ext, this.g_ext),
                    this.actualLength);
        }

        private int calcExtendedLength() {
            int N = this.actualLength;

            if (Power2Util.isPowerOf2(N)) {
                return N;
            }

            int minExtLength = 2 * N - 1;
            return Power2Util.ceilToPower2(minExtLength);
        }

        private ComplexNumber[] f_ext(ComplexNumber[] f) {
            int N = this.actualLength;
            int p = this.extendedLength - N;

            ComplexNumber[] out = new ComplexNumber[this.extendedLength];
            out[0] = f[0];
            Arrays.fill(out, 1, p + 1, ComplexNumber.ZERO);
            System.arraycopy(f, 1, out, p + 1, N - 1);

            return out;
        }

        private ComplexNumber[] g_ext(ComplexNumber[] g) {
            int N = this.actualLength;
            int ext_N = this.extendedLength;

            ComplexNumber[] out = new ComplexNumber[this.extendedLength];

            int start_pos = 0;
            while (start_pos < ext_N) {
                int l = Math.min(N, ext_N - start_pos);
                System.arraycopy(g, 0, out, start_pos, l);
                start_pos += N;
            }

            return out;
        }

    }

}
