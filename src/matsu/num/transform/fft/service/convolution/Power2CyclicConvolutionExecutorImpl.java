/**
 * 2024.2.17
 */
package matsu.num.transform.fft.service.convolution;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.convolution.Power2CyclicConvolutionExecutor;
import matsu.num.transform.fft.fftmodule.Power2CyclicConvolutionModule;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.skeletal.convolution.Power2CyclicConvolutionSkeletal;

/**
 * {@link Power2CyclicConvolutionExecutor} の実装.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class Power2CyclicConvolutionExecutorImpl
        extends Power2CyclicConvolutionSkeletal implements Power2CyclicConvolutionExecutor {

    private final FourierBasisComputer.Supplier computerSupplier;
    private final Power2CyclicConvolutionModule module;

    /**
     * 巡回畳み込みを構築する.
     * 
     * @param lib ライブラリ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public Power2CyclicConvolutionExecutorImpl(CommonLib lib) {
        super();
        this.computerSupplier = new FourierBasisComputer.Supplier(lib.trigonometry());
        this.module = new Power2CyclicConvolutionModule(this.computerSupplier);
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
