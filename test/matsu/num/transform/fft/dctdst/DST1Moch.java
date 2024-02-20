/**
 * 2024.2.18
 */
package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * 実用的でない(低速な)DST-1.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
@Ignore
public final class DST1Moch extends LinearByScalingStability implements DST1Executor {

    private static final Trigonometry TRIGONOMETRY = CommonLib.defaultImplemented().trigonometry();

    public DST1Moch() {
        super(MAX_DATA_SIZE);
    }

    @Override
    public double[] applyInner(double[] data) {
        return new DST1Calc(data).calculate();
    }

    private static final class DST1Calc {

        private final int entireLength;
        private final double[] data;

        DST1Calc(double[] data) {
            this.data = data.clone();
            this.entireLength = data.length;
        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 2 * this.entireLength + 2;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int j1_k1_mod_2Np2 = k + 1;
                for (int j = 0; j < this.entireLength; j++) {
                    double sin = TRIGONOMETRY.sinpi(2d * j1_k1_mod_2Np2 / Mod);
                    X_k += data[j] * sin;

                    j1_k1_mod_2Np2 += k + 1;
                    if (j1_k1_mod_2Np2 > Mod) {
                        j1_k1_mod_2Np2 -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }
}
