/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.5.8
 */
package matsu.num.transform.fft.dctdst.impl;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.dctdst.DST2Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link DST2Executor} の実装.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class GenericDST2Executor
        extends LinearByScalingStability
        implements DST2Executor,
        matsu.num.transform.fft.dctdst.GenericDST2Executor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DST2Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDST2Executor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DST-2は2N個の実数データ点a,
         * a[j] = x[j] (j=0,...,N-1)
         * a[2N - 1 - j] = -x[j] (j=0,...,N-1)
         * に対してFFTを実行し,
         * X[k] = (i/2) * exp[-i*2pi*(k+1)/(4N)] * A[k + 1]
         * とすればよい.
         */

        int fftSize = size * 2;
        FourierBasisComputer dftBasisComputer =
                this.computerSupplier.covering(2 * fftSize, FourierType.DFT);

        /* FFT用のデータ作成 */
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        ComplexNumber[] a = new ComplexNumber[fftSize];
        for (int i = 0; i < size; i++) {
            a[i] = ComplexNumber.of(data[i], 0);
        }
        for (int i = 0; i < size; i++) {
            a[fftSize - 1 - i] = ComplexNumber.of(-data[i], 0);
        }

        /* FFT実行 */
        //前処理/後処理のための係数を得るために必要.
        ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

        /* 結果をDST-2に変換 */
        //exp[-i*2pi*(k+1)/(4N)]の計算をするため, 4NサイズのDFT基底を得る
        FourierBasis dftBasis_4N = dftBasisComputer.getBasis(2 * fftSize);
        double[] result = new double[size];
        for (int k = 0; k < size; k++) {
            result[k] = -0.5 * A[k + 1].times(dftBasis_4N.valueAt(k + 1)).imaginary();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDST2Executor";
    }
}
