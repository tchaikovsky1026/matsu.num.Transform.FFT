package matsu.num.transform.fft.number;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link Power2Util}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Power2UtilTest {

    public static final Class<?> TEST_CLASS = Power2Util.class;

    @RunWith(Enclosed.class)
    public static class isPower2のテスト {

        @RunWith(Theories.class)
        public static class 正常系 {

            @DataPoints
            public static IntBooleanPair[] PAIRS = {
                    IntBooleanPair.of(1, true),
                    IntBooleanPair.of(2, true),
                    IntBooleanPair.of(4, true),
                    IntBooleanPair.of(8, true),
                    IntBooleanPair.of(16, true),
                    IntBooleanPair.of(0x4000_0000, true),

                    IntBooleanPair.of(15, false),
                    IntBooleanPair.of(17, false),
                    IntBooleanPair.of(0x7FFF_FFFF, false)
            };

            @Theory
            public void test(IntBooleanPair pair) {
                assertThat(Power2Util.isPowerOf2(pair.n), is(pair.pred));
            }

            public static class IntBooleanPair {
                final int n;
                final boolean pred;

                private IntBooleanPair(int n, boolean pred) {
                    super();
                    this.n = n;
                    this.pred = pred;
                }

                @Override
                public String toString() {
                    return String.format("[%d, %b]", n, pred);
                }

                public static IntBooleanPair of(int n, boolean pred) {
                    return new IntBooleanPair(n, pred);
                }

            }

        }

        @RunWith(Theories.class)
        public static class 例外系 {

            @DataPoints
            public static int[] VALUES = {
                    0,
                    -1,
                    0x8000_0000
            };

            @Theory
            public void test(int value) {
                try {
                    Power2Util.isPowerOf2(value);

                    //例外が発生しなかった場合, アサーションエラーが発生する
                    throw new AssertionError("例外が発生しなかった: [" + value + "]");
                } catch (IllegalArgumentException e) {
                    //ここに来ると成功
                }
            }

        }
    }

    @RunWith(Enclosed.class)
    public static class floorLog2のテスト {

        @RunWith(Theories.class)
        public static class 正常系 {

            @DataPoints
            public static int[][] PAIRS = {
                    { 1, 0 },
                    { 2, 1 },
                    { 3, 1 },
                    { 4, 2 },
                    { 7, 2 },
                    { 8, 3 },
                    { 9, 3 },
                    { 0x4000_0000, 30 },
                    { 0x7FFF_FFFF, 30 }
            };

            @Theory
            public void test(int[] pair) {
                assertThat(Power2Util.floorLog2(pair[0]), is(pair[1]));
            }
        }

        @RunWith(Theories.class)
        public static class 例外系 {

            @DataPoints
            public static int[] VALUES = {
                    0,
                    -1,
                    0x8000_0000
            };

            @Theory
            public void test(int value) {
                try {
                    Power2Util.floorLog2(value);

                    //例外が発生しなかった場合, アサーションエラーが発生する
                    throw new AssertionError("例外が発生しなかった: [" + value + "]");
                } catch (IllegalArgumentException e) {
                    //ここに来ると成功
                }
            }

        }
    }

    @RunWith(Enclosed.class)
    public static class ceilToPower2のテスト {

        @RunWith(Theories.class)
        public static class 正常系 {

            @DataPoints
            public static int[][] PAIRS = {
                    { 1, 1 },
                    { 2, 2 },
                    { 3, 4 },
                    { 4, 4 },
                    { 7, 8 },
                    { 8, 8 },
                    { 9, 16 },
                    { 0x4000_0000, 0x4000_0000 },
                    { 0x2000_0001, 0x4000_0000 }
            };

            @Theory
            public void test(int[] pair) {
                assertThat(Power2Util.ceilToPower2(pair[0]), is(pair[1]));
            }
        }

        @RunWith(Theories.class)
        public static class 例外系 {

            @DataPoints
            public static int[] VALUES = {
                    0,
                    -1,
                    0x4000_0001
            };

            @Theory
            public void test(int value) {
                try {
                    Power2Util.ceilToPower2(value);

                    //例外が発生しなかった場合, アサーションエラーが発生する
                    throw new AssertionError("例外が発生しなかった: [" + value + "]");
                } catch (IllegalArgumentException e) {
                    //ここに来ると成功
                }
            }

        }
    }

}
