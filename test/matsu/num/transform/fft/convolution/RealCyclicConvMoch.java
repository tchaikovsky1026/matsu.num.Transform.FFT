/**
 * 2023.9.30
 */
package matsu.num.transform.fft.convolution;

import matsu.num.commons.ArraysUtil;

/**
 * 実用性の低い(低速な)実数の巡回畳み込みを扱う.
 * 
 * @author Matsuura Y.
 * @version 12.8
 */
public final class RealCyclicConvMoch implements RealNumbersCyclicConvolution {

    private static final RealNumbersCyclicConvolution INSTANCE = new RealCyclicConvMoch();

    private RealCyclicConvMoch() {

    }

    @Override
    public double[] apply(double[] f, double[] g) {
        int length = f.length;
        if (length != g.length) {
            throw new IllegalArgumentException("サイズが整合しない");
        }

        if (length == 0) {
            throw new IllegalArgumentException("サイズ0");
        }

        double[] h = new double[length];

        for (int i = 0; i < length; i++) {
            double[] g_shift = new double[length];
            for (int j = 0; j < length; j++) {

                int i_minus_j = i - j;
                if (i_minus_j < 0) {
                    i_minus_j += length;
                }
                g_shift[j] = g[i_minus_j];
            }
            h[i] = ArraysUtil.dot(f, g_shift);
        }

        return h;
    }

    public static RealNumbersCyclicConvolution instance() {
        return INSTANCE;
    }

}
