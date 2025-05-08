/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.5.8
 */
package matsu.num.transform.fft;

/**
 * このモジュールが扱う計算のエグゼキュータであることを表明するマーカーインターフェース.
 * 
 * <p>
 * このインターフェースのサブタイプは実質的にイミュータブルであり,
 * すべてのメソッドは副作用無し,
 * スレッドセーフ, 参照透過であることが保証される.
 * </p>
 * 
 * <p>
 * <i><u>
 * このインターフェース, およびサブインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * {@code instanceof} 比較による分岐は危険である.
 * </u></i>
 * </p>
 * 
 * @implSpec
 *               このインターフェースをモジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 */
public interface FFTModuleExecutor {

}
