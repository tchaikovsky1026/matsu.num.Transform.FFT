/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.1
 */
package matsu.num.transform.fft.convolution.impl;

import matsu.num.transform.fft.component.BiLinearByScalingStability;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.convolution.CyclicConvolutionExecutor;
import matsu.num.transform.fft.fftmodule.Power2CyclicConvolutionModule;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.number.Power2Util;
import matsu.num.transform.fft.validation.DataSizeNotMismatchException;
import matsu.num.transform.fft.validation.StructureRejected;

/**
 * {@link CyclicConvolutionExecutor} の実装. <br>
 * 2の累乗のデータサイズにのみ対応する.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[], double[])}
 * のreject条件は,
 * {@link CyclicConvolutionExecutor}
 * に対して次が追加される.
 * </p>
 * 
 * <ul>
 * <li>データサイズが2の累乗でない場合</li>
 * </ul>
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("removal")
public final class Power2CyclicConvolutionExecutor
        extends BiLinearByScalingStability
        implements CyclicConvolutionExecutor,
        matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor {

    /*
     * deprecated(removal)は, インターフェース削除後にスーパーインターフェースに変更する(v25以降).
     */

    private final FourierBasisComputer.Supplier computerSupplier;
    private final Power2CyclicConvolutionModule module;

    /**
     * 巡回畳み込みを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public Power2CyclicConvolutionExecutor(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.module = new Power2CyclicConvolutionModule(this.computerSupplier);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
        this.dataSizeContract.addRejectionContract(
                size -> !Power2Util.isPowerOf2(size),
                StructureRejected
                        .by(() -> new DataSizeNotMismatchException("データサイズが2の累乗でない"), "REJECT_BY_NOT_POWER_OF_2"));
    }

    @Override
    protected double[] applyInner(double[] f, double[] g) {
        int size = f.length;

        double[] imaginaryZero = new double[size];
        ComplexNumber[] complexF = ComplexNumber.from(f, imaginaryZero);
        ComplexNumber[] complexG = ComplexNumber.from(g, imaginaryZero);

        ComplexNumber[] complexResult = this.module.compute(complexF, complexG);
        return ComplexNumber.separateToArrays(complexResult)[0];
    }
}
