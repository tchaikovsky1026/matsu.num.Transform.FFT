package matsu.num.transform.fft.fftmodule;

import static matsu.num.transform.fft.NumberArrayDataCreator.*;
import static matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierBasisComputerSupplierDefaultHolder;
import matsu.num.transform.fft.component.FourierType;

/**
 * {@link Power2InnerFFTExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Power2InnerFFTExecutorTest {

    public static final Class<?> TEST_CLASS = Power2InnerFFTExecutor.class;

    private static final FourierBasisComputer.Supplier COMPUTER_SUPPLIER =
            FourierBasisComputerSupplierDefaultHolder.INSTANCE;

    private static final Power2InnerFFTExecutor POWER2_FFT_EXECUTOR = new Power2InnerFFTExecutor();
    private static final RawInnerDFTExecutor RAW_DFT_EXECUTOR = new RawInnerDFTExecutor();

    public static class 入力に関するテスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_2のべき乗でないならIAEx() {
            POWER2_FFT_EXECUTOR.compute(new ComplexNumber[25], COMPUTER_SUPPLIER.covering(25, FourierType.DFT));
        }

    }

    @RunWith(Theories.class)
    public static class FFT検証 {

        @DataPoint
        public static ComplexNumber[] data_size_2;
        @DataPoint
        public static ComplexNumber[] data_size_4;
        @DataPoint
        public static ComplexNumber[] data_size_8;
        @DataPoint
        public static ComplexNumber[] data_size_16;

        @BeforeClass
        public static void before_data_size_4_複素数のデータを作成() {
            data_size_4 = createComplexArrayData(4);
        }

        @BeforeClass
        public static void before_data_size_2_複素数のデータを作成() {
            data_size_2 = createComplexArrayData(2);
        }

        @BeforeClass
        public static void before_data_size_8_複素数のデータを作成() {
            data_size_8 = createComplexArrayData(8);
        }

        @BeforeClass
        public static void before_data_size_16_複素数のデータを作成() {
            data_size_16 = createComplexArrayData(16);
        }

        @Theory
        public void test_FFTの実行(ComplexNumber[] data) {
            FourierBasisComputer basisComputer = COMPUTER_SUPPLIER.covering(data.length, FourierType.DFT);
            double[][] resultArray = ComplexNumber.separateToArrays(
                    POWER2_FFT_EXECUTOR.compute(data, basisComputer));
            double[][] expectedArray = ComplexNumber.separateToArrays(
                    RAW_DFT_EXECUTOR.compute(data, basisComputer));

            double[] resReal = resultArray[0].clone();
            subtract(resReal, expectedArray[0]);
            double[] resImag = resultArray[1].clone();
            subtract(resImag, expectedArray[1]);

            double norm = Math.max(
                    normMax(expectedArray[0]), normMax(expectedArray[1]));
            double normRes = Math.max(
                    normMax(resReal), normMax(resImag));

            assertThat(normRes, is(lessThan(1E-12 * norm + 1E-100)));
        }
    }
}
