package matsu.num.transform.fft.convolution;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link GenericCyclicConvolutionExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class GenericCyclicConvolutionExecutorTest {

    public static final Class<?> TEST_CLASS = GenericCyclicConvolutionExecutor.class;

    public static class 実数畳み込みテスト_サイズ5 {

        private double[] f;
        private double[] g;

        private double[] expected_conv_fg;

        @Before
        public void before_fgと素の畳み込みの準備() {
            f = new double[] { 1, 2, 3, 1, 3 };
            g = new double[] { 2, 3, 1, 2, 1 };
            expected_conv_fg = RealCyclicConvMoch.instance().apply(f, g);

        }

        @Test
        public void test() {
            double[] result = GenericCyclicConvolutionExecutor.instance().apply(f, g);
            for (int i = 0; i < f.length; i++) {
                assertThat(
                        result[i],
                        is(
                                closeTo(
                                        expected_conv_fg[i],
                                        1E-12 * Math.max(Math.abs(result[i]), Math.abs(expected_conv_fg[i])))));
            }
        }
    }

    public static class toString表示の検証 {

        @Test
        public void test_toString表示() {
            System.out.println(TEST_CLASS.getName() + ":");
            System.out.println(GenericCyclicConvolutionExecutor.instance());
            System.out.println();
        }
    }
}
