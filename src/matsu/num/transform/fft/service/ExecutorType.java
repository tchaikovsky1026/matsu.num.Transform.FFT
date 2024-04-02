/**
 * 2024.4.2
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.Executor;
import matsu.num.transform.fft.service.fuctionaltype.FunctionalType;

/**
 * このモジュールが提供するエグゼキュータのタイプ.
 * 
 * @author Matsuura Y.
 * @version 19.0
 * @param <T> このタイプが返却するエグゼキュータの型
 */
@SuppressWarnings("rawtypes")
public sealed interface ExecutorType<T extends Executor> permits FunctionalType {

}
