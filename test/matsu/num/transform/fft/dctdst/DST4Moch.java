package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * 実用的でない(低速な)DST-4.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class DST4Moch extends LinearByScalingStability implements DST4Executor {

    private static final Trigonometry TRIGONOMETRY = TrigonometryForTesting.INSTANCE;

    public DST4Moch() {
        super(MAX_DATA_SIZE, ArraysUtilForTesting.INSTANCE);
    }

    @Override
    public double[] applyInner(double[] data) {
        return new DST4Calc(data).calculate();
    }

    private static final class DST4Calc {

        private final int entireLength;
        private final double[] data;

        DST4Calc(double[] data) {
            if (data.length == 1) {
                throw new IllegalArgumentException("長さが0");
            }

            this.data = data.clone();
            this.entireLength = data.length;

        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 8 * this.entireLength;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int j2p1_k2p1_mod_4N = 2 * k + 1;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = TRIGONOMETRY.sinpi(2d * j2p1_k2p1_mod_4N / Mod);
                    X_k += data[j] * cos;

                    j2p1_k2p1_mod_4N += 4 * k + 2;
                    if (j2p1_k2p1_mod_4N > Mod) {
                        j2p1_k2p1_mod_4N -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
