/**
 * 2024.4.2
 */
package matsu.num.transform.fft.service.fuctionaltype;

import java.util.Objects;
import java.util.function.Function;

import matsu.num.transform.fft.Executor;
import matsu.num.transform.fft.service.CommonLib;

/**
 * このモジュールが提供するエグゼキュータのタイプ.
 * 
 * @author Matsuura Y.
 * @version 19.0
 * @param <T> このタイプが返却するエグゼキュータの型,
 *            {@link CommonLib} 以外の状態を持たないことが保証されている.
 */
public final class FunctionalTypeImpl<T extends Executor> implements FunctionalType<T> {

    private final Class<T> executorClass;
    private final Function<CommonLib, T> getter;
    private final String typeName;

    /**
     * パッケージプライベートな唯一のコンストラクタ. <br>
     * Function::apply でnullが与えられることはない.
     * @param executorClass 
     * @param getter 
     * @param typeName 
     */
    public FunctionalTypeImpl(Class<T> executorClass, Function<CommonLib, T> getter, String typeName) {
        this.executorClass = Objects.requireNonNull(executorClass);
        this.getter = Objects.requireNonNull(getter);
        this.typeName = Objects.requireNonNull(typeName);
    }

    @Override
    public T createExecutor(CommonLib lib) {
        Objects.requireNonNull(lib);
        return this.getter.apply(lib);
    }

    @Override
    public Class<T> executorClass() {
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
