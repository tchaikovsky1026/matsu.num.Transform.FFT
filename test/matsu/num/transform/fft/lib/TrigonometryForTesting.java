/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.lib;

/**
 * テスト用三角関数ライブラリ.
 */
public final class TrigonometryForTesting implements Trigonometry{

    public static final Trigonometry INSTANCE = new TrigonometryForTesting();
    
    private TrigonometryForTesting() {
    }
    
    @Override
    public double cospi(double x) {
        return Math.cos(Math.PI * x);
    }

    @Override
    public double sinpi(double x) {
        return Math.sin(Math.PI * x);
    }
}
