/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * このモジュールで実装された機能を外部向けに提供するパッケージ.
 * 
 * <p>
 * このパッケージでは各計算のエグゼキュータインスタンスを取得する仕組みが用意されている. <br>
 * エグゼキュータインスタンスの取得は
 * エグゼキュータプロバイダ
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider}
 * のインスタンスを経由して行われる. <br>
 * エグゼキュータインスタンスの取得までの流れは次の通りである.
 * </p>
 * 
 * <ol>
 * 
 * <li>
 * エグゼキュータプロバイダ
 * ({@link matsu.num.transform.fft.service.FFTModuleExecutorProvider})
 * インスタンスを取得する.
 * </li>
 * 
 * <li>
 * 取得したいエグゼキュータタイプ
 * ({@link matsu.num.transform.fft.service.ExecutorType})
 * インスタンスを用意する.
 * これは, 定数クラスとして用意されている. <br>
 * (詳細は
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider}
 * の説明文を参照.)
 * </li>
 * 
 * <li>
 * エグゼキュータプロバイダの
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider#get(ExecutorType)}
 * メソッドをコールし, エグゼキュータを取得する.
 * </li>
 * 
 * </ol>
 * 
 */
package matsu.num.transform.fft.service;
