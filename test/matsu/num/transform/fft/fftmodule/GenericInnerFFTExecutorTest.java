package matsu.num.transform.fft.fftmodule;

import static matsu.num.transform.fft.NumberArrayDataCreator.*;
import static matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
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
 * {@link GenericInnerFFTExecutor}クラスのテスト.
 */
@RunWith(Enclosed.class)
final class GenericInnerFFTExecutorTest {

    public static final Class<?> TEST_CLASS = GenericInnerFFTExecutor.class;

    private static final FourierBasisComputer.Supplier COMPUTER_SUPPLIER =
            FourierBasisComputerSupplierDefaultHolder.INSTANCE;

    private static final GenericInnerFFTExecutor GENERIC_INNER_FFT_EXECUTOR =
            new GenericInnerFFTExecutor(COMPUTER_SUPPLIER);
    private static final RawInnerDFTExecutor RAW_DFT_EXECUTOR = new RawInnerDFTExecutor();

    @RunWith(Theories.class)
    public static class FFT検証 {

        @DataPoint
        public static ComplexNumber[] data_size_8;
        @DataPoint
        public static ComplexNumber[] data_size_100;
        @DataPoint
        public static ComplexNumber[] data_size_2_41_43;

        @BeforeClass
        public static void before_data_size_8_ノコギリ波のデータを作成() {
            data_size_8 = createComplexArrayData(8);
        }

        @BeforeClass
        public static void before_data_size_100_三角波のデータを作成() {
            data_size_100 = createComplexArrayData(100);
        }

        @BeforeClass
        public static void before_data_size_2_41_43_三角波のデータを作成() {
            data_size_2_41_43 = createComplexArrayData(2 * 41 * 43);
        }

        @Theory
        public void test_FFTの実行(ComplexNumber[] data) {
            FourierBasisComputer basisComputer = COMPUTER_SUPPLIER.covering(data.length, FourierType.DFT);
            double[][] resultArray = ComplexNumber.separateToArrays(
                    GENERIC_INNER_FFT_EXECUTOR.compute(data, basisComputer));
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
