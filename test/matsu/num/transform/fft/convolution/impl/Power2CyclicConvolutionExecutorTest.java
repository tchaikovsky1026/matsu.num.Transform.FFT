/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.convolution.impl;

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

import matsu.num.transform.fft.convolution.CyclicConvolutionExecutor;
import matsu.num.transform.fft.convolution.RealCyclicConvMoch;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule;
import matsu.num.transform.fft.validation.DataSizeNotMismatchException;
import matsu.num.transform.fft.validation.NotRequiredDataSizeException;

/**
 * {@link Power2CyclicConvolutionExecutor}クラスのテスト.
 */
@RunWith(Enclosed.class)
final class Power2CyclicConvolutionExecutorTest {

    public static final Class<?> TEST_CLASS = Power2CyclicConvolutionExecutor.class;

    private static final Power2CyclicConvolutionExecutor EXECUTOR_NEW =
            new Power2CyclicConvolutionExecutor(TrigonometryForTesting.INSTANCE, ArraysUtilForTesting.INSTANCE);

    public static class 事前条件テスト {

        @Test(expected = DataSizeNotMismatchException.class)
        public void test_サイズが整合しない場合はDSMNEx() {
            EXECUTOR_NEW.apply(new double[4], new double[8]);
        }

        @Test(expected = DataSizeNotMismatchException.class)
        public void test_2の累乗でない場合はDSMNEx() {
            EXECUTOR_NEW.apply(new double[5], new double[5]);
        }

        @Test(expected = NotRequiredDataSizeException.class)
        public void test_0の場合はNRDSEx() {
            EXECUTOR_NEW.apply(new double[0], new double[0]);
        }

        @Test(expected = None.class)
        public void test_1の場合は例外なし() {
            EXECUTOR_NEW.apply(new double[1], new double[1]);
        }
    }

    @RunWith(Theories.class)
    public static class 実数畳み込みテスト {

        @DataPoint
        public static final CyclicConvolutionExecutor executorNew = EXECUTOR_NEW;

        /**
         * [f, g, expected_conv_fg]
         */
        @DataPoint
        public static double[][] data1;
        @DataPoint
        public static double[][] data2;

        @BeforeClass
        public static void before_data1準備_size_8() {
            data1 = new double[][] { createArrayData(8), createArrayData(8) };
        }

        @BeforeClass
        public static void before_data2準備_size_1() {
            data2 = new double[][] { createArrayData(1), createArrayData(1) };
        }

        @Theory
        public void test(CyclicConvolutionExecutor executor, double[][] data) {

            double[] f = data[0];
            double[] g = data[1];
            double[] expected_conv_fg = RealCyclicConvMoch.instance().apply(f, g);

            double[] result = executor.apply(f, g);
            double norm = normMax(f) * normMax(g);

            double res[] = result.clone();
            ArraysUtilStaticForTestModule.subtract(res, expected_conv_fg);

            assertThat(normMax(res), is(lessThan(1E-12 * norm + 1E-100)));
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
