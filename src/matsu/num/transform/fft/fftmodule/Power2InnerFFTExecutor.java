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
import matsu.num.transform.fft.number.Power2Util;

/**
 * 2の累乗の要素数に関するFFT型変換を扱う.
 * 
 * <p>
 * このクラスが提供する{@link InnerDFTExecutor#compute(ComplexNumber[], FourierBasisComputer)}では,
 * 例外スロー条件として次を追加する. <br>
 * サイズが2の塁乗でない場合に{@link IllegalArgumentException}をスローする.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class Power2InnerFFTExecutor implements InnerDFTExecutor {

    private final RawInnerDFTExecutor rawDFT;

    /**
     * このクラスのインスタンスを構築する.
     */
    public Power2InnerFFTExecutor() {
        super();
        this.rawDFT = new RawInnerDFTExecutor();
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer) {
        return new Power2FFTHelper(data, basisComputer).compute();
    }

    private final class Power2FFTHelper {

        private final FourierBasisComputer fourierBasisComputer;
        private final ComplexNumber[] data;

        /**
         * 2の累乗の要素数に関するFFTを構築する.
         * 
         * @param data 変換を行うデータ
         * @param basisComputer 基底関数の生成器
         * @throws IllegalArgumentException サイズが2の累乗でない場合,
         *             基底関数の生成器がdataのサイズに対応していない場合
         * @throws NullPointerException 引数にnullが含まれる場合
         * @see FourierBasisComputer#support(int)
         */
        private Power2FFTHelper(ComplexNumber[] data, FourierBasisComputer basisComputer) {
            //isPowerOf2の呼び出しで1以上かどうかをバリデーションする
            if (!Power2Util.isPowerOf2(data.length)) {
                throw new IllegalArgumentException("2の累乗でない");
            }

            if (!basisComputer.support(data.length)) {
                throw new IllegalArgumentException("基底関数がサイズに対応していない");
            }

            this.fourierBasisComputer = basisComputer;
            this.data = data;
        }

        /**
         * 結果を返す.
         * 
         * @return DFT/IDFTの結果
         */
        ComplexNumber[] compute() {
            return this.fftRecursion(this.data);
        }

        /**
         * 再帰によるFFT処理. <br>
         * [position]から[position + length - 1]の範囲をDFTする.
         * 
         * @param real real
         * @param imaginary imaginary
         * @param position position
         * @param length length
         */
        private ComplexNumber[] fftRecursion(ComplexNumber[] currentData) {

            int N = currentData.length;

            /* 標本サイズが4以下ならば直接DFTを呼ぶ */
            if (N <= 4) {
                return Power2InnerFFTExecutor.this.rawDFT.compute(currentData, this.fourierBasisComputer);
            }

            /* 標本サイズが8以上の場合は再帰的FFT */
            /* N1が直接DFT, N2が再帰的FFTのサイズ */
            int N1 = 4;
            //N2は2以上である
            int N2 = N / 4;

            /* dataからN2飛ばしでデータ抽出し, サイズN1の系列をN2個作る */
            ComplexNumber[][] b = new ComplexNumber[N2][];
            for (int j2 = 0; j2 < N2; j2++) {
                ComplexNumber[] b_j2 = new ComplexNumber[N1];

                for (int j1 = 0; j1 < N1; j1++) {
                    b_j2[j1] = currentData[N2 * j1 + j2];
                }

                b[j2] = b_j2;
            }

            /* 横方向のDFT (サイズN1をN2個) */
            for (int j2 = 0; j2 < N2; j2++) {
                b[j2] = Power2InnerFFTExecutor.this.compute(b[j2], this.fourierBasisComputer);
            }

            // N2 = 1の場合はDFTが完了している
            if (N2 == 1) {
                return b[0];
            }

            //回転
            FourierBasis basis_N = this.fourierBasisComputer.getBasis(N);
            for (int j2 = 0; j2 < N2; j2++) {
                ComplexNumber[] b_j2 = b[j2];
                for (int k1 = 0; k1 < N1; k1++) {
                    ComplexNumber a = b_j2[k1];
                    ComplexNumber W = basis_N.valueAt(j2 * k1);
                    b_j2[k1] = a.times(W);
                }
            }

            /* 転置: (サイズN1をN2個)→(サイズN2をN1個) */
            ComplexNumber[][] d = new ComplexNumber[N1][N2];
            for (int j2 = 0; j2 < N2; j2++) {
                ComplexNumber[] b_j2 = b[j2];
                for (int k1 = 0; k1 < N1; k1++) {
                    d[k1][j2] = b_j2[k1];
                }
            }

            /* d(サイズN2をN1個)に対して再帰的FFT */
            for (int k1 = 0; k1 < N1; k1++) {
                d[k1] = this.fftRecursion(d[k1]);
            }

            //転置
            ComplexNumber[] result = new ComplexNumber[N];
            for (int k1 = 0; k1 < N1; k1++) {
                ComplexNumber[] d_k1 = d[k1];
                for (int k2 = 0; k2 < N2; k2++) {
                    result[N1 * k2 + k1] = d_k1[k2];
                }
            }

            return result;

        }
    }

}
