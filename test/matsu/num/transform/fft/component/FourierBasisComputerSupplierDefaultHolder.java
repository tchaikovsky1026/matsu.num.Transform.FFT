/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.component;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.TrigonometryForTesting;

/**
 * Fourier基底コンピュータのデフォルトサプライヤのホルダ.
 */
@Ignore
public final class FourierBasisComputerSupplierDefaultHolder {

    public static final FourierBasisComputer.Supplier INSTANCE =
            new FourierBasisComputer.Supplier(TrigonometryForTesting.INSTANCE);

    private FourierBasisComputerSupplierDefaultHolder() {
        // インスタンス化不可
        throw new AssertionError();
    }
}
