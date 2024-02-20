package matsu.num.transform.fft.component;

import org.junit.Ignore;

import matsu.num.transform.fft.service.CommonLib;

/**
 * Fourier基底コンピュータのデフォルトサプライヤのホルダ.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class FourierBasisComputerSupplierDefaultHolder {

    public static final FourierBasisComputer.Supplier INSTANCE =
            new FourierBasisComputer.Supplier(CommonLib.defaultImplemented().trigonometry());

    private FourierBasisComputerSupplierDefaultHolder() {
        // インスタンス化不可
        throw new AssertionError();
    }
}
