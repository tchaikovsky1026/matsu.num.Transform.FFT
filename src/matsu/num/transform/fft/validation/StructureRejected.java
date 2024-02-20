/**
 * 2024.2.16
 */
package matsu.num.transform.fft.validation;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 拒絶を表す {@link StructureAcceptance} を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class StructureRejected extends StructureAcceptance {

    private final Supplier<IllegalArgumentException> exceptionGetter;
    private final String explanation;

    /**
     * 
     * @param exceptionGetter
     * @param explanation 説明
     * @throws IllegalArgumentException 説明がブランクの場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    private StructureRejected(Supplier<IllegalArgumentException> exceptionGetter, String explanation) {
        this.exceptionGetter = Objects.requireNonNull(exceptionGetter);
        if (explanation.isBlank()) {
            throw new IllegalArgumentException("説明がブランク");
        }
        this.explanation = explanation;
    }

    /**
     * rejectを返す.
     */
    @Override
    Type type() {
        return Type.REJECTED;
    }

    /**
     * このインスタンスの拒絶理由に適した例外インスタンスを取得する.
     * 空でない.
     * 
     * @return スローすべき例外, 空でない
     */
    @Override
    public IllegalArgumentException getException() {
        return this.exceptionGetter.get();
    }

    /**
     * このインスタンスの文字列表現
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return this.explanation;
    }

    /**
     * 拒絶根拠となる例外で特徴づけられた, 拒絶インスタンスを構築する.
     * 
     * @param exceptionGetter 例外インスタンスの生成器
     * @param explanation 説明
     * @return 拒絶インスタンス
     * @throws IllegalArgumentException 説明がブランクの場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static StructureRejected by(Supplier<IllegalArgumentException> exceptionGetter, String explanation) {
        return new StructureRejected(exceptionGetter, explanation);
    }
}
