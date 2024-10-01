/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.1
 */
package matsu.num.transform.fft.dctdst.impl;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.dctdst.GenericDCT3Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link GenericDCT3Executor} の実装.
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
public final class GenericDCT3ExecutorImpl
        extends LinearByScalingStability implements GenericDCT3Executor {

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DCT3Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDCT3ExecutorImpl(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DCT-3は2N個の実数データ点a,
         * a[0] = x[0],
         * a[j] = x[j] * exp(-i * 2pi * j/(4N)) (j=1,...,N-1),
         * a[N] = 0;
         * a[2N - j] = x[j] * exp(i * 2pi * j/(4N)) (j=1,...,N-1),
         * に対してFFTを実行し,
         * X[k] = 0.5 * A[k]
         * とすればよい.
         */

        /* FFT用のデータ作成 */
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        int fftSize = 2 * size;
        //DCT3サイズの4倍 = fftSizeの2倍を表す(最大2^29). 
        //前処理/後処理のための係数を得るために必要.
        int N4 = fftSize * 2;
        FourierBasisComputer dftBasisComputer = this.computerSupplier.covering(N4, FourierType.DFT);
        FourierBasis dftBasis_4N = dftBasisComputer.getBasis(N4);

        ComplexNumber[] a = new ComplexNumber[fftSize];
        a[0] = ComplexNumber.of(data[0], 0);
        for (int j = 1; j < size; j++) {
            a[j] = dftBasis_4N.valueAt(j).timesReal(data[j]);
        }
        a[size] = ComplexNumber.ZERO;
        for (int j = 1; j < size; j++) {
            a[fftSize - j] = dftBasis_4N.valueAt(N4 - j).timesReal(data[j]);
        }

        /* FFT実行 */
        ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

        /* 結果をDCT-3に変換 */
        double[] result = new double[size];
        for (int k = 0; k < size; k++) {
            result[k] = 0.5 * A[k].real();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDCT3Executor";
    }
}
