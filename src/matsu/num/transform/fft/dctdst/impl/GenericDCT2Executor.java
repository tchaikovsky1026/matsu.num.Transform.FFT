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
import matsu.num.transform.fft.dctdst.DCT2Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link DCT2Executor} の実装.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class GenericDCT2Executor
        extends LinearByScalingStability
        implements DCT2Executor,
        matsu.num.transform.fft.dctdst.GenericDCT2Executor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DCT2Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDCT2Executor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {
        int size = data.length;

        /*
         * DCT-2は2N個の実数データ点a,
         * a[0] = x[0],..., a[N-1] = x[N-1],
         * a[N] = x[N-1], ,..., x[2N-1] = x[0]
         * に対してFFTを実行し,
         * X[k] = 0.5 * exp[-i*2pi*k/(4N)] * A[k]
         * とすればよい.
         */

        /* FFT用のデータ作成 */
        //fftSizeの上限が　FFTExecutor.MAX_DATA_SIZE　になっている
        int fftSize = 2 * size;
        ComplexNumber[] a = new ComplexNumber[fftSize];
        for (int i = 0; i < size; i++) {
            a[i] = ComplexNumber.of(data[i], 0);
        }
        for (int i = 0; i < size; i++) {
            a[fftSize - 1 - i] = a[i];
        }

        /* FFT実行 */
        //DCT2サイズの4倍 = fftSizeの2倍を表す(最大2^29). 
        //前処理/後処理のための係数を得るために必要.
        int N4 = 2 * fftSize;
        FourierBasisComputer dftBasisComputer = this.computerSupplier.covering(N4, FourierType.DFT);
        ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

        /* 結果をDCT-2に変換 */
        //exp[-i*2pi*k/(4N)]の計算をするため, 4NサイズのDFT基底を得る
        FourierBasis dftBasis_4N = dftBasisComputer.getBasis(N4);
        double[] result = new double[size];
        for (int k = 0; k < size; k++) {
            result[k] = 0.5 * A[k].times(dftBasis_4N.valueAt(k)).real();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDCT2Executor";
    }
}
