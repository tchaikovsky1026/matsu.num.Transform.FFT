/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.convolution;

/**
 * 任意のデータサイズに適用可能な実数列の巡回畳み込み.
 * 
 * <p>
 * このインターフェースにおいて,
 * {@link #accepts(double[], double[])}
 * のreject条件は,
 * {@link CyclicConvolutionExecutor} と同等である.
 * </p>
 * 
 * <p>
 * <i><u>
 * このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
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
public interface GenericCyclicConvolutionExecutor extends CyclicConvolutionExecutor {

}
