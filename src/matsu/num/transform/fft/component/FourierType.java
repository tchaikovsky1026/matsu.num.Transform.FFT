/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.component;

/**
 * 離散Fourier変換に関する基底関数の種類を表す列挙型.
 * 
 * @author Matsuura Y.
 */
public enum FourierType {

    /**
     * Fourier変換: <i>W</i><sub><i>N</i></sub> = exp(-i(2&pi;/<i>N</i>))
     * を扱うことの表示.
     */
    DFT {
        @Override
        double argByPi(int N, int j) {
            return (-2d * j) / N;
        }
    },

    /**
     * 逆Fourier変換: <i>W</i><sub><i>N</i></sub> = exp(i(2&pi;/<i>N</i>))
     * を扱うことの表示.
     */
    IDFT {
        @Override
        double argByPi(int N, int j) {
            return (2d * j) / N;
        }
    };

    /**
     * <i>W</i><sub><i>N</i></sub><sup><i>j</i></sup> = exp(i&pi;<i>x</i>)
     * となる<i>x</i>を計算する.
     * 
     * @param N N
     * @param j j
     * @return x
     */
    abstract double argByPi(int N, int j);
}