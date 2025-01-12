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
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputerSupplierDefaultHolder;

/**
 * {@link CyclicConvolutionModule}クラスのテスト.
 */
@RunWith(Enclosed.class)
final class CyclicConvolutionModuleTest {

    public static final Class<?> TEST_CLASS = CyclicConvolutionModule.class;

    private static final CyclicConvolutionModule CYCLIC_CONVOLUTION_MODULE =
            new CyclicConvolutionModule(FourierBasisComputerSupplierDefaultHolder.INSTANCE);

    @RunWith(Theories.class)
    public static class 畳み込み検証 {

        @DataPoint
        public static ComplexNumber[][] data_size_4;
        @DataPoint
        public static ComplexNumber[][] data_size_7;

        @BeforeClass
        public static void before_size_4_fgを作成() {
            data_size_4 = new ComplexNumber[][] {
                    createComplexArrayData(4),
                    createComplexArrayData(4)
            };
        }

        @BeforeClass
        public static void before_size_7_fgを作成() {
            data_size_7 = new ComplexNumber[][] {
                    createComplexArrayData(7),
                    createComplexArrayData(7)
            };
        }

        @Theory
        public void test_畳み込みの実行(ComplexNumber[][] data) {
            ComplexNumber[] f = data[0];
            ComplexNumber[] g = data[1];

            double[][] resultArray = ComplexNumber.separateToArrays(
                    CYCLIC_CONVOLUTION_MODULE.compute(f, g));
            double[][] expectedArray = ComplexNumber.separateToArrays(
                    new CyclicConvolutionMoch(f, g).computeConvolution());

            double[] resReal = resultArray[0].clone();
            subtract(resReal, expectedArray[0]);
            double[] resImag = resultArray[1].clone();
            subtract(resImag, expectedArray[1]);

            double[][] arrayF = ComplexNumber.separateToArrays(f);
            double[][] arrayG = ComplexNumber.separateToArrays(g);

            double norm = Math.max(normMax(arrayF[0]), normMax(arrayF[1]))
                    * Math.max(normMax(arrayG[0]), normMax(arrayG[1]));
            double normRes = Math.max(
                    normMax(resReal), normMax(resImag));

            assertThat(normRes, is(lessThan(1E-12 * norm + 1E-100)));
        }
    }
}
