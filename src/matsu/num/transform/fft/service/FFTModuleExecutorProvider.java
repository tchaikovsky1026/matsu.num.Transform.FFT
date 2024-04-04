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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import matsu.num.transform.fft.Executor;
import matsu.num.transform.fft.service.fuctionaltype.FunctionalType;

/**
 * <p>
 * このモジュール内で実装されているエグゼキュータのプロバイダ.
 * </p>
 * 
 * <p>
 * まずこのプロバイダを生成するには, 共通ライブラリ
 * ({@link CommonLib})
 * の準備が必要である. <br>
 * デフォルトのライブラリを使う場合は
 * {@link #byDefaultLib()} をコールすればよく,
 * ユーザーが準備し多ライブラリを使う場合は
 * {@link #by(CommonLib)} をコールする.
 * </p>
 * 
 * <p>
 * このプロバイダの {@link #get(ExecutorType)} メソッドを呼ぶことで,
 * 対応するエグゼキュータを得ることができる. <br>
 * {@link ExecutorType} は次のクラス内に定数として提供されている.
 * </p>
 * 
 * <ul>
 * <li>{@link DftTypes}</li>
 * <li>{@link DctDstTypes}</li>
 * <li>{@link CyclicConvolutionTypes}</li>
 * </ul>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class FFTModuleExecutorProvider {

    private static final FFTModuleExecutorProvider DEFAULT_INSTANCE =
            new FFTModuleExecutorProvider(CommonLib.defaultImplemented());

    private final CommonLib lib;

    private final Map<FunctionalType<?>, Object> map;

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
        FunctionalType<R> functionalType = (FunctionalType<R>) type;

        Class<R> executorClass = functionalType.executorClass();
        Object out = this.map.get(functionalType);
        if (Objects.nonNull(out)) {
            //このキャストは必ず成功する
            return executorClass.cast(out);
        }
        synchronized (this.lock) {
            out = this.map.get(functionalType);
            if (Objects.nonNull(out)) {
                //このキャストは必ず成功する
                return executorClass.cast(out);
            }
            R castedObj = functionalType.createExecutor(this.lib);
            this.map.put(functionalType, castedObj);
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
