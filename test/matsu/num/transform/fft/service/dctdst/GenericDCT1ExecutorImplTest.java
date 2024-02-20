package matsu.num.transform.fft.service.dctdst;

import static matsu.num.transform.fft.NumberArrayDataCreator.*;
import static matsu.num.transform.fft.lib.privatelib.ArraysUtilForTestModule.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.dctdst.DCT1Executor;
import matsu.num.transform.fft.dctdst.DCT1Moch;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.service.ExecutorTypes;
import matsu.num.transform.fft.service.FFTModuleExecutorProvider;
import matsu.num.transform.fft.validation.NotRequiredDataSizeException;

/**
 * {@link GenericDCT1ExecutorImpl}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class GenericDCT1ExecutorImplTest {

    public static final Class<?> TEST_CLASS = GenericDCT1ExecutorImpl.class;

    private static final GenericDCT1ExecutorImpl EXECUTOR_NEW =
            new GenericDCT1ExecutorImpl(CommonLib.defaultImplemented());

    public static class 入力サイズの検証 {

        @Test(expected = NotRequiredDataSizeException.class)
        public void test_入力サイズが1でNRDSEx() {
            EXECUTOR_NEW.apply(new double[1]);
        }

        @Test(expected = None.class)
        public void test_入力サイズが2で正常() {
            EXECUTOR_NEW.apply(new double[2]);
        }
    }

    @RunWith(Theories.class)
    public static class DCT1検証 {

        @DataPoint
        public static final DCT1Executor executorNew = EXECUTOR_NEW;

        @DataPoint
        public static final DCT1Executor executorCall =
                FFTModuleExecutorProvider.byDefaultLib().get(ExecutorTypes.DCTDST.GENERIC_DCT1_EXECUTOR);

        @DataPoint
        public static double[] data1;
        @DataPoint
        public static double[] data2;

        @BeforeClass
        public static void before_data1_サイズ10() {
            data1 = createArrayData(10);
        }

        @BeforeClass
        public static void before_data2_サイズ49() {
            data2 = createArrayData(49);
        }

        @Theory
        public void test_DCT1の実行(DCT1Executor executor, double[] data) {
            double[] result = executor.apply(data);
            double[] expected = new DCT1Moch().apply(data);

            double[] res = result.clone();
            subtract(res, expected);
            double norm = normMax(expected);
            double normRes = normMax(res);

            assertThat(normRes, is(lessThan(1E-12 * norm + 1E-100)));
        }
    }

    public static class toString表示の検証 {

        @Test
        public void test_toString表示() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(EXECUTOR_NEW);
            System.out.println();
        }
    }
}
