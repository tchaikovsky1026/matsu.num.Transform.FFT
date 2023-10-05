package matsu.num.transform.fft.fftmodule;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;

/**
 * {@link CyclicConvolutionModule}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class CyclicConvolutionModuleTest {

    public static final Class<?> TEST_CLASS = CyclicConvolutionModule.class;

    public static class 畳み込み検証_サイズ4 {

        private ComplexNumber[] f;
        private ComplexNumber[] g;

        @Before
        public void before_複素数列fのデータを作成() {
            double[] real = { 1, 2, 3, 3 };
            double[] imaginary = { 2, -1, 1, -1 };

            f = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                f[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Before
        public void before_複素数列gのデータを作成() {
            double[] real = { 1, 0, 3, 3 };
            double[] imaginary = { 2, 1, 1, -1 };

            g = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                g[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_畳み込みの実行() {
            ComplexNumber[] result = CyclicConvolutionModule.instance().compute(f, g);
            ComplexNumber[] expected = new CyclicConvolutionMoch(f, g).computeConvolution();
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

    public static class 畳み込み検証_サイズ7 {

        private ComplexNumber[] f;
        private ComplexNumber[] g;

        @Before
        public void before_複素数列fのデータを作成() {
            double[] real = { 1, 2, 3, 3, 1, 1, 2 };
            double[] imaginary = { 2, -1, 1, -1, -1, 1, 2 };

            f = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                f[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Before
        public void before_複素数列gのデータを作成() {
            double[] real = { 1, 0, 3, 3, 0, 2, 1 };
            double[] imaginary = { 2, 1, 1, -1, 4, -1, 1 };

            g = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                g[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_畳み込みの実行() {
            ComplexNumber[] result = CyclicConvolutionModule.instance().compute(f, g);
            ComplexNumber[] expected = new CyclicConvolutionMoch(f, g).computeConvolution();
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

}
