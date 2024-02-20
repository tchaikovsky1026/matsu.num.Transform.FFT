/**
 * 2024.2.18
 */
package matsu.num.transform.fft.validation;

/**
 * データサイズが大きすぎることを通知する例外クラス.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public class DataSizeTooLargeException extends IllegalArgumentException {

    private static final long serialVersionUID = -7087063066249315744L;

    /**
     * メッセージ無しの例外インスタンスを構築する.
     */
    public DataSizeTooLargeException() {
        super();
    }

    /**
     * メッセージ有りの例外インスタンスを構築する.
     * 
     * @param message メッセージ
     */
    public DataSizeTooLargeException(String message) {
        super(message);
    }

}
