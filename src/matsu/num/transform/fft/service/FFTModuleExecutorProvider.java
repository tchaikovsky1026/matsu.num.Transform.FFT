/**
 * 2024.2.14
 */
package matsu.num.transform.fft.service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import matsu.num.transform.fft.Executor;

/**
 * このモジュール内で実装されているFFTモジュールエグゼキュータのプロバイダ.
 * 
 * <p>
 * このプロバイダの {@link #get(ExecutorType)} メソッドを呼ぶことで,
 * 対応するエグゼキュータを得ることができる. <br>
 * {@link ExecutorType} は {@link ExecutorTypes} 内に定数として提供されている.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class FFTModuleExecutorProvider {

    private static final FFTModuleExecutorProvider DEFAULT_INSTANCE =
            new FFTModuleExecutorProvider(CommonLib.defaultImplemented());

    private final CommonLib lib;

    private final Map<ExecutorType<?>, Object> map;

    //ロック用オブジェクト
    private final Object lock = new Object();

    /**
     * staticファクトリから呼ばれる非公開コンストラクタ.
     * 
     * @throws NullPointerException 引数にnullが含まれる場合.
     */
    private FFTModuleExecutorProvider(CommonLib lib) {
        this.lib = lib;
        this.map = new ConcurrentHashMap<>();
    }

    /**
     * 型を与えてエグゼキュータを取得する.
     * 
     * @param <R> エグゼキュータの型
     * @param type エグゼキュータタイプ
     * @return エグゼキュータ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public <R extends Executor> R get(ExecutorType<R> type) {
        Objects.requireNonNull(type);

        Class<R> executorClass = type.executorClass();
        Object out = this.map.get(type);
        if (Objects.nonNull(out)) {
            //このキャストは必ず成功する
            return executorClass.cast(out);
        }
        synchronized (this.lock) {
            out = this.map.get(type);
            if (Objects.nonNull(out)) {
                //このキャストは必ず成功する
                return executorClass.cast(out);
            }
            R castedObj = type.createExecutor(this.lib);
            this.map.put(type, castedObj);
            return castedObj;
        }
    }

    /**
     * このインスタンスの説明表現を返す.
     * 
     * @return 説明表現
     */
    @Override
    public String toString() {
        return String.format(
                "%s(%s)",
                this.getClass().getSimpleName(), this.lib);
    }

    /**
     * 与えられたライブラリを使用して処理を行う,
     * エグゼキュータプロバイダを返す.
     * 
     * @param lib ライブラリ
     * @return ライブラリを使用するエグゼキュータプロバイダ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static FFTModuleExecutorProvider by(CommonLib lib) {
        return new FFTModuleExecutorProvider(Objects.requireNonNull(lib));
    }

    /**
     * デフォルトライブラリを使用して処理を行う,
     * エグゼキュータプロバイダを返す.
     * 
     * @return デフォルトライブラリを使用するエグゼキュータプロバイダ
     */
    public static FFTModuleExecutorProvider byDefaultLib() {
        return DEFAULT_INSTANCE;
    }
}
