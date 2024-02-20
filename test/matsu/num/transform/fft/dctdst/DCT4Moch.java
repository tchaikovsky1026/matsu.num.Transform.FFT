/**
 * 2024.2.18
 */
package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.skeletal.LinearByScalingStability;

/**
 * 実用的でない(低速な)DCT-4.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
@Ignore
public final class DCT4Moch extends LinearByScalingStability implements DCT4Executor {

    private static final Trigonometry TRIGONOMETRY = CommonLib.defaultImplemented().trigonometry();

    public DCT4Moch() {
        super(MAX_DATA_SIZE);
    }

    @Override
    public double[] applyInner(double[] data) {
        return new DCT4Calc(data).calculate();
    }

    private static final class DCT4Calc {

        private final int entireLength;
        private final double[] data;

        DCT4Calc(double[] data) {
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

                int j2p1_k2p1_mod_8N = 2 * k + 1;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = TRIGONOMETRY.cospi(2d * j2p1_k2p1_mod_8N / Mod);
                    X_k += data[j] * cos;

                    j2p1_k2p1_mod_8N += 4 * k + 2;
                    if (j2p1_k2p1_mod_8N > Mod) {
                        j2p1_k2p1_mod_8N -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
