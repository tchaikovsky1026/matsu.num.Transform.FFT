/**
 * 2024.4.2
 */
package matsu.num.transform.fft.service.fuctionaltype;

import matsu.num.transform.fft.Executor;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.service.ExecutorType;

/**
 * このモジュールが提供するエグゼキュータタイプのmix-in.
 * 
 * @author Matsuura Y.
 * @version 19.0
 * @param <T> このタイプが返却するエグゼキュータの型
 */
public non-sealed interface FunctionalType<T extends Executor> extends ExecutorType<T> {

    /**
     * ライブラリを与えてエグゼキュータを生成する.
     * 
     * @param lib ライブラリ
     * @return エグゼキュータ
     */
    public T createExecutor(CommonLib lib);

    /**
     * エグゼキュータのクラス定義を取得する.
     * 
     * @return クラス定義
     */
    public Class<T> executorClass();
}
