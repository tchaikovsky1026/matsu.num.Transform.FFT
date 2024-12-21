package matsu.num.transform.fft.service;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DftTypes} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class CyclicConvolutionTypesTest {

    public static final Class<?> TEST_CLASS = CyclicConvolutionTypes.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            CyclicConvolutionTypes.values().stream().forEach(System.out::println);
            System.out.println();
        }
    }
}
