package matsu.num.transform.fft.component;

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
 * {@link FourierBasisComputer}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class FourierBasisComputerTest {

    public static final Class<?> TEST_CLASS = FourierBasisComputer.class;

    public static class 基底関数のテスト {

        @Test
        public void test_8分割_DFT() {
            FourierBasis basis = FourierBasisComputer.covering(8, FourierType.DFT).getBasis(8);

            assertThat(basis.valueAt(0).real(), is(1d));
            assertThat(basis.valueAt(4).real(), is(-1d));

            assertThat(basis.valueAt(2).imaginary(), is(-1d));
            assertThat(basis.valueAt(6).imaginary(), is(1d));
        }

        @Test
        public void test_8分割_IDFT() {
            FourierBasis basis = FourierBasisComputer.covering(8, FourierType.IDFT).getBasis(8);

            assertThat(basis.valueAt(0).real(), is(1d));
            assertThat(basis.valueAt(4).real(), is(-1d));

            assertThat(basis.valueAt(2).imaginary(), is(1d));
            assertThat(basis.valueAt(6).imaginary(), is(-1d));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_0分割は不可能() {
            FourierBasisComputer.covering(0, FourierType.DFT);
        }
    }

    public static class 間引きのテスト {

        private FourierBasisComputer basisComputer;

        @Before
        public void before_8分割_DFT() {
            basisComputer = FourierBasisComputer.covering(8, FourierType.DFT);
        }

        @Test
        public void test_4分割DFT基底関数の取得() {

            FourierBasis sub = basisComputer.getBasis(4);

            assertThat(sub.valueAt(0).real(), is(1d));
            assertThat(sub.valueAt(2).real(), is(-1d));

            assertThat(sub.valueAt(1).imaginary(), is(-1d));
            assertThat(sub.valueAt(3).imaginary(), is(1d));
        }

        @Test(expected = IllegalArgumentException.class)
        public void test_3分割は取得不可能() {
            basisComputer.getBasis(3);
        }
    }

    @RunWith(Theories.class)
    public static class Power2のキャッシュComputerの取得テスト {

        @DataPoints
        public static int[] SIZE = {
                1,
                1 << 1,
                1 << 2,
                1 << 3,
                1 << 4,
                1 << 5,
                1 << 6,
                1 << 7,
                1 << 8,
                1 << 9,
                1 << 10,
                1 << 11,
                1 << 12
        };

        @Theory
        public void test_DFTのBasis取得(int size) {
            //2の累乗サイズのBasisを取得する
            FourierBasisComputer.covering(size, FourierType.DFT).getBasis(size);
        }

        @Theory
        public void test_IDFTのBasis取得(int size) {
            //2の累乗サイズのBasisを取得する
            FourierBasisComputer.covering(size, FourierType.IDFT).getBasis(size);
        }
    }
}
