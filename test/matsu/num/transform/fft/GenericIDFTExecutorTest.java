package matsu.num.transform.fft;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.fftmodule.RawDFTExecutor;

/**
 * {@link GenericIDFTExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class GenericIDFTExecutorTest {

    public static final Class<?> TEST_CLASS = GenericIDFTExecutor.class;

    public static class 入力サイズの検証 {

        @Test(expected = IllegalArgumentException.class)
        public void test_入力サイズが0でIAEx() {
            GenericIDFTExecutor.instance().apply(ComplexNumberArrayDTO.zeroFilledOf(0));
        }

        @Test(expected = None.class)
        public void test_入力サイズが1で正常() {
            GenericIDFTExecutor.instance().apply(ComplexNumberArrayDTO.zeroFilledOf(1));
        }
    }

    public static class IDFTの実行を検証_サイズ100 {

        private final int size = 100;

        private ComplexNumber[] data;
        private ComplexNumberArrayDTO dataArrayDto;

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

            dataArrayDto = ComplexNumberArrayDTO.zeroFilledOf(size);
            for (int i = 0; i < size; i++) {
                dataArrayDto.realPart[i] = real[i];
                dataArrayDto.imaginaryPart[i] = imaginary[i];
            }
        }

        @Test
        public void test_IDFTの実行() {
            FourierBasisComputer basisComputer = FourierBasisComputer.covering(data.length, FourierType.IDFT);
            ComplexNumberArrayDTO resultDto = GenericIDFTExecutor.instance().apply(dataArrayDto);
            ComplexNumber[] expected = RawDFTExecutor.instance().compute(data, basisComputer);
            assertThat(resultDto.size, is(expected.length));

            for (int i = 0; i < resultDto.size; i++) {
                assertThat(resultDto.realPart[i], is(closeTo(expected[i].real(), 1E-12 * size)));
                assertThat(resultDto.imaginaryPart[i], is(closeTo(expected[i].imaginary(), 1E-12 * size)));
            }
        }
    }

    public static class toString表示の検証 {

        @Test
        public void test_toString表示() {
            System.out.println(TEST_CLASS.getName() + ":");
            System.out.println(GenericIDFTExecutor.instance());
            System.out.println();
        }
    }

}
