package matsu.num.transform.fft.validation;

import org.junit.Test;
import org.junit.Test.None;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link NotRequiredDataSizeException} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class NotRequiredDataSizeExceptionTest {

    public static final Class<?> TEST_CLASS = NotRequiredDataSizeException.class;

    public static class 生成に関するテスト {

        @Test(expected = None.class)
        public void test_メッセージにnullを渡すことができる() {
            new NotRequiredDataSizeException(null);
        }
    }
}
