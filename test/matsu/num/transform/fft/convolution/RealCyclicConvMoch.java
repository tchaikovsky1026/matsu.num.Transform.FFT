package matsu.num.transform.fft.convolution;

import org.junit.Ignore;

import matsu.num.transform.fft.component.BiLinearByScalingStability;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilStaticForTestModule;

/**
 * 実用性の低い(低速な)実数の巡回畳み込みを扱う.
 */
@Ignore
public final class RealCyclicConvMoch
        extends BiLinearByScalingStability implements GenericCyclicConvolutionExecutor {

    private static final CyclicConvolutionExecutor INSTANCE = new RealCyclicConvMoch();

    private RealCyclicConvMoch() {
        super(ArraysUtilForTesting.INSTANCE);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
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
