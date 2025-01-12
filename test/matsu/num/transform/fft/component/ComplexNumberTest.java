/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.component;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link ComplexNumber}クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ComplexNumberTest {

    public static final Class<?> TEST_CLASS = ComplexNumber.class;

    public static class 和のテスト {

        @Test
        public void test_パターン1() {
            ComplexNumber c1 = ComplexNumber.of(1, 2);
            ComplexNumber c2 = ComplexNumber.of(2, -1);

            ComplexNumber sum = c1.plus(c2);

            assertThat(sum.real(), is(closeTo(3, 1E-12)));
            assertThat(sum.imaginary(), is(closeTo(1, 1E-12)));
        }
    }

    public static class 積のテスト {

        @Test
        public void test_パターン1() {
            ComplexNumber c1 = ComplexNumber.of(1, 2);
            ComplexNumber c2 = ComplexNumber.of(2, -1);

            ComplexNumber prod = c1.times(c2);

            assertThat(prod.real(), is(closeTo(4, 1E-12)));
            assertThat(prod.imaginary(), is(closeTo(3, 1E-12)));
        }
    }

    public static class 総和のテスト {

        @Test
        public void test_パターン1() {
            ComplexNumber c1 = ComplexNumber.of(1, 2);
            ComplexNumber c2 = ComplexNumber.of(2, -1);
            ComplexNumber c3 = ComplexNumber.of(1, -2);

            ComplexNumber sum = ComplexNumber.sum(c1, c2, c3);

            assertThat(sum.real(), is(closeTo(4, 1E-12)));
            assertThat(sum.imaginary(), is(closeTo(-1, 1E-12)));
        }
    }

    public static class 積の総和のテスト {

        @Test
        public void test_パターン1() {
            ComplexNumber a1 = ComplexNumber.of(1, 2);
            ComplexNumber a2 = ComplexNumber.of(2, -1);
            ComplexNumber a3 = ComplexNumber.of(1, -2);

            ComplexNumber b1 = ComplexNumber.of(3, 2);
            ComplexNumber b2 = ComplexNumber.of(-1, -1);
            ComplexNumber b3 = ComplexNumber.of(1, 2);

            ComplexNumber[] a = { a1, a2, a3 };
            ComplexNumber[] b = { b1, b2, b3 };

            ComplexNumber rawSumProd = ComplexNumber.sum(a1.times(b1), a2.times(b2), a3.times(b3));
            ComplexNumber sumProd = ComplexNumber.sumProduct(a, b);

            assertThat(sumProd.real(), is(closeTo(rawSumProd.real(), 1E-12)));
            assertThat(sumProd.imaginary(), is(closeTo(rawSumProd.imaginary(), 1E-12)));
        }
    }
}
