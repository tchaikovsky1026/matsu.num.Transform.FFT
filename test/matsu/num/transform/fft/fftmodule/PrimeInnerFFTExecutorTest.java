/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.fftmodule;

import static matsu.num.transform.fft.NumberArrayDataCreator.*;
import static matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierBasisComputerSupplierDefaultHolder;
import matsu.num.transform.fft.component.FourierType;

/**
 * {@link PrimeInnerFFTExecutor}クラスのテスト.
 */
final class PrimeInnerFFTExecutorTest {

    public static final Class<?> TEST_CLASS = PrimeInnerFFTExecutor.class;

    private static final FourierBasisComputer.Supplier COMPUTER_SUPPLIER =
            FourierBasisComputerSupplierDefaultHolder.INSTANCE;
    private static final PrimeInnerFFTExecutor PRIME_FFT_EXECUTOR = new PrimeInnerFFTExecutor(COMPUTER_SUPPLIER);
    private static final RawInnerDFTExecutor RAW_DFT_EXECUTOR = new RawInnerDFTExecutor();

    @RunWith(Theories.class)
    public static class FFT検証_サイズ13 {

        @DataPoint
        public static ComplexNumber[] data_size_13;
        @DataPoint
        public static ComplexNumber[] data_size_829;

        @BeforeClass
        public static void before_data_size_13_ノコギリ波のデータを作成() {
            data_size_13 = createComplexArrayData(13);
        }

        @BeforeClass
        public static void before_data_size_829_三角波のデータを作成() {
            data_size_829 = createComplexArrayData(829);
        }

        @Theory
        public void test_FFTの実行(ComplexNumber[] data) {
            FourierBasisComputer basisComputer = COMPUTER_SUPPLIER.covering(data.length, FourierType.DFT);
            double[][] resultArray = ComplexNumber.separateToArrays(
                    PRIME_FFT_EXECUTOR.compute(data, basisComputer));
            double[][] expectedArray = ComplexNumber.separateToArrays(
                    RAW_DFT_EXECUTOR.compute(data, basisComputer));

            double[] resReal = resultArray[0].clone();
            subtract(resReal, expectedArray[0]);
            double[] resImag = resultArray[1].clone();
            subtract(resImag, expectedArray[1]);

            double norm = Math.max(
                    normMax(expectedArray[0]), normMax(expectedArray[1]));
            double normRes = Math.max(
                    normMax(resReal), normMax(resImag));

            assertThat(normRes, is(lessThan(1E-12 * norm + 1E-100)));
        }
    }
}
