package matsu.num.transform.fft.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.service.fuctionaltype.FunctionalType;

/**
 * {@link FFTModuleExecutorProvider} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class FFTModuleExecutorProviderTest {

    public static final Class<?> TEST_CLASS = FFTModuleExecutorProvider.class;

    @RunWith(Theories.class)
    public static class getメソッドのテスト {

        private FFTModuleExecutorProvider provider = FFTModuleExecutorProvider.byDefaultLib();

        @DataPoints
        public static ExecutorType<?>[] types;

        @BeforeClass
        public static void before_ExecutorTypeの登録() {
            ExecutorType<?>[] zero = new ExecutorType<?>[0];
            List<ExecutorType<?>> list = new ArrayList<>();
            list.addAll(DftTypes.values());
            list.addAll(DctDstTypes.values());
            list.addAll(CyclicConvolutionTypes.values());
            types = list.toArray(zero);
        }

        @Theory
        public void test(ExecutorType<?> type) {
            try {
                ((FunctionalType<?>) type).executorClass().cast(provider.get(type));
            } catch (ClassCastException e) {
                throw new AssertionError("providerは正しい型のインスタンスを生成していない");
            }
        }
    }

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
