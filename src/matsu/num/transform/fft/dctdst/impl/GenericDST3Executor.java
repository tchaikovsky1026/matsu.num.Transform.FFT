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
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.dctdst.DST3Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link DST3Executor} の実装.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class GenericDST3Executor
        extends LinearByScalingStability
        implements DST3Executor,
        matsu.num.transform.fft.dctdst.GenericDST3Executor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DST3Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDST3Executor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DST-3は2N個の実数データ点a,
         * a[0] = 0;
         * a[j + 1] = x[j]exp[-i * 2pi * (j + 1) / (4N)] (j=0,...,N-1)
         * a[2N - 1 - j] = x[j]exp[-i * 2pi * (2N - 1 - j) / (4N)]
         * (j=0,...,N-2)
         * に対してFFTを実行し,
         * X[k] = (i/2) * A[k]
         * とすればよい.
         */

        int fftSize = size * 2;
        FourierBasisComputer dftBasisComputer =
                this.computerSupplier.covering(2 * fftSize, FourierType.DFT);

        /* FFT用のデータ作成 */
        //exp[-i*2pi*j/(4N)]型の計算をするため, 4NサイズのDFT基底を得る
        FourierBasis dftBasis_4N = dftBasisComputer.getBasis(2 * fftSize);
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        ComplexNumber[] a = new ComplexNumber[fftSize];
        a[0] = ComplexNumber.ZERO;
        for (int j = 0; j < size; j++) {
            a[j + 1] = dftBasis_4N.valueAt(j + 1).timesReal(data[j]);
        }
        for (int j = 0; j < size - 1; j++) {
            a[fftSize - 1 - j] = dftBasis_4N.valueAt(fftSize - 1 - j).timesReal(data[j]);
        }

        /* FFT実行 */
        //前処理/後処理のための係数を得るために必要.
        ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

        /* 結果をDST-3に変換 */
        double[] result = new double[size];
        for (int k = 0; k < size; k++) {
            result[k] = -0.5 * A[k].imaginary();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDST3Executor";
    }
}
