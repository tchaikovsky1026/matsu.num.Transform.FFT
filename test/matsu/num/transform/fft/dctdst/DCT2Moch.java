/**
 * 2023.9.25
 */
package matsu.num.transform.fft.dctdst;

import matsu.num.commons.Trigonometry;

/**
 * 実用的でない(低速な)DCT-2.
 * 
 * @author Matsuura Y.
 * @version 12.3
 */
public final class DCT2Moch implements DCT2Executor {

    @Override
    public double[] apply(double[] data) {
        return new DCT2Calc(data).calculate();
    }

    private static final class DCT2Calc {

        private final int entireLength;
        private final double[] data;

        DCT2Calc(double[] data) {
            if (data.length == 1) {
                throw new IllegalArgumentException("長さが0");
            }

            this.data = data.clone();
            this.entireLength = data.length;
        }

        double[] calculate() {

            double[] X = new double[this.entireLength];

            int Mod = 4 * this.entireLength;

            for (int k = 0; k < this.entireLength; k++) {
                double X_k = 0;

                int j2p1_k_mod_4N = k;
                for (int j = 0; j < this.entireLength; j++) {
                    double cos = Trigonometry.cospi(2d * j2p1_k_mod_4N / Mod);
                    X_k += data[j] * cos;

                    j2p1_k_mod_4N += 2 * k;
                    if (j2p1_k_mod_4N > Mod) {
                        j2p1_k_mod_4N -= Mod;
                    }
                }

                X[k] = X_k;
            }

            return X;
        }
    }

}
