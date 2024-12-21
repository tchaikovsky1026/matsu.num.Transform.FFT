/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.21
 */
package matsu.num.transform.fft.service;

import java.util.Objects;
import java.util.function.Function;

import matsu.num.transform.fft.FFTModuleExecutor;

/**
 * このモジュールが提供するエグゼキュータのタイプを表す.
 * 
 * @author Matsuura Y.
 * @version 22.1
 * @param <T> このタイプが返却するエグゼキュータの型
 */
public final class ExecutorType<T extends FFTModuleExecutor> {

    private final Class<T> executorClass;
    private final Function<FFTModuleExecutorProvider, T> getter;
    private final String typeName;

    /**
     * パッケージプライベートな唯一のコンストラクタ. <br>
     * Function::apply でnullが与えられることはない.
     * 
     * @param executorClass
     * @param getter
     * @param typeName
     */
    ExecutorType(String typeName, Class<T> executorClass, Function<FFTModuleExecutorProvider, T> getter) {
        this.executorClass = Objects.requireNonNull(executorClass);
        this.getter = Objects.requireNonNull(getter);
        this.typeName = Objects.requireNonNull(typeName);
    }

    /**
     * プロバイダからファクトリを生成する.
     * 
     * @param provider プロバイダ
     * @return ファクトリ
     */
    T get(FFTModuleExecutorProvider provider) {
        return this.getter.apply(provider);
    }

    /**
     * キャストするためのメソッド. <br>
     * このパッケージ内部から呼ばれ, 必ず成功することが想定されている.
     * 
     * @param obj (キャスト可能な)オブジェクト
     * @return キャストしたオブジェクト
     */
    T cast(Object obj) {
        return this.executorClass.cast(obj);
    }

    /**
     * このタイプの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return this.typeName;
    }
}
