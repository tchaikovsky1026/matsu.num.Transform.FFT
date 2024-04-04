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
 * 必要なデータサイズが無いことを通知する例外クラス.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public class NotRequiredDataSizeException extends IllegalArgumentException {

    private static final long serialVersionUID = -287619039759385607L;

    /**
     * メッセージ無しの例外インスタンスを構築する.
     */
    public NotRequiredDataSizeException() {
        super();
    }

    /**
     * メッセージ有りの例外インスタンスを構築する.
     * 
     * @param message メッセージ
     */
    public NotRequiredDataSizeException(String message) {
        super(message);
    }

}
