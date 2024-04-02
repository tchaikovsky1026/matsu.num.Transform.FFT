package matsu.num.transform.fft.service;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link DctDstTypes} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class DctDstTypesTest {

    public static final Class<?> TEST_CLASS = DctDstTypes.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            DctDstTypes.values().stream().forEach(System.out::println);
            System.out.println();
        }
    }
}
