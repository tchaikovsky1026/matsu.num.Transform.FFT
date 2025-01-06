/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.validation;

/**
 * <p>
 * 数列の構造が処理に対応しているかどうかを表すインターフェース.
 * </p>
 * 
 * <p>
 * 数列が対応していることは, シングルトンインスタンス {@link #ACCEPTED}
 * により表される. <br>
 * 対応していないことを表すインスタンスは, {@link StructureRejected} により構築することができる.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public abstract class StructureAcceptance {

    /**
     * 行列が処理に対応していることを表すシングルトン. <br>
     * acceptであることを判断する場合, "=="での評価が許される.
     */
    public static final StructureAcceptance ACCEPTED = new StructureAcceptance() {

        @Override
        public final Type type() {
            return Type.ACCEPTED;
        }

        @Override
        public final IllegalArgumentException getException() {
            throw new UnsupportedOperationException("acceptであるためこのメソッドを呼び出しを許可されていない.");
        }

        @Override
        public final String toString() {
            return this.type().toString();
        }
    };

    /**
     * パッケージプライベートのアクセス制限を持つコンストラクタ.
     */
    StructureAcceptance() {
        super();
    }

    /**
     * <p>
     * このインスタンスの属性を返す.
     * </p>
     * 
     * @return 属性
     */
    abstract Type type();

    /**
     * このインスタンスが「数列が対応していること」を表すかどうか,
     * すなわち {@link #ACCEPTED} に相当するかどうかを判定する.
     * 
     * @return 「数列が対応していること」を表す場合はtrue
     */
    public final boolean isAcceptState() {
        return this.type() == Type.ACCEPTED;
    }

    /**
     * <p>
     * このインスタンスの拒絶理由に適した例外インスタンスを取得する. <br>
     * {@code isAcceptState()==true} の場合は例外をスローする.
     * </p>
     * 
     * @return スローすべき例外
     * @throws UnsupportedOperationException acceptの場合
     */
    public abstract IllegalArgumentException getException();

    /**
     * 対応しているかどうかを示す列挙型.
     */
    enum Type {

        /**
         * 対応していることを表す. <br>
         * このタイプを有するのはシングルトン
         * {@link StructureAcceptance#ACCEPTED}
         * のみである.
         */
        ACCEPTED,

        /**
         * 対応していないことを表す.
         */
        REJECTED;
    }
}
