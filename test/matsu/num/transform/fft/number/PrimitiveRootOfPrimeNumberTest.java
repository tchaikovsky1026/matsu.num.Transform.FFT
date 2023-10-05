package matsu.num.transform.fft.number;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link PrimitiveRootOfPrimeNumber}クラスのテスト. 
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class PrimitiveRootOfPrimeNumberTest {

    public static final Class<?> TEST_CLASS = PrimitiveRootOfPrimeNumber.class;

    @RunWith(Theories.class)
    public static class 生成失敗に関するテスト {

        @DataPoints
        public static int[] args = { 0, 1, 2, 4, 6, 8 };

        @Theory
        public void test_生成に失敗しIAEx(int arg) {
            try {

                //ここで例外が投げられる
                PrimitiveRootOfPrimeNumber.of(arg);

                throw new AssertionError("fail");
            } catch (IllegalArgumentException e) {
                // ここに到達するのが正しい
            }
        }
    }

    public static class マッピングのテスト {

        private PrimitiveRootOfPrimeNumber primeRoot;

        @Before
        public void before_素数7の原始根を生成() {
            primeRoot = PrimitiveRootOfPrimeNumber.of(7);
        }

        @Test
        public void test_累乗を検証() {
            int[] expected = { 1, 3, 2, 6, 4, 5 };

            for (int i = 0; i < expected.length; i++) {
                assertThat(primeRoot.power(i), is(expected[i]));
            }
        }

        @Test
        public void test_逆関数を検証() {
            int[] expected = { 0, 2, 1, 4, 5, 3 };

            for (int i = 0; i < expected.length; i++) {
                assertThat(primeRoot.invPower(i + 1), is(expected[i]));
            }
        }
    }

    @RunWith(Theories.class)
    public static class 生成された原始根の振る舞いの検証 {

        @DataPoints
        public static int[] primes = { 3, 5, 31, 43 };

        @Theory
        public void test_生成に成功しpowerが1対1対応(int prime) {
            PrimitiveRootOfPrimeNumber root = PrimitiveRootOfPrimeNumber.of(prime);

            boolean[] isMapped = new boolean[prime - 1];
            for (int i = 0; i < prime - 1; i++) {
                isMapped[root.power(i) - 1] = true;
            }
            for (boolean b : isMapped) {
                if (!b) {
                    throw new AssertionError("1対1対応になっていない");
                }
            }
        }

        @Theory
        public void test_生成に成功しpowerとinvPowerが逆関数(int prime) {
            PrimitiveRootOfPrimeNumber root = PrimitiveRootOfPrimeNumber.of(prime);

            for (int i = 0; i < prime - 1; i++) {
                assertThat(root.invPower(root.power(i)), is(i));
                assertThat(root.power(root.invPower(i + 1)), is(i + 1));
            }
        }

    }

}
