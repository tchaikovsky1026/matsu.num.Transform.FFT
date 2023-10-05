package matsu.num.transform.fft.fftmodule;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;

/**
 * {@link Power2FFTExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Power2FFTExecutorTest {

    public static final Class<?> TEST_CLASS = Power2FFTExecutor.class;

    public static class 入力に関するテスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_2のべき乗でないならIAEx() {
            Power2FFTExecutor.instance().compute(new ComplexNumber[25], FourierBasisComputer.covering(25, FourierType.DFT));
        }

    }

    public static class FFT検証_サイズ4 {

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = { 1, 2, 3, 3 };
            double[] imaginary = { 2, -1, 1, -1 };

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result =
                    Power2FFTExecutor.instance().compute(data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

    public static class FFT検証_サイズ2 {

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = { 1, 2 };
            double[] imaginary = { -1, 0 };

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result =
                    Power2FFTExecutor.instance().compute(data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

    public static class FFT検証_サイズ8 {

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = { 1, 2, 3, 2, 3, 3, 3, 3 };
            double[] imaginary = { -1, 0, 1, 0, 1, 1, -1, -1 };

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result =
                    Power2FFTExecutor.instance().compute(data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

    public static class FFT検証_サイズ16 {

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = {
                    1, 2, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1 };
            double[] imaginary = {
                    1, 1, 3, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1 };

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result =
                    Power2FFTExecutor.instance().compute(data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12)));
            }
        }
    }

}
