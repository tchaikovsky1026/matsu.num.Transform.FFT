package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;

/**
 * 実用的でない(低速な)DST-3.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class DST3Moch extends LinearByScalingStability implements DST3Executor {

    private static final Trigonometry TRIGONOMETRY = TrigonometryForTesting.INSTANCE;

    public DST3Moch() {
        super(ArraysUtilForTesting.INSTANCE);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
    }

    @Override
    public double[] applyInner(double[] data) {
        return new DST3Calc(data).calculate();
    }

    private static final class DST3Calc {

        private final int entireLength;
        private final double[] data;

        DST3Calc(double[] data) {
            if (data.length == 1) {
                throw new IllegalArgumentException("長さが0");
            }

            this.data = data.clone();
            this.entireLength = data.length;

            this.data[this.entireLength - 1] *= 0.5;
        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 4 * this.entireLength;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int jp1_k2p1_mod_4N = 2 * k + 1;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = TRIGONOMETRY.sinpi(2d * jp1_k2p1_mod_4N / Mod);
                    X_k += data[j] * cos;

                    jp1_k2p1_mod_4N += 2 * k + 1;
                    if (jp1_k2p1_mod_4N > Mod) {
                        jp1_k2p1_mod_4N -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
