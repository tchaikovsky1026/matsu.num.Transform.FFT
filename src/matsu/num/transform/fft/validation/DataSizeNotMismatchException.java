/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.validation;

/**
 * データサイズが仕様に整合しないことを通知する例外クラス.
 * 
 * @author Matsuura Y.
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
