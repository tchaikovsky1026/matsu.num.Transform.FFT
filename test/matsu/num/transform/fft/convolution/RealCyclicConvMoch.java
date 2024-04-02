package matsu.num.transform.fft.convolution;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule;
import matsu.num.transform.fft.skeletal.BiLinearByScalingStability;

/**
 * 実用性の低い(低速な)実数の巡回畳み込みを扱う.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class RealCyclicConvMoch
        extends BiLinearByScalingStability implements CyclicConvolutionExecutor {

    private static final CyclicConvolutionExecutor INSTANCE = new RealCyclicConvMoch();

    private RealCyclicConvMoch() {
        super(MAX_DATA_SIZE, ArraysUtilForTesting.INSTANCE);
    }

    @Override
    protected double[] applyInner(double[] f, double[] g) {
        int length = f.length;

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
            h[i] = ArraysUtilStaticForTestModule.dot(f, g_shift);
        }

        return h;
    }

    public static CyclicConvolutionExecutor instance() {
        return INSTANCE;
    }
}
