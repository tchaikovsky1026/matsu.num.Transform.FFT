/**
 * 2024.4.3
 */
package matsu.num.transform.fft.lib;

/**
 * <p>
 * このモジュール内での処理における, 三角関数の計算を扱う. <br>
 * 外部のモジュールからこのインターフェースのメソッドを呼ぶことは想定されていない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
public interface Trigonometry {

    /**
     * <p>
     * cos(<i>&pi;x</i>) の値を返す.
     * </p>
     * 
     * <p>
     * このインターフェースが適切に使用される限り, 
     * <i>x</i>に極端な与えられることはない.
     * </p>
     * 
     * @param x ラジアン単位での <i>x</i>
     * @return cos(<i>&pi;x</i>)
     */
    public abstract double cospi(double x);

    /**
     * <p>
     * sin(<i>&pi;x</i>) の値を返す.
     * </p>
     * 
     * <p>
     * このインターフェースが適切に使用される限り, 
     * <i>x</i>に極端な与えられることはない.
     * </p>
     *
     * @param x ラジアン単位での <i>x</i>
     * @return sin(<i>&pi;x</i>)
     */
    public abstract double sinpi(double x);
}
