/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link CyclicConvolutionExecutors} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class CyclicConvolutionExecutorsTest {

    public static final Class<?> TEST_CLASS = CyclicConvolutionExecutors.class;

    @RunWith(Theories.class)
    public static class エグゼキュータの生成のテスト {

        @DataPoints
        public static Collection<ExecutorType<?>> types = values();

        private static FFTModuleExecutorProvider provider;

        @BeforeClass
        public static void before_プロバイダを準備する() {
            provider = FFTModuleExecutorProvider.byDefaultLib();
        }

        @Theory
        public void test_エグゼキュータが生成できることを検証する(ExecutorType<?> type)
                throws InterruptedException, ExecutionException {

            //網羅的にエグゼキュータの生成を行う
            //エグゼキュータに循環依存がある場合, タイムアウトする.

            long timeoutAsSeconds = 5;

            ExecutorService service = Executors.newCachedThreadPool();
            Future<?> submit = service.submit(() -> provider.get(type));
            service.shutdown();
            try {
                submit.get(timeoutAsSeconds, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                throw new AssertionError(String.format("タイムアウトしました: %s秒", timeoutAsSeconds));
            }
        }
    }

    public static class 列挙表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            values().stream().forEach(System.out::println);
            System.out.println();
        }
    }

    /**
     * テストクラスのすべての公開定数を含んだコレクションを返す.
     * 
     * @return コレクション
     */
    static Collection<ExecutorType<?>> values() {
        List<ExecutorType<?>> constantFieldList = new ArrayList<>();

        @SuppressWarnings("rawtypes")
        Class<ExecutorType> clazz = ExecutorType.class;

        for (Field f : TEST_CLASS.getFields()) {
            if ((f.getModifiers() & Modifier.STATIC) == 0) {
                continue;
            }
            try {
                constantFieldList.add(clazz.cast(f.get(null)));
            } catch (IllegalAccessException | ClassCastException ignore) {
                //無関係なフィールドなら無視する
            }
        }

        return constantFieldList;
    }
}
