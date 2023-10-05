package matsu.num.transform.fft.number;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link MinimumPrimitiveRootSearch}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class MinimumPrimitiveRootSearchTest {

    public static final Class<?> TEST_CLASS = MinimumPrimitiveRootSearch.class;

    @RunWith(Theories.class)
    public static class 適当な数値で最小原始根を確かめる {

        @DataPoints
        public static int[][] primeRootArray = {
                { 3, 2 },
                { 191, 19 },
                { 271, 6 },
                { 409, 21 },
                { 457, 13 }
        };

        @Theory
        public void test(int[] primeRoot) {
            int p = primeRoot[0];
            int resultMinRoot = new MinimumPrimitiveRootSearch(p).primitiveRoot();
            int expectedMinRoot = primeRoot[1];
            assertThat(
                    String.format("%d の計算結果は %d だが, 真値は %d である", p, resultMinRoot, expectedMinRoot),
                    resultMinRoot, is(expectedMinRoot));

        }
    }

}
