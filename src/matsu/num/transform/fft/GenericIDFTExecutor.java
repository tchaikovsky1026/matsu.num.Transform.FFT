/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft;

import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;

/**
 * 任意のデータサイズに適用可能なIDFT.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(ComplexNumberArrayDTO)}
 * のreject条件は,
 * {@link IDFTExecutor} と同等である. <br>
 * このインターフェースのサブタイプではこれ以上reject条件を緩めて (accept条件を強めて) いないことを保証する.
 * </p>
 * 
 * <p>
 * <i><u>
 * このインターフェース, およびサブインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * モジュールの外部で実装することを禁止する.
 * </u></i>
 * </p>
 * 
 * @author Matsuura Y.
 * @deprecated
 *                 このインターフェースは version 25 以降削除予定である. <br>
 *                 スーパーインターフェースにより取り扱うように変更すべきである.
 */
@Deprecated(forRemoval = true)
public interface GenericIDFTExecutor extends IDFTExecutor {

}
