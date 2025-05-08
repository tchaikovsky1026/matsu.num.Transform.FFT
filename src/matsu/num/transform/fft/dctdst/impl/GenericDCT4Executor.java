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
import matsu.num.transform.fft.dctdst.DCT4Executor;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link DCT4Executor} の実装.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class GenericDCT4Executor
        extends LinearByScalingStability
        implements DCT4Executor,
        matsu.num.transform.fft.dctdst.GenericDCT4Executor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final Trigonometry trigonometry;
    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor fftExecutor;

    /**
     * DCT4Executorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericDCT4Executor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.trigonometry = trigonometry;
        this.computerSupplier = new FourierBasisComputer.Supplier(this.trigonometry);
        this.fftExecutor = new GenericInnerFFTExecutor(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    protected double[] applyInner(double[] data) {

        int size = data.length;

        /*
         * DCT-4は2N個の実数データ点a,
         * a[j] = x[j] * exp(-i * 2pi * j/(4N)) * exp(-i * pi/(4N))
         * (j=0,...,N-1),
         * a[2N - 1 - j] = x[j] * exp(-i * 2pi * (4N - j)/(4N)) * exp(i
         * * pi/(4N)) (j=0,...,N-1),
         * に対してFFTを実行し,
         * X[k] = 0.5 * exp[-i*2pi*k/(4N)] * A[k]
         * とすればよい.
         */
        int fftSize = size * 2;
        FourierBasisComputer dftBasisComputer = this.computerSupplier.covering(2 * fftSize, FourierType.DFT);

        ComplexNumber rot_quarter;
        ComplexNumber invRot_quarter;
        {
            //cos, sin (pi/(4N))
            double phi = 0.5 / fftSize;
            double cos = this.trigonometry.cospi(phi);
            double sin = this.trigonometry.sinpi(phi);
            rot_quarter = ComplexNumber.of(cos, -sin);
            invRot_quarter = ComplexNumber.of(cos, sin);
        }

        /* FFT用のデータ作成 */
        //fftSizeの2倍を表す(最大2^29). 
        //前処理/後処理のための係数を得るために必要.
        FourierBasis dftBasis_4N = dftBasisComputer.getBasis(fftSize * 2);

        ComplexNumber[] a = new ComplexNumber[fftSize];
        for (int j = 0; j < size; j++) {
            a[j] = rot_quarter
                    .times(dftBasis_4N.valueAt(j))
                    .timesReal(data[j]);
        }
        for (int j = 0; j < size; j++) {
            //exp(-i * 2pi * (4N - j)/(4N))が位相2πになる(IndexOutOfBoundsEx)のを回避するため, 
            //x[j]を(-1)倍し, exp(-i * 2pi * (2N - j)/(4N))の回転にする
            a[fftSize - 1 - j] = invRot_quarter
                    .times(dftBasis_4N.valueAt(fftSize - j))
                    .timesReal(-data[j]);
        }

        /* FFT実行 */
        ComplexNumber[] A = this.fftExecutor.compute(a, dftBasisComputer);

        /* 結果をDCT-4に変換 */
        double[] result = new double[size];
        for (int k = 0; k < size; k++) {
            result[k] = 0.5 * A[k].times(dftBasis_4N.valueAt(k)).real();
        }

        return result;
    }

    @Override
    public String toString() {
        return "GenericDCT4Executor";
    }
}
