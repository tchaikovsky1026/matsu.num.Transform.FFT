package matsu.num.transform.fft.validation;

import org.junit.Test;
import org.junit.Test.None;

/**
 * {@link DataSizeNotMismatchException} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
public class DataSizeNotMismatchExceptionTest {
    
    public static final Class<?> TEST_CLASS = DataSizeNotMismatchException.class;

    @Test(expected = None.class)
    public void test_メッセージにnullを渡すことができる() {
        new DataSizeNotMismatchException(null);
    }
}
