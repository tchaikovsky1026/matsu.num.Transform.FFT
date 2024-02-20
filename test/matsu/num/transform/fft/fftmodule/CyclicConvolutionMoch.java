/**
 * 2024.2.19
 */
package matsu.num.transform.fft.fftmodule;

import org.junit.Ignore;

import matsu.num.transform.fft.component.ComplexNumber;

/**
 * 実用性の低い(低速な)巡回畳み込みを扱う.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
@Ignore
public final class CyclicConvolutionMoch {

    private final int length;

    private final ComplexNumber[] f;
    private final ComplexNumber[] g;

    public CyclicConvolutionMoch(ComplexNumber[] f, ComplexNumber[] g) {
        if (f.length != g.length) {
            throw new IllegalArgumentException("サイズが異なる");
        }
        this.length = f.length;
        this.f = f.clone();
        this.g = g.clone();
    }

    public ComplexNumber[] computeConvolution() {
        ComplexNumber[] h = new ComplexNumber[this.length];

        for (int i = 0; i < this.length; i++) {
            ComplexNumber[] g_shift = new ComplexNumber[this.length];
            for (int j = 0; j < this.length; j++) {

                int i_minus_j = i - j;
                if (i_minus_j < 0) {
                    i_minus_j += this.length;
                }
                g_shift[j] = this.g[i_minus_j];
            }
            h[i] = ComplexNumber.sumProduct(this.f, g_shift);
        }

        return h;
    }
}
