package matsu.num.transform.fft.convolution;

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
 * {@link Power2CyclicConvolutionExecutor}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Power2CyclicConvolutionExecutorTest {

    public static final Class<?> TEST_CLASS = Power2CyclicConvolutionExecutor.class;

    public static class 事前条件テスト {

        @Test(expected = IllegalArgumentException.class)
        public void test_2の累乗でない場合はIAEx() {
            Power2CyclicConvolutionExecutor.instance().apply(new double[5], new double[5]);
        }

        @Test(expected = None.class)
        public void test_1の場合は例外なし() {
            Power2CyclicConvolutionExecutor.instance().apply(new double[1], new double[1]);
        }
    }

    public static class 実数畳み込みテスト_サイズ8 {

        private int size = 8;

        private double[] f;
        private double[] g;

        private double[] expected_conv_fg;

        @Before
        public void before_fgと素の畳み込みの準備() {
            f = new double[size];
            g = new double[size];

            Random random = ThreadLocalRandom.current();
            for (int i = 0; i < size; i++) {
                f[i] = random.nextDouble();
                g[i] = random.nextDouble();
            }

            expected_conv_fg = RealCyclicConvMoch.instance().apply(f, g);

        }

        @Test
        public void test() {
            double[] result = Power2CyclicConvolutionExecutor.instance().apply(f, g);
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

}
