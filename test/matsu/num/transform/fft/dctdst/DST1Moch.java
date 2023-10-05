/**
 * 2023.9.11
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;

/**
 * 実用的でない(低速な)DST-1.
 * 
 * @author Matsuura Y.
 * @version 12.0
 */
public final class DST1Moch implements DST1Executor {

    public DST1Moch() {

    }

    @Override
    public double[] apply(double[] data) {
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
                    double sin = Trigonometry.sinpi(2d * j1_k1_mod_2Np2 / Mod);
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
