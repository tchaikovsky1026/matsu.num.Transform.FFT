package matsu.num.transform.fft.dctdst;

import org.junit.Ignore;

import matsu.num.transform.fft.component.LinearByScalingStability;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.TrigonometryForTesting;
import matsu.num.transform.fft.lib.privatelib.ArraysUtilForTesting;

/**
 * 実用的でない(低速な)DCT-4.
 * 
 * @author Matsuura Y.
 */
@Ignore
public final class DCT4Moch extends LinearByScalingStability implements GenericDCT4Executor {

    private static final Trigonometry TRIGONOMETRY = TrigonometryForTesting.INSTANCE;

    public DCT4Moch() {
        super(ArraysUtilForTesting.INSTANCE);

        this.dataSizeContract.bindUpperLimitSize(MAX_DATA_SIZE);
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
