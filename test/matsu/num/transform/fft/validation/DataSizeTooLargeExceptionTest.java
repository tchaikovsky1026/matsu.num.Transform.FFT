package matsu.num.transform.fft.validation;

import org.junit.Test;
import org.junit.Test.None;

/**
 * {@link DataSizeTooLargeException} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
public class DataSizeTooLargeExceptionTest {

    public static final Class<?> TEST_CLASS = DataSizeTooLargeException.class;

    @Test(expected = None.class)
    public void test_メッセージにnullを渡すことができる() {
        new DataSizeTooLargeException(null);
    }
}
