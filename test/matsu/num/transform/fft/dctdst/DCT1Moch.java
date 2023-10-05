/**
 * 2023.9.22
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;

/**
 * 実用的でない(低速な)DCT-1.
 * 
 * @author Matsuura Y.
 * @version 12.2
 */
public final class DCT1Moch implements DCT1Executor {

    @Override
    public double[] apply(double[] data) {
        return new DCT1Calc(data).calculate();
    }

    private static final class DCT1Calc {

        private final int entireLength;
        private final double[] data;

        DCT1Calc(double[] data) {
            if (data.length <= 1) {
                throw new IllegalArgumentException("長さが1以下");
            }

            this.data = data.clone();
            this.entireLength = data.length;

            this.data[0] *= 0.5;
            this.data[this.entireLength - 1] *= 0.5;
        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 2 * this.entireLength - 2;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int j_k_mod_2Nm2 = 0;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = Trigonometry.cospi(2d * j_k_mod_2Nm2 / Mod);
                    X_k += data[j] * cos;

                    j_k_mod_2Nm2 += k;
                    if (j_k_mod_2Nm2 > Mod) {
                        j_k_mod_2Nm2 -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
