/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.number;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link PrimeFactorization}クラスのテスト.
 */
@RunWith(Enclosed.class)
final class PrimeFactorizationTest {

    public static final Class<?> TEST_CLASS = PrimeFactorization.class;

    public static class 整数1の素因数分解でテスト {

        private PrimeFactorization factorization;

        @Before
        public void before_1を素因数分解する() {
            factorization = PrimeFactorization.of(1);
        }

        @Test(expected = IllegalStateException.class)
        public void test_分離不可_value() {
            factorization.separatedValue();
        }

        @Test(expected = IllegalStateException.class)
        public void test_分離不可_child() {
            factorization.child();
        }
    }

    public static class 整数12の素因数分解でテスト {

        private PrimeFactorization factorization;

        @Before
        public void before_12を素因数分解する() {
            factorization = PrimeFactorization.of(12);
        }

        @Test
        public void test_素因数は2_2_3() {
            int[] expected = { 2, 2, 3 };
            Arrays.sort(expected);

            PrimeFactorization current = factorization;
            List<Integer> resultList = new ArrayList<>();
            while (current.original() > 1) {
                resultList.add(current.separatedValue());
                current = current.child();
            }

            int[] result = new int[resultList.size()];
            {
                int i = 0;
                for (int factor : resultList) {
                    result[i] = factor;
                    i++;
                }
            }
            Arrays.sort(result);

            //配列の比較
            assertThat(Arrays.equals(result, expected), is(true));

        }

        @Test(expected = None.class)
        public void test_3回分離可能() {
            factorization.child().child().child();
        }

        @Test(expected = IllegalStateException.class)
        public void test_4回分離は不可() {
            factorization.child().child().child().child();
        }
    }

    public static class 分離のテスト {

        private PrimeFactorization factorization;

        @Before
        public void before_12を素因数分解する() {
            factorization = PrimeFactorization.of(12);
        }

        @Test
        public void test_1度目は2が分離() {
            PrimeFactorization child = factorization;

            assertThat(child.separatedValue(), is(2));
            assertThat(child.child().original(), is(6));
        }

        @Test
        public void test_2度目は2が分離() {
            PrimeFactorization child = factorization.child();

            assertThat(child.separatedValue(), is(2));
            assertThat(child.child().original(), is(3));
        }

        @Test
        public void test_3度目は3が分離() {
            PrimeFactorization child = factorization.child().child();

            assertThat(child.separatedValue(), is(3));
            assertThat(child.child().original(), is(1));
        }
    }
}
