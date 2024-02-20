/**
 * 2024.2.7
 */
package matsu.num.transform.fft.service;

import java.util.Objects;

import matsu.num.transform.fft.lib.Trigonometry;

/**
 * <p>
 * このモジュールで使うライブラリを管理する概念. <br>
 * イミュータブルである.
 * </p>
 * 
 * <p>
 * デフォルトインスタンスの生成は {@link #defaultImplemented()} で可能だが, 
 * その他はビルダを使用する.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public abstract class CommonLib {

    /**
     * 管理外での継承を禁止するために, パッケージプライベートにしている.
     */
    CommonLib() {
        super();
    }

    /**
     * 三角関数の計算を扱うライブラリを返す.
     * 
     * @return 三角関数の計算
     */
    public abstract Trigonometry trigonometry();

    /**
     * このライブラリの文字列表現.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return "CommonLib";
    }

    /**
     * デフォルトのライブラリが入った状態のビルダを返す.
     * 
     * @return デフォルトビルダ
     */
    public static CommonLib defaultImplemented() {
        return CommonLibDefaultHolder.DEFAULT_INSTANCE;
    }

    /**
     * <p>
     * {@link CommonLib} のイミュータブルビルダ.
     * </p>
     * 
     * <p>
     * ビルダ生成時には, デフォルトとなるライブラリがセットされている. <br>
     * 個別のライブラリに置き換える場合は専用のメソッドを用いる.
     * </p>
     */
    public static final class Builder {

        private static final Builder DEFAULT_BUILDER = new Builder();

        private Trigonometry trigonometry;

        /**
         * このビルダの状態でビルドされたインスタンス.
         * 遅延初期化される.
         */
        private volatile CommonLib build;

        /**
         * デフォルトライブラリを保持したビルダを構築する.
         */
        private Builder() {
            super();
            CommonLib defaultInstance = CommonLibDefaultHolder.DEFAULT_INSTANCE;
            this.trigonometry = defaultInstance.trigonometry();
        }

        /**
         * 内部から呼ばれるコピーコンストラクタ.
         * 
         * @param src ソース
         */
        private Builder(Builder src) {
            this.trigonometry = src.trigonometry;
        }

        /**
         * <p>
         * 自身の三角関数計算器を引数のものに置き換え, 新しいビルダインスタンスとして返す. <br>
         * メソッドチェーンが可能だが, 最後に呼び出し元で戻り値を受け取る必要がある.
         * </p>
         * 
         * @param newTrigonometry 三角関数計算器
         * @return 置き換え後の新しいビルダ
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        public Builder replacedTrigonometry(Trigonometry newTrigonometry) {
            Builder out = new Builder(this);
            out.trigonometry = Objects.requireNonNull(newTrigonometry);
            return out;
        }

        /**
         * {@link CommonLib} をビルドする.
         * 
         * @return ビルドされたインスタンス.
         */
        public CommonLib build() {
            CommonLib out = this.build;
            if (Objects.nonNull(out)) {
                return out;
            }

            out = new CommonLibImpl(this);
            this.build = out;
            return out;
        }

        /**
         * インスタンスの文字列表現.
         * 
         * @return 文字列表現
         */
        @Override
        public String toString() {
            return "LibBuilder";
        }

        /**
         * デフォルトのライブラリが入った状態を初期状態として, ビルダを返す.
         * 
         * @return 初期ビルダ
         */
        public static Builder implementedInit() {
            return DEFAULT_BUILDER;
        }

        /**
         * ビルダを用いて生成される {@link CommonLib} の具象クラス.
         */
        private static final class CommonLibImpl extends CommonLib {

            private final Trigonometry trigonometry;

            /**
             * ビルダから呼ばれる.
             */
            CommonLibImpl(Builder builder) {
                super();
                this.trigonometry = builder.trigonometry;
            }

            @Override
            public Trigonometry trigonometry() {
                return this.trigonometry;
            }
            
            @Override
            public String toString() {
                return "CommonLib(byBuilder)";
            }
        }
    }
}
