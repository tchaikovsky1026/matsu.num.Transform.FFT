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

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.number.PrimeFactorization;
import matsu.num.transform.fft.number.PrimeFactorization.Separated;

/**
 * 高速なDFT,IDFTの実行を扱う.
 * 
 * <p>
 * このクラスが提供する{@link InnerDFTExecutor#compute(ComplexNumber[], FourierBasisComputer)}の事前条件(引数の条件)は,
 * 標本サイズが大きすぎないことである. <br>
 * 標本サイズが{@link #MAX_DATA_SIZE}を超過した場合は{@link IllegalArgumentException}をスローする.
 * </p>
 * 
 * <p>
 * 標本サイズ(データサイズ)を<i>N</i>とし, <i>W</i> = exp(-i(2&pi;/<i>N</i>))とする. <br>
 * DFTでは, <i>k</i> = 0,...,<i>N</i> - 1として<br>
 * <i>A</i><sub><i>k</i></sub> =
 * &Sigma;<sub><i>j</i>=0</sub><sup><i>N</i>-1</sup><i>a</i><sub><i>j</i></sub>
 * <i>W</i><sup><i>jk</i></sup>, <br>
 * IDFTでは, <i>j</i> = 0,...,<i>N</i> - 1として<br>
 * <i>a</i><sub><i>j</i></sub> =
 * &Sigma;<sub><i>k</i>=0</sub><sup><i>N</i>-1</sup><i>A</i><sub><i>k</i></sub>
 * <i>W</i><sup>-<i>jk</i></sup> <br>
 * である.
 * </p>
 * 
 * <p>
 * 注意: このクラスが扱う変換は, DFTとIDFTが逆関数になっておらず, 両方を作用させると全体が<i>N</i>倍になる.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class GenericInnerFFTExecutor implements InnerDFTExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = PrimeInnerFFTExecutor.MAX_DATA_SIZE;

    private final InnerDFTExecutor rawDFT;
    private final InnerDFTExecutor primeFFT;

    /**
     * このクラスの機能を実行するインスタンスを返す.
     * 
     * @param computerSuppier Fourier基底コンピュータのサプライヤ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericInnerFFTExecutor(FourierBasisComputer.Supplier computerSuppier) {
        super();
        this.primeFFT = new PrimeInnerFFTExecutor(computerSuppier);
        this.rawDFT = new RawInnerDFTExecutor();
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer) {
        if (data.length == 0) {
            throw new IllegalArgumentException("データサイズが0である");
        }
        if (data.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("サイズが大きすぎる");
        }
        return new FFTCalculation(data, basisComputer).compute();
    }

    private final class FFTCalculation {

        /**
         * 各素因数に対して使うアルゴリズム選択の閾値. <br>
         * この値未満の場合はRaw, この値以上の場合はPrimeFFTを使う. <br>
         * 素数DFTの都合上, この値は5以上でなければならない.
         */
        private static final int PRIME_FFT_THRESHOLD = 320;

        private final int entireSize;

        private final ComplexNumber[] data;
        private final FourierBasisComputer basisComputer;

        private final PrimeFactorization primeFactorization;

        private FFTCalculation(ComplexNumber[] data, FourierBasisComputer basisComputer) {
            assert data.length > 0 : "データサイズが0である";
            assert data.length <= GenericInnerFFTExecutor.MAX_DATA_SIZE : "サイズが大きすぎる";

            this.data = data.clone();
            this.basisComputer = basisComputer;

            this.entireSize = this.data.length;

            //基底生成メソッドを呼ぶことで, このデータサイズに整合するかを確かめる
            this.basisComputer.getBasis(this.entireSize);

            this.primeFactorization = PrimeFactorization.of(this.entireSize);
        }

        ComplexNumber[] compute() {
            return this.fftRecursion(this.data, this.primeFactorization);
        }

        /**
         * 
         * 再帰的にFFTを実行する.
         * 
         * <p>
         * dataの中身がどこから参照されているかわからないので, 中身を書き換える処理を行ってはいけない
         * </p>
         * 
         * @param currentData
         * @param primeFactorization
         * @return
         */
        private ComplexNumber[] fftRecursion(ComplexNumber[] currentData, PrimeFactorization primeFactorization) {
            assert currentData.length == primeFactorization.original();

            if (currentData.length == 1) {
                return currentData;
            }

            /*
             * N1とN2に分解
             * N1が直接DFT, N2が再帰的FFTのサイズ
             */
            int N = primeFactorization.original();
            Separated separated = primeFactorization.separateLowest();
            int N1 = separated.separatedValue();
            // N2の素因数分解
            PrimeFactorization primeFact_N2 = separated.child();
            int N2 = primeFact_N2.original();

            //素因数2が2個以上取れる場合, サイズ4のDFTに置き換える
            if (N2 >= 2) {
                if (N1 == 2 && primeFact_N2.separateLowest().separatedValue() == 2) {
                    N1 = 4;
                    primeFact_N2 = primeFact_N2.separateLowest().child();
                    N2 = primeFact_N2.original();
                }
            }

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
                b[j2] = this.fftLocal(b[j2]);
            }

            // N2 = 1の場合はDFTが完了している
            if (N2 == 1) {
                return b[0];
            }

            //回転
            FourierBasis basis_N = this.basisComputer.getBasis(N);
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
                d[k1] = this.fftRecursion(d[k1], primeFact_N2);
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

        private ComplexNumber[] fftLocal(ComplexNumber[] localData) {

            if (localData.length < PRIME_FFT_THRESHOLD) {
                return GenericInnerFFTExecutor.this.rawDFT.compute(localData, this.basisComputer);
            }

            return GenericInnerFFTExecutor.this.primeFFT.compute(localData, this.basisComputer);
        }
    }
}
