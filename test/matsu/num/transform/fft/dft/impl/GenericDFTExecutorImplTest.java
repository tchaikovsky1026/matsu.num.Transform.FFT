/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.dft.impl;

import static matsu.num.transform.fft.NumberArrayDataCreator.*;
import static matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.DFTExecutor;
import matsu.num.transform.fft.GenericDFTExecutor;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierBasisComputerSupplierDefaultHolder;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.fftmodule.RawInnerDFTExecutor;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.validation.NotRequiredDataSizeException;

/**
 * {@link GenericDFTExecutorImpl} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class GenericDFTExecutorImplTest {

    public static final Class<?> TEST_CLASS = GenericDFTExecutorImpl.class;
    private static final GenericDFTExecutor EXECUTOR_NEW = new GenericDFTExecutorImpl(
            TrigonometryForTesting.INSTANCE, ArraysUtilForTesting.INSTANCE);

    private static final FourierBasisComputer.Supplier COMPUTER_SUPPLIER =
            FourierBasisComputerSupplierDefaultHolder.INSTANCE;
    private static final RawInnerDFTExecutor RAW_DFT_EXECUTOR = new RawInnerDFTExecutor();

    public static class 入力サイズの検証 {

        @Test(expected = NotRequiredDataSizeException.class)
        public void test_入力サイズが0でNRDSEx() {
            EXECUTOR_NEW.apply(ComplexNumberArrayDTO.zeroFilledOf(0));
        }

        @Test(expected = None.class)
        public void test_入力サイズが1で正常() {
            EXECUTOR_NEW.apply(ComplexNumberArrayDTO.zeroFilledOf(1));
        }
    }

    @RunWith(Theories.class)
    public static class DFTの実行を検証 {

        @DataPoint
        public static final DFTExecutor executorNew = EXECUTOR_NEW;

        @DataPoint
        public static ComplexNumberArrayDTO dataArrayDto_size_10;
        @DataPoint
        public static ComplexNumberArrayDTO dataArrayDto_size_49;

        @BeforeClass
        public static void before_dataArrayDto_size_10_複素数データの設定() {
            dataArrayDto_size_10 = createComplexArrayDTO(10);
        }

        @BeforeClass
        public static void before_dataArrayDto_size_49_複素数データの設定() {
            dataArrayDto_size_49 = createComplexArrayDTO(49);
        }

        @Theory
        public void test_DFTの実行(DFTExecutor executor, ComplexNumberArrayDTO dataDTO) {

            ComplexNumber[] data = ComplexNumber.from(dataDTO.realPart, dataDTO.imaginaryPart);

            FourierBasisComputer basisComputer = COMPUTER_SUPPLIER.covering(data.length, FourierType.DFT);
            ComplexNumberArrayDTO resultDto = executor.apply(dataDTO);
            double[][] expected = ComplexNumber.separateToArrays(RAW_DFT_EXECUTOR.compute(data, basisComputer));

            double[] resReal = resultDto.realPart.clone();
            double[] resImag = resultDto.imaginaryPart.clone();
            subtract(resReal, expected[0]);
            subtract(resImag, expected[1]);

            double resNorm = Math.max(normMax(resReal), normMax(resImag));
            double norm = Math.max(normMax(expected[0]), normMax(expected[1]));

            assertThat(resNorm, is(lessThan(1E-12 * norm + 1E-100)));
        }
    }

    public static class toString表示の検証 {

        @Test
        public void test_toString表示() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(EXECUTOR_NEW);
            System.out.println();
        }
    }
}
