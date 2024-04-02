/**
 * 2024.4.2
 */
package matsu.num.transform.fft.convolution.impl;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.convolution.GenericCyclicConvolutionExecutor;
import matsu.num.transform.fft.fftmodule.CyclicConvolutionModule;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.skeletal.BiLinearByScalingStability;

/**
 * {@link GenericCyclicConvolutionExecutor} の実装.
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
public final class GenericCyclicConvolutionExecutorImpl
        extends BiLinearByScalingStability
        implements GenericCyclicConvolutionExecutor {

    private final FourierBasisComputer.Supplier computerSupplier;
    private final CyclicConvolutionModule module;

    /**
     * 巡回畳み込みを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericCyclicConvolutionExecutorImpl(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(MAX_DATA_SIZE, arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.module = new CyclicConvolutionModule(this.computerSupplier);
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

    @Override
    public String toString() {
        return "GenericCyclicConvolutionExecutor";
    }
}
