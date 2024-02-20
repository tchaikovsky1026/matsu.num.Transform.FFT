/**
 * 2024.2.14
 */
package matsu.num.transform.fft.service;

import java.util.Objects;
import java.util.function.Function;

import matsu.num.transform.fft.Executor;

/**
 * このモジュールが提供するエグゼキュータのタイプ.
 * 
 * @author Matsuura Y.
 * @version 18.0
 * @param <T> このタイプが返却するエグゼキュータの型,
 *            {@link CommonLib} 以外の状態を持たないことが保証されている.
 */
public final class ExecutorType<T extends Executor> {

    private final Class<T> executorClass;
    private final Function<CommonLib, T> getter;
    private final String typeName;

    /**
     * パッケージプライベートな唯一のコンストラクタ. <br>
     * Function::apply でnullが与えられることはない.
     */
    ExecutorType(Class<T> executorClass, Function<CommonLib, T> getter, String typeName) {
        this.executorClass = Objects.requireNonNull(executorClass);
        this.getter = Objects.requireNonNull(getter);
        this.typeName = Objects.requireNonNull(typeName);
    }

    /**
     * ライブラリを与えてエグゼキュータを生成する.
     * 
     * @param lib ライブラリ
     * @return エグゼキュータ
     */
    T createExecutor(CommonLib lib) {
        Objects.requireNonNull(lib);
        return this.getter.apply(lib);
    }

    /**
     * エグゼキュータのクラス定義を取得する.
     * 
     * @return クラス定義
     */
    Class<T> executorClass() {
        return this.executorClass;
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
