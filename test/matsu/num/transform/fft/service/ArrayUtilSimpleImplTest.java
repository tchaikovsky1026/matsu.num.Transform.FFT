/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link ArrayUtilSimpleImpl} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ArrayUtilSimpleImplTest {

    public static final Class<?> TEST_CLASS = ArrayUtilSimpleImpl.class;

    private static final ArraysUtil ARRAYS_UTIL = new ArrayUtilSimpleImpl();

    @RunWith(Theories.class)
    public static class 単項演算のテスト {

        @DataPoints
        public static double[][] data_src;

        @BeforeClass
        public static void before_データソースの用意() {
            int sizeMax = 8;
            List<double[]> list = new ArrayList<>();
            for (int size = 1; size <= sizeMax; size++) {
                for (int c = 0; c < 100; c++) {
                    List<Double> values = new ArrayList<>(size);
                    for (int i = 0; i < size; i++) {
                        values.add(Double.valueOf(-(i + 1)));
                    }
                    Collections.shuffle(values);

                    list.add(
                            values.stream()
                                    .mapToDouble(v -> v.doubleValue())
                                    .toArray());
                }
            }

            data_src = list.toArray(double[][]::new);
        }

        @Theory
        public void test_最大ノルム(double[] arr) {
            double result = ARRAYS_UTIL.normMax(arr);

            double expected = 0d;
            for (double v : arr) {
                expected = Math.max(result, Math.abs(v));
            }

            assertThat(result, is(expected));
        }
    }
}
