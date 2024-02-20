package matsu.num.transform.fft.service;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link ExecutorTypes} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ExecutorTypesTest {

    public static final Class<?> TEST_CLASS = ExecutorTypes.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            ExecutorTypes.values().stream().forEach(System.out::println);
            System.out.println();
        }
    }
}
