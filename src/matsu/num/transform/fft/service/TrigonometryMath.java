/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.lib.Trigonometry;

/**
 * {@link Math} ライブラリを用いた {@link Trigonometry}.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
final class TrigonometryMath implements Trigonometry {

    TrigonometryMath() {
        super();
    }

    @Override
    public double cospi(double x) {
        return Math.cos(Math.PI * x);
    }

    @Override
    public double sinpi(double x) {
        return Math.sin(Math.PI * x);
    }

    @Override
    public String toString() {
        return "Exponentiation(Math)";
    }
}
