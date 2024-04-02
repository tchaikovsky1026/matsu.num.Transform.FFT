package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * 実用的でない(低速な)DCT-3.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class DCT3Moch extends LinearByScalingStability implements DCT3Executor {

    private static final Trigonometry TRIGONOMETRY = TrigonometryForTesting.INSTANCE;

    public DCT3Moch() {
        super(MAX_DATA_SIZE, ArraysUtilForTesting.INSTANCE);
    }

    @Override
    public double[] applyInner(double[] data) {
        return new DCT3Calc(data).calculate();
    }

    private static final class DCT3Calc {

        private final int entireLength;
        private final double[] data;

        DCT3Calc(double[] data) {
            if (data.length == 1) {
                throw new IllegalArgumentException("長さが0");
            }

            this.data = data.clone();
            this.entireLength = data.length;

            this.data[0] *= 0.5;
        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 4 * this.entireLength;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int j_k2p1_mod_4N = 0;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = TRIGONOMETRY.cospi(2d * j_k2p1_mod_4N / Mod);
                    X_k += data[j] * cos;

                    j_k2p1_mod_4N += 2 * k + 1;
                    if (j_k2p1_mod_4N > Mod) {
                        j_k2p1_mod_4N -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
