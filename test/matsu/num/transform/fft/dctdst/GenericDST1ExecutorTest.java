package matsu.num.transform.fft.dctdst;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Before;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link GenericDST1Executor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class GenericDST1ExecutorTest {

    public static final Class<?> TEST_CLASS = GenericDST1Executor.class;

    public static class 入力サイズの検証 {

        @Test(expected = IllegalArgumentException.class)
        public void test_入力サイズが0でIAEx() {
            GenericDST1Executor.instance().apply(new double[0]);
        }

        @Test(expected = None.class)
        public void test_入力サイズが1で正常() {
            GenericDST1Executor.instance().apply(new double[1]);
        }
    }

    public static class DST1検証_サイズ98 {

        private final int size = 98;

        private double[] data;

        @Before
        public void before_実数データを作成() {
            data = new double[size];

            Random random = ThreadLocalRandom.current();

            for (int i = 0; i < size; i++) {
                data[i] = random.nextDouble();
            }
        }

        @Test
        public void test_DST1の実行() {
            double[] result = GenericDST1Executor.instance().apply(data);
            double[] expected = new DST1Moch().apply(data);
            assertThat(result.length, is(expected.length));

            for (int i = 0; i < result.length; i++) {
                assertThat(result[i], is(closeTo(expected[i], 1E-12 * size)));
            }
        }
    }

    public static class toString表示の検証 {

        @Test
        public void test_toString表示() {
            System.out.println(TEST_CLASS.getName() + ":");
            System.out.println(GenericDST1Executor.instance());
            System.out.println();
        }
    }
}
