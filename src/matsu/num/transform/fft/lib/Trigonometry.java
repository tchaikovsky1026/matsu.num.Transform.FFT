/**
 * 2024.2.7
 */
package matsu.num.transform.fft.lib;

/**
 * 三角関数を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public interface Trigonometry {

    /**
     * cos(&pi;x) を返す.
     *
     * @param x x
     * @return cos(&pi;x)
     */
    public abstract double cospi(double x);

    /**
     * sin(&pi;x) を返す.
     *
     * @param x x
     * @return sin(&pi;x)
     */
    public abstract double sinpi(double x);
}
