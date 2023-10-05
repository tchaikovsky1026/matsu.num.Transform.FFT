/**
 * 2023.9.28
 */
package matsu.num.transform.fft.fftmodule;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;

/**
 * 直接的な離散Fourier変換/逆変換を扱う. 
 * 
 * @author Matsuura Y.
 * @version 12.5
 */
public final class RawDFTExecutor {

    private static final InnerDFTExecutor INSTANCE = new RawDFTImpl();

    private RawDFTExecutor() {
        throw new AssertionError();
    }

    /**
     * このクラスの機能を実行するインスタンスを返す.
     * 
     * @return このクラスの機能を実行するインスタンス
     */
    public static InnerDFTExecutor instance() {
        return INSTANCE;
    }

    private static final class RawDFTImpl implements InnerDFTExecutor {

        @Override
        public ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer) {

            //ここでbasisComputerのサイズが整合しないことに対する例外をスロー
            FourierBasis basis = basisComputer.getBasis(data.length);

            int N = data.length;

            ComplexNumber[] A = new ComplexNumber[N];

            for (int k = 0; k < N; k++) {
                ComplexNumber[] w = new ComplexNumber[N];

                int jk_mod_N = 0;
                for (int j = 0; j < N; j++) {
                    w[j] = basis.valueAt(jk_mod_N);

                    jk_mod_N += k;
                    if (jk_mod_N >= N) {
                        jk_mod_N -= N;
                    }
                }

                A[k] = ComplexNumber.sumProduct(data, w);
            }

            return A;
        }
    }

}
