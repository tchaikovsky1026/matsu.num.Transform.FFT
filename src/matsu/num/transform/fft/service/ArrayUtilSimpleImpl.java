/**
 * 2024.4.2
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * シンプルな {@link ArraysUtil} の実装.
 * 
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
final class ArrayUtilSimpleImpl implements ArraysUtil {

    ArrayUtilSimpleImpl() {
        super();
    }

    @Override
    public double normMax(double[] vector) {
        double outputValue = 0.0;
        int index;
        for (index = vector.length - 1; index >= 3; index -= 4) {
            double v0 = Math.abs(vector[index]);
            double v1 = Math.abs(vector[index - 1]);
            double v2 = Math.abs(vector[index - 2]);
            double v3 = Math.abs(vector[index - 3]);
            double v01 = Math.max(v0, v1);
            double v23 = Math.max(v2, v3);
            outputValue = Math.max(outputValue, Math.max(v01, v23));
        }
        for (; index >= 0; index--) {
            double v0 = Math.abs(vector[index]);
            outputValue = Math.max(outputValue, v0);
        }
        return outputValue;
    }

}
