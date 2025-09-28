/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.9.27
 */
package matsu.num.transform.fft.number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 素因数分解を扱う.
 * 
 * <p>
 * このクラスのインスタンスはイミュータブルである. <br>
 * ただし, マルチスレッドで使用された場合はフィールドの初期化が複数回走る可能性がある.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class PrimeFactorization {

    private final int original;
    private final int[] factors;

    // この2値は, originalが1のときダミーが入る(外には漏洩しない)
    private final int separatedValue;
    private PrimeFactorization child;

    private PrimeFactorization(int original, int[] factors) {
        this.original = original;
        this.factors = factors;

        this.separatedValue = factors.length == 0
                ? 0
                : factors[0];
    }

    /**
     * 自身の値を返す.
     * 
     * @return 自身の値
     */
    public int original() {
        return this.original;
    }

    /**
     * 自身から分離される素因数を返す. <br>
     * どの素因数が分離されるかは決定的だが規定されていない.
     * 
     * @return 分離された素因数
     * @throws IllegalStateException 自身が1の場合
     */
    public int separatedValue() {
        if (this.original == 1) {
            throw new IllegalStateException("original == 1");
        }
        return this.separatedValue;
    }

    /**
     * 素因数を1個分離した残りを返す. <br>
     * {@link #separatedValue()} と整合する.
     * 
     * @return 自身から素因数を1個分離した残り
     * @throws IllegalStateException 自身が1の場合
     */
    public PrimeFactorization child() {
        if (this.original == 1) {
            throw new IllegalStateException("original == 1");
        }

        if (this.child != null) {
            return this.child;
        }

        int separatedValue = this.separatedValue;
        int newOriginal = this.original / separatedValue;
        int[] newFactors = Arrays.copyOfRange(this.factors, 1, this.factors.length);
        return this.child = new PrimeFactorization(newOriginal, newFactors);
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

        return new PrimeFactorization(original, out);
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
}
