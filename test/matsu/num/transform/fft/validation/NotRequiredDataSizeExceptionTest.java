package matsu.num.transform.fft.validation;

import org.junit.Test;
import org.junit.Test.None;

/**
 * {@link NotRequiredDataSizeException} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
public class NotRequiredDataSizeExceptionTest {
    
    public static final Class<?> TEST_CLASS = NotRequiredDataSizeException.class;

    @Test(expected = None.class)
    public void test_メッセージにnullを渡すことができる() {
        new NotRequiredDataSizeException(null);
    }
}
