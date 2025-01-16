/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.21
 */
package matsu.num.transform.fft.lib;

/**
 * 三角関数の計算ライブラリを表す.
 * 
 * <p>
 * このインターフェースは, この乱数生成モジュール内で必要となる計算ロジックを外部から注入するために実装される. <br>
 * インターフェースを実装した具象クラスのインスタンスは, 最終的に
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider}
 * に注入され, このモジュール内の基本計算を担う.
 * </p>
 * 
 * 
 * <hr>
 * 
 * <h2>使用について</h2>
 * 
 * 
 * <h3>実装規約</h3>
 * 
 * <p>
 * この {@link Trigonometry} インターフェース実行する三角関数の計算は,
 * {@link java.lang.Math} に即した結果が返されることが期待される. <br>
 * ただし, その精度はおおよその倍精度であれば良い. <br>
 * そのため, {@link java.lang.Math} により計算してもよいが,
 * より高速な計算ライブラリを用いてもよい.
 * </p>
 * 
 * <p>
 * 数学関数の計算であるため定義域については厳格であり,
 * {@link Double#NaN} を返さないことは極めて重要である. <br>
 * ただし, 定義域の区間端の極限が有限値 or +&infin; or -&infin; になる場合において,
 * 区間端の付近でそれらを返すことは問題ない.
 * </p>
 * 
 * 
 * <h3><u><i>非推奨：外部からのメソッドコール</i></u></h3>
 * 
 * <p>
 * このインターフェースはインスタンスの注入のための型を定義しているだけであり,
 * 外部のモジュールからこのインターフェースのメソッドをコールすることは想定されていない. <br>
 * これに違反した方法で使用することは, 強く非推奨である.
 * </p>
 * 
 * @author Matsuura Y.
 */
public interface Trigonometry {

    /**
     * cos(<i>&pi;x</i>) の値を返す.
     * 
     * @param x ラジアン単位での <i>x</i>
     * @return cos(<i>&pi;x</i>)
     */
    public abstract double cospi(double x);

    /**
     * sin(<i>&pi;x</i>) の値を返す.
     *
     * @param x ラジアン単位での <i>x</i>
     * @return sin(<i>&pi;x</i>)
     */
    public abstract double sinpi(double x);
}
