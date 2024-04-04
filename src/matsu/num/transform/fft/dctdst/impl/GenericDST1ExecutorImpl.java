/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.dctdst.impl;

import java.util.Arrays;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dctdst.GenericDST1Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * {@link GenericDST1Executor} の実装.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class GenericDST1ExecutorImpl
        extends LinearByScalingStability implements GenericDST1Executor {

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DST1Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDST1ExecutorImpl(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(MAX_DATA_SIZE, arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DST-1は2N+2個の実数データ点a,
         * a[0] = 0, a[1] = x[0],..., a[N] = x[N-1],
         * a[N+1] = 0, a[N+2] = -x[N-1],..., x[2N+1] = -x[0]
         * に対してFFTを実行し,
         * X[k] = (-1/2)Im(A[k+1])
         * とすればよい.
         */

        /* FFT用のデータ作成 */
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        int fftSize = 2 * size + 2;
        ComplexNumber[] a = new ComplexNumber[fftSize];
        Arrays.fill(a, ComplexNumber.ZERO);
        for (int i = 0; i < size; i++) {
            a[i + 1] = ComplexNumber.of(data[i], 0);
        }
        for (int i = 0; i < size; i++) {
            a[fftSize - i - 1] = ComplexNumber.of(-a[i + 1].real(), 0);
        }

        /* FFT実行 */
        ComplexNumber[] A = this.fftExecutor
                .compute(a, this.computerSupplier.covering(fftSize, FourierType.DFT));

        /* 結果をDST-1に変換 */
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = -0.5 * A[i + 1].imaginary();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDST1Executor";
    }
}
