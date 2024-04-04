/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.validation;

/**
 * データサイズが大きすぎることを通知する例外クラス.
 * 
 * @author Matsuura Y.
 * @version 20.0
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
