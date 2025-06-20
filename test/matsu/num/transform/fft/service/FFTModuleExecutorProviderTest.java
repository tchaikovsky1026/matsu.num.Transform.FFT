/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.service;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link FFTModuleExecutorProvider} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class FFTModuleExecutorProviderTest {

    public static final Class<?> TEST_CLASS = FFTModuleExecutorProvider.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(FFTModuleExecutorProvider.byDefaultLib());
            System.out.println(FFTModuleExecutorProvider.by(CommonLib.Builder.implementedInit().build()));
            System.out.println();
        }
    }
}
