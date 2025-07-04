/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.21
 */
package matsu.num.transform.fft.service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import matsu.num.transform.fft.FFTModuleExecutor;

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
 * <li>{@link DftExecutors}</li>
 * <li>{@link DctDstExecutors}</li>
 * <li>{@link CyclicConvolutionExecutors}</li>
 * </ul>
 * 
 * @author Matsuura Y.
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
    public <R extends FFTModuleExecutor> R get(ExecutorType<R> type) {
        Objects.requireNonNull(type);

        Object out = this.map.get(type);
        if (Objects.nonNull(out)) {
            //このキャストは必ず成功する
            return type.cast(out);
        }
        synchronized (this.lock) {
            out = this.map.get(type);
            if (Objects.nonNull(out)) {
                //このキャストは必ず成功する
                return type.cast(out);
            }
            R castedObj = type.get(this);
            this.map.put(type, castedObj);
            return castedObj;
        }
    }

    /**
     * このプロバイダに紐づけられているライブラリを返す.
     * 
     * @return このプロバイダが紐づくライブラリ
     */
    public CommonLib lib() {
        return this.lib;
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
