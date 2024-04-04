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
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;

/**
 * {@link CommonLib} のデフォルト実装のホルダ.
 * 
 * @author Matsuura Y.
 * @version 20.0
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
