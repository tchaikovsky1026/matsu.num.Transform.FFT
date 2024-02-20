/**
 * 2024.2.18
 */
package matsu.num.transform.fft.validation;

/**
 * データサイズが仕様に整合しないことを通知する例外クラス.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public class DataSizeNotMismatchException extends IllegalArgumentException {

    private static final long serialVersionUID = 7438480880365201863L;

    /**
     * メッセージ無しの例外インスタンスを構築する.
     */
    public DataSizeNotMismatchException() {
        super();
    }

    /**
     * メッセージ有りの例外インスタンスを構築する.
     * 
     * @param message メッセージ
     */
    public DataSizeNotMismatchException(String message) {
        super(message);
    }

}
