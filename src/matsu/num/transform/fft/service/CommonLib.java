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

import java.util.Objects;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * このモジュールで使うライブラリを管理する概念. <br>
 * イミュータブルである.
 * 
 * <p>
 * デフォルトインスタンスの生成は {@link #defaultImplemented()} で可能だが,
 * その他はビルダを使用する.
 * </p>
 * 
 * <p>
 * <u>
 * <i>コンストラクタが公開されていないので, 外部からの継承は不可.</i>
 * </u>
 * </p>
 * 
 * @author Matsuura Y.
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
    abstract Trigonometry trigonometry();

    /**
     * 配列に関する計算を扱うライブラリを返す.
     * 
     * @return 配列の計算
     */
    abstract ArraysUtil arrayUtil();

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
     * デフォルトライブラリを返す.
     * 
     * @return デフォルトライブラリ
     */
    public static CommonLib defaultImplemented() {
        return CommonLibDefaultHolder.DEFAULT_INSTANCE;
    }

    /**
     * {@link CommonLib} のイミュータブルビルダ.
     * 
     * <p>
     * 基本となるビルダインスタンスは, {@link #implementedInit()} により取得する. <br>
     * このビルダインスタンスにはデフォルトとなるライブラリがセットされている. <br>
     * 個別のライブラリに置き換える場合は専用のメソッドを用いる. <br>
     * ただし, ビルダインスタンスはイミュータブルであるため, 戻り値を受け取る必要がある
     * (ただし, メソッドチェーンは書ける).
     * </p>
     * 
     * <p>
     * 使用例は以下である
     * (メソッド名は適宜読み替える).
     * </p>
     * 
     * 
     * <blockquote>
     * 
     * <pre>
     * // 基本となるビルダインスタンスの取得
     * CommonLib.Builder builder =　CommonLib.Builder.implementedInit();　
     * // 個別のライブラリで置き換えた新しいビルダインスタンスを受け取る
     * builder = builder.replacedX(myXLibrary);
     * // ライブラリのビルド
     * CommonLib lib = builder.build();
     * </pre>
     * 
     * <pre>
     * // メソッドチェーンを用いたビルド
     * CommonLib lib = CommonLib.Builder.implementedInit()
     *         .replacedX(myXLibrary)
     *         .build();
     * </pre>
     * 
     * </blockquote>
     * 
     */
    public static final class Builder {

        private static final Builder DEFAULT_BUILDER = new Builder();

        private Trigonometry trigonometry;
        private ArraysUtil arraysUtil;

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
            this.arraysUtil = defaultInstance.arrayUtil();
        }

        /**
         * 内部から呼ばれるコピーコンストラクタ.
         * 
         * @param src ソース
         */
        private Builder(Builder src) {
            this.trigonometry = src.trigonometry;
            this.arraysUtil = src.arraysUtil;
        }

        /**
         * 自身の三角関数計算器を引数のものに置き換え, 新しいビルダインスタンスとして返す. <br>
         * メソッドチェーンが可能だが, 最後に呼び出し元で戻り値を受け取る必要がある.
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
            private final ArraysUtil arraysUtil;

            /**
             * ビルダから呼ばれる.
             */
            CommonLibImpl(Builder builder) {
                super();
                this.trigonometry = builder.trigonometry;
                this.arraysUtil = builder.arraysUtil;
            }

            @Override
            public Trigonometry trigonometry() {
                return this.trigonometry;
            }

            @Override
            ArraysUtil arrayUtil() {
                return this.arraysUtil;
            }

            @Override
            public String toString() {
                return "CommonLib(byBuilder)";
            }
        }
    }
}
