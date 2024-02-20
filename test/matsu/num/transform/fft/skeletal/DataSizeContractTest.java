package matsu.num.transform.fft.skeletal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.transform.fft.validation.StructureAcceptance;
import matsu.num.transform.fft.validation.StructureRejected;

/**
 * {@link DataSizeContract} のテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class DataSizeContractTest {

    public static final Class<?> TEST_CLASS = DataSizeContract.class;

    @RunWith(Enclosed.class)
    public static class このクラスのprivateメソッドのテスト {
        public static class isACCEPTEDメソッド {
            @Test
            public void test_isACCEPTED_ACCEPTEDを渡すとtrue() {
                assertThat(isACCEPTED(StructureAcceptance.ACCEPTED), is(true));
            }

            @Test
            public void test_isACCEPTED_rejectを渡すとfalse() {
                assertThat(isACCEPTED(StructureRejected.by(IllegalArgumentException::new, "IAEx")), is(false));
            }
        }
    }

    @RunWith(Theories.class)
    public static class DataSizeContractのデフォルト実装のテスト {

        private DataSizeContract instance;

        /**
         * サイズとisACCEPTEDの結果の期待値ペアを扱う.
         */
        @DataPoints
        public static IntBooleanPair[] SIZE_TO_EXPECTED_IsACCEPTED_PAIRS = {
                new IntBooleanPair(0, true),
                new IntBooleanPair(1, true),
                new IntBooleanPair(-1, false)
        };

        @Before
        public void before_デフォルト実装のインスタンス化() {
            instance = new DataSizeContract() {
            };
        }

        @Theory
        public void test_サイズを与えたときのisACCEPTEDの結果を検証する(IntBooleanPair sizeToExpectedIsAccepted) {
            StructureAcceptance result = instance.acceptsSize(sizeToExpectedIsAccepted.intValue);
            assertThat(isACCEPTED(result), is(sizeToExpectedIsAccepted.booleanValue));
        }
    }

    @RunWith(Theories.class)
    public static class DataSizeContractのオーバーライド実装のテスト {

        private static final int REQUIRED_SIZE = 1;
        private static final int UPPER_LIMIT_SIZE = 10;

        private DataSizeContract instance;

        /**
         * サイズとisACCEPTEDの結果の期待値ペアを扱う.
         */
        @DataPoints
        public static IntBooleanPair[] SIZE_TO_EXPECTED_IsACCEPTED_PAIRS = {
                new IntBooleanPair(REQUIRED_SIZE - 1, false),
                new IntBooleanPair(REQUIRED_SIZE, true),
                new IntBooleanPair(UPPER_LIMIT_SIZE, true),
                new IntBooleanPair(UPPER_LIMIT_SIZE + 1, false)
        };

        @Before
        public void before_オーバーライド実装のインスタンス化() {
            instance = new DataSizeContract() {
                @Override
                protected int requiredSize() {
                    return REQUIRED_SIZE;
                }

                @Override
                protected int upperLimitSize() {
                    return UPPER_LIMIT_SIZE;
                }
            };
        }

        @Theory
        public void test_サイズを与えたときのisACCEPTEDの結果を検証する(IntBooleanPair sizeToExpectedIsAccepted) {
            StructureAcceptance result = instance.acceptsSize(sizeToExpectedIsAccepted.intValue);
            assertThat(isACCEPTED(result), is(sizeToExpectedIsAccepted.booleanValue));
        }
    }

    /**
     * 与えた値がACCEPTEDかどうかを判定する.
     * 
     * @param value 判定する値
     * @return 値がACCEPTEDならtrue
     * @throws NullPointerException 引数がnull
     */
    private static boolean isACCEPTED(StructureAcceptance value) {
        return Objects.requireNonNull(value) == StructureAcceptance.ACCEPTED;
    }

    /**
     * intとbooleanのペアを扱う.
     */
    private static final class IntBooleanPair {

        final int intValue;
        final boolean booleanValue;

        IntBooleanPair(int intValue, boolean booleanValue) {
            super();
            this.intValue = intValue;
            this.booleanValue = booleanValue;
        }
    }

}
