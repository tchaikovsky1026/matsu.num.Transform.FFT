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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.number.PrimeFactorization.Separated;

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

        @Test
        public void test_素因数は0個() {
            assertThat(factorization.numberOfFactors(), is(0));
        }
    }

    public static class 整数12の素因数分解でテスト {

        private PrimeFactorization factorization;

        @Before
        public void before_12を素因数分解する() {
            factorization = PrimeFactorization.of(12);
        }

        @Test
        public void test_素因数は3個() {
            assertThat(factorization.numberOfFactors(), is(3));
        }

        @Test
        public void test_素因数は2_2_3() {
            int[] expected = { 2, 2, 3 };
            Arrays.sort(expected);

            PrimeFactorization current = factorization;
            List<Integer> resultList = new ArrayList<>();
            while (current.numberOfFactors() > 0) {
                Separated separated = current.separate();
                current = separated.child();
                resultList.add(separated.separatedValue());
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

        @Test
        public void test_3回separate可能() {
            factorization.separate().child().separate().child().separate();
        }

        @Test(expected = IllegalStateException.class)
        public void test_4回separateは不可() {
            factorization.separate().child().separate().child().separate().child().separate();
        }
    }

    public static class Separatedのテスト_上から {

        private PrimeFactorization factorization;

        @Before
        public void before_12を素因数分解する() {
            factorization = PrimeFactorization.of(12);
        }

        @Test
        public void test_1度目は3が分離() {
            Separated separated = factorization.separateHighest();

            assertThat(separated.separatedValue(), is(3));
            assertThat(separated.child().original(), is(4));
        }

        @Test
        public void test_2度目は2が分離() {
            Separated separated = factorization.separateHighest().child().separateHighest();

            assertThat(separated.separatedValue(), is(2));
            assertThat(separated.child().original(), is(2));
        }

        @Test
        public void test_3度目は2が分離() {
            Separated separated = factorization.separateHighest().child().separateHighest().child().separateHighest();

            assertThat(separated.separatedValue(), is(2));
            assertThat(separated.child().original(), is(1));
        }

        @Test
        public void test_複数回の呼び出しで同一インスタンス_実装の詳細に依存() {
            assertThat(factorization.separateHighest() == factorization.separateHighest(), is(true));
        }

    }

    public static class Separatedのテスト_下から {

        private PrimeFactorization factorization;

        @Before
        public void before_12を素因数分解する() {
            factorization = PrimeFactorization.of(12);
        }

        @Test
        public void test_1度目は2が分離() {
            Separated separated = factorization.separateLowest();

            assertThat(separated.separatedValue(), is(2));
            assertThat(separated.child().original(), is(6));
        }

        @Test
        public void test_2度目は2が分離() {
            Separated separated = factorization.separateLowest().child().separateLowest();

            assertThat(separated.separatedValue(), is(2));
            assertThat(separated.child().original(), is(3));
        }

        @Test
        public void test_3度目は3が分離() {
            Separated separated = factorization.separateLowest().child().separateLowest().child().separateLowest();

            assertThat(separated.separatedValue(), is(3));
            assertThat(separated.child().original(), is(1));
        }

        @Test
        public void test_複数回の呼び出しで同一インスタンス_実装の詳細に依存() {
            assertThat(factorization.separateLowest() == factorization.separateLowest(), is(true));
        }

    }

    public static class Familyのテスト {

        private PrimeFactorization factorization;

        @Before
        public void before_36を素因数分解する() {
            factorization = PrimeFactorization.of(36);
        }

        @Test
        public void test_分離_上下_と分離_下上_の後の子は同一インスタンス_実装の詳細に依存() {
            PrimeFactorization lh = factorization.separateLowest().child().separateHighest().child();
            PrimeFactorization hl = factorization.separateHighest().child().separateLowest().child();
            assertThat(lh == hl, is(true));
        }

    }

}
