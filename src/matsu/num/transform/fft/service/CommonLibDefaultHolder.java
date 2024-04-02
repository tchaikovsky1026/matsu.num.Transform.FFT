/**
 * 2024.2.15
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link CommonLib} のデフォルト実装のホルダ.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
final class CommonLibDefaultHolder {

    static final CommonLib DEFAULT_INSTANCE = new CommonLibImpl();

    private CommonLibDefaultHolder() {
        throw new AssertionError();
    }

    private static final class CommonLibImpl extends CommonLib {

        private final Trigonometry trigonometry;
        private final ArraysUtil arraysUtil;

        CommonLibImpl() {
            super();
            this.trigonometry = new TrigonometryMath();
            this.arraysUtil = new ArrayUtilSimpleImpl();
        }

        @Override
        public Trigonometry trigonometry() {
            return this.trigonometry;
        }


        @Override
        ArraysUtil arrayUtil() {
            return this.arraysUtil;
        }
        
        @Override
        public String toString() {
            return "CommonLib(default)";
        }

    }
}
