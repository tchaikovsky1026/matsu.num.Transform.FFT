/**
 * 2023.9.30
 */
package matsu.num.transform.fft.number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 素因数分解を扱う.
 * 
 * <p>
 * このクラスは素因数分解前のオリジナルの値に基づいて等価性を定義する.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.8
 */
public final class PrimeFactorization {

    private final int original;
    private final int[] factors;

    //自身のファミリー
    private final Family family;

    //循環参照が生じ不要な場合もあるため, 遅延初期化
    private volatile Separated separatedLowest;
    private volatile Separated separatedHighest;

    //遅延初期化に係るロックオブジェクト
    private final Object lock = new Object();

    private PrimeFactorization(int original, int[] factors, Family family) {
        this.original = original;
        this.factors = factors;
        this.family = family;
    }

    public int original() {
        return this.original;
    }

    public int numberOfFactors() {
        return this.factors.length;
    }

    /**
     * 素因数を分離する. <br>
     * どの素因数が分離されるかは決定的だが規定されていない.
     * 
     * 
     * @return 分離
     * @throws IllegalStateException originalが1で分解できない場合
     */
    public Separated separate() {
        return this.separateLowest();
    }

    /**
     * 最小の素因数を分離する.
     * 
     * 
     * @return 分離
     * @throws IllegalStateException originalが1で分解できない場合
     */
    public Separated separateLowest() {

        Separated out = this.separatedLowest;
        if (Objects.nonNull(out)) {
            return out;
        }
        synchronized (this.lock) {
            out = this.separatedLowest;
            if (Objects.nonNull(out)) {
                return out;
            }
            out = this.createSeparatedLowest();
            this.separatedLowest = out;
            return out;
        }
    }

    /**
     * 最大の素因数を分離する.
     * 
     * 
     * @return 分離
     * @throws IllegalStateException originalが1で分解できない場合
     */
    public Separated separateHighest() {

        Separated out = this.separatedHighest;
        if (Objects.nonNull(out)) {
            return out;
        }
        synchronized (this.lock) {
            out = this.separatedHighest;
            if (Objects.nonNull(out)) {
                return out;
            }
            out = this.createSeparatedHighest();
            this.separatedHighest = out;
            return out;
        }
    }

    private Separated createSeparatedLowest() {
        if (this.factors.length == 0) {
            throw new IllegalStateException("originalが1である");
        }

        //配列の先頭を分離する
        int separatedValue = this.factors[0];
        int[] newFact = Arrays.copyOfRange(this.factors, 1, this.factors.length);
        int newOriginal = this.original / separatedValue;

        return new Separated(
                separatedValue, this.family.intern(new PrimeFactorization(newOriginal, newFact, this.family)));
    }

    private Separated createSeparatedHighest() {
        if (this.factors.length == 0) {
            throw new IllegalStateException("originalが1である");
        }

        //配列の末尾を分離する
        int[] newFact = Arrays.copyOf(this.factors, this.factors.length - 1);
        int separatedValue = this.factors[this.factors.length - 1];
        int newOriginal = this.original / separatedValue;

        return new Separated(
                separatedValue, this.family.intern(new PrimeFactorization(newOriginal, newFact, this.family)));
    }

    /**
     * このインスタンスと引数との等価性を判定する.
     * 
     * @return 等価ならtrue
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrimeFactorization)) {
            return false;
        }

        PrimeFactorization target = (PrimeFactorization) obj;

        return Integer.compare(this.original, target.original) == 0;
    }

    /**
     * このインスタンスのハッシュコードを返す.
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(this.original);
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * おそらく次の形式が適切であろうが, 確実ではなく,
     * バージョン間の整合性も担保されていない. <br>
     * {@code [original: %original]}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format("[original:%d]", this.original);
    }

    /**
     * 与えた整数を素因数分解する. 
     * 
     * @param original 素因数分解する整数
     * @return 素因数分解
     * @throws IllegalArgumentException 引数が1以上の整数でない場合
     */
    public static PrimeFactorization of(int original) {

        if (original < 1) {
            throw new IllegalArgumentException("1以上でない");
        }

        List<Integer> factorList = new ArrayList<>();

        { //素因数を探す
            int n = original;
            while (true) {
                if (n % 2 == 0) {
                    factorList.add(2);
                    n /= 2;
                } else {
                    break;
                }
            }

            //奇数のみ素因数検証(q:odd)
            int q = 3;
            while (q * q <= n) {
                if (n % q == 0) {
                    factorList.add(q);
                    n /= q;
                } else {
                    q += 2;
                }
            }
            if (n != 1) {
                factorList.add(n);
            }
        }

        //配列にして返す
        int[] out = new int[factorList.size()];
        int j = 0;
        for (int q : factorList) {
            out[j] = q;
            j++;
        }

        //outは昇順であるはず
        assert isSorted(out) : "Bug?:outが昇順でない";

        return new PrimeFactorization(original, out, new Family());
    }

    /**
     * アサーション用メソッド.
     * 引数が昇順かどうかを判定する.
     * 
     * @param array 配列
     * @return 昇順ならtrue
     */
    private static boolean isSorted(int[] array) {
        int[] sorted = array.clone();
        Arrays.sort(sorted);
        return Arrays.equals(array, sorted);
    }

    public static final class Separated {

        private int separatedValue;
        private final PrimeFactorization child;

        private Separated(int separatedValue, PrimeFactorization child) {
            this.separatedValue = separatedValue;
            this.child = child;
        }

        public int separatedValue() {
            return this.separatedValue;
        }

        public PrimeFactorization child() {
            return this.child;
        }
    }

    /**
     * 素因数分解のファミリーを管理する.
     * 
     * <p>
     * {@link PrimeFactorization#separatedLowest}, {@link PrimeFactorization#separatedHighest}によって, <br>
     * {@link Separated}が2の累乗で爆発するリスクがあるので, <br>
     * それを管理して無駄な{@link PrimeFactorization}を生成しないための仕組み.
     * </p>
     */
    private static final class Family {

        private final Map<PrimeFactorization, PrimeFactorization> family;

        /**
         * 新しいファミリーを構築する.
         */
        public Family() {
            this.family = new HashMap<>();
        }

        /**
         * 引数を正準表現に直す.
         * 
         * <p>
         * 引数と等価なインスタンスがすでに管理下にある場合は管理下のインスタンスを,
         * 管理下にない場合は新しく登録し引数と同じ参照を返す.
         * </p>
         * 
         * @param obj obj
         * @return objの正準表現
         */
        public synchronized PrimeFactorization intern(PrimeFactorization obj) {

            assert Objects.nonNull(obj) : "引数がnullである";

            PrimeFactorization interned = this.family.putIfAbsent(obj, obj);
            return Objects.nonNull(interned)
                    ? interned
                    : obj;
        }
    }

}
