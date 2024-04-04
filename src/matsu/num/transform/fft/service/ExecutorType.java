/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.14
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.Executor;
import matsu.num.transform.fft.service.fuctionaltype.FunctionalType;

/**
 * このモジュールが提供するエグゼキュータのタイプを表す.
 * 
 * @author Matsuura Y.
 * @version 20.0
 * @param <T> このタイプが返却するエグゼキュータの型
 */
@SuppressWarnings("rawtypes")
public sealed interface ExecutorType<T extends Executor> permits FunctionalType {

}
