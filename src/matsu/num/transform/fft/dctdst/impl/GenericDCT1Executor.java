/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.1
 */
package matsu.num.transform.fft.dctdst.impl;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.dctdst.DCT1Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link DCT1Executor} の実装.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class GenericDCT1Executor
        extends LinearByScalingStability
        implements DCT1Executor,
        matsu.num.transform.fft.dctdst.GenericDCT1Executor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DCT1Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDCT1Executor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindRequiredSize(2);
        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DCT-1は2N-2個の実数データ点a,
         * a[0] = x[0],..., a[N-1] = x[N-1],
         * a[N] = x[N-2], ,..., x[2N-3] = x[1]
         * に対してFFTを実行し,
         * X[k] = 0.5 * A[k] (A[k]は実数)
         * とすればよい.
         */

        /* FFT用のデータ作成 */
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        int fftSize = 2 * size - 2;
        ComplexNumber[] a = new ComplexNumber[fftSize];
        for (int i = 0; i < size; i++) {
            a[i] = ComplexNumber.of(data[i], 0);
        }
        for (int i = 1; i < size - 1; i++) {
            a[fftSize - i] = a[i];
        }

        /* FFT実行 */
        ComplexNumber[] A = this.fftExecutor
                .compute(a, this.computerSupplier.covering(fftSize, FourierType.DFT));

        /* 結果をDCT-1に変換 */
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            result[i] = 0.5 * A[i].real();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDCT1Executor";
    }
}
