/**
 * 2024.2.15
 */
package matsu.num.transform.fft.service;

import matsu.num.transform.fft.lib.Trigonometry;

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

        CommonLibImpl() {
            super();
            this.trigonometry = new TrigonometryMath();
        }

        @Override
        public Trigonometry trigonometry() {
            return this.trigonometry;
        }
        
        @Override
        public String toString() {
            return "CommonLib(default)";
        }

    }
}
