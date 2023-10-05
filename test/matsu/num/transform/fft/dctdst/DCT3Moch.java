/**
 * 2023.9.25
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;

/**
 * 実用的でない(低速な)DCT-3.
 * 
 * @author Matsuura Y.
 * @version 12.3
 */
public final class DCT3Moch implements DCT3Executor {

    @Override
    public double[] apply(double[] data) {
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
                    double cos = Trigonometry.cospi(2d * j_k2p1_mod_4N / Mod);
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
