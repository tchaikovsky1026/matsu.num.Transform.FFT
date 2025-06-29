/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * シンプルな {@link ArraysUtil} の実装.
 * 
 * 
 * @author Matsuura Y.
 */
final class ArrayUtilSimpleImpl implements ArraysUtil {

    ArrayUtilSimpleImpl() {
        super();
    }

    @Override
    public double normMax(double[] vector) {

        /*
         * 4系列を分離することで, 並列化される可能性がある.
         */
        double v0 = 0d;
        double v1 = 0d;
        double v2 = 0d;
        double v3 = 0d;
        int index;
        for (index = vector.length - 1; index >= 3; index -= 4) {
            v0 = Math.max(v0, Math.abs(vector[index]));
            v1 = Math.max(v1, Math.abs(vector[index - 1]));
            v2 = Math.max(v2, Math.abs(vector[index - 2]));
            v3 = Math.max(v3, Math.abs(vector[index - 3]));
        }
        for (; index >= 0; index--) {
            v0 = Math.max(v0, Math.abs(vector[index]));
        }
        return Math.max(Math.max(v0, v1), Math.max(v2, v3));
    }
}
