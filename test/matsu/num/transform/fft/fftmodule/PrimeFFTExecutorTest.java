package matsu.num.transform.fft.fftmodule;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;

/**
 * {@link PrimeFFTExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
public class PrimeFFTExecutorTest {

    public static final Class<?> TEST_CLASS = PrimeFFTExecutor.class;

    public static class FFT検証_サイズ13 {

        private final int size = 13;

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = new double[size];
            double[] imaginary = new double[size];

            Random random = ThreadLocalRandom.current();

            for (int i = 0; i < size; i++) {
                real[i] = random.nextDouble();
                imaginary[i] = random.nextDouble();
            }

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result = PrimeFFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12 * size)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12 * size)));
            }
        }
    }

    public static class FFT検証_サイズ829 {

        private final int size = 829;

        private ComplexNumber[] data;

        @Before
        public void before_複素数のデータを作成() {
            double[] real = new double[size];
            double[] imaginary = new double[size];

            Random random = ThreadLocalRandom.current();

            for (int i = 0; i < size; i++) {
                real[i] = random.nextDouble();
                imaginary[i] = random.nextDouble();
            }

            data = new ComplexNumber[real.length];
            for (int j = 0; j < real.length; j++) {
                data[j] = ComplexNumber.of(real[j], imaginary[j]);
            }
        }

        @Test
        public void test_FFTの実行() {
            ComplexNumber[] result = PrimeFFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(
                    data, FourierBasisComputer.covering(data.length, FourierType.DFT));
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i].real(), is(closeTo(expected[i].real(), 1E-12 * size)));
                assertThat(result[i].imaginary(), is(closeTo(expected[i].imaginary(), 1E-12 * size)));
            }
        }
    }

}
