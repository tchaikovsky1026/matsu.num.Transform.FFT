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
 * {@link DftTypes} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class DftTypesTest {

    public static final Class<?> TEST_CLASS = DftTypes.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            DftTypes.values().stream().forEach(System.out::println);
            System.out.println();
        }
    }
}
