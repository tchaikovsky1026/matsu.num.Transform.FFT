/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.number;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 3以上の素数の原始根の最小値の検索.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
final class MinimumPrimitiveRootSearch {

    private final int prime;
    private final int primitiveRoot;

    /**
     * 3以上の素数を与えて, 原始根の最小値を生成する.
     * 
     * <p>
     * 素数判定は行わない. <br>
     * 合成数を与えた場合の動作は保証されない.
     * </p>
     * 
     * @param prime 3以上の素数
     * @throws IllegalArgumentException 引数が3以上の奇数でない場合, primeが合成数と予測される場合
     *             (ただし, 合成数ならば必ずスローされるわけではない)
     */
    MinimumPrimitiveRootSearch(int prime) {
        if (!(prime >= 3 && (prime & 1) == 1)) {
            throw new IllegalArgumentException("引数が3以上の奇数でない");
        }

        this.prime = prime;
        this.primitiveRoot = calcPrimitiveRoot(prime);
    }

    public int prime() {
        return this.prime;
    }

    public int primitiveRoot() {
        return this.primitiveRoot;
    }

    private static int calcPrimitiveRoot(int prime) {
        if (prime == 3) {
            return 2;
        }

        //mは4以上の偶数であることが保証される
        int m = prime - 1;

        //検証すべき指数
        int[] exponent;
        {
            int[] fact = factDistinct(m);
            exponent = new int[fact.length];
            for (int i = 0; i < exponent.length; i++) {
                exponent[i] = m / fact[i];
            }
            Arrays.sort(exponent);
        }

        //2からm(=p-1)まで順番に原始根かどうかを探索
        for (int a = 2; a <= m; a++) {
            if (isPrimitiveRoot(a, prime, exponent)) {
                return a;
            }
        }

        throw new IllegalArgumentException("合成数である");
    }

    /**
     * 与えられた整数の素因数を重複を除いて列挙する. <br>
     * nは合成数であることを期待する.
     * 
     * @param n 整数
     * @return 重複を除いた素因数の列挙
     */
    private static int[] factDistinct(int n) {
        assert (n & 1) == 0 : "nが偶数でない";
        assert n >= 4 : "nが4以上でない";

        Set<Integer> set = new HashSet<>();

        { //素因数を探す
          //素因数2だけ別に扱い, 後は奇数を順番に検証する
            while (true) {
                if (n % 2 == 0) {
                    set.add(2);
                    n /= 2;
                } else {
                    break;
                }
            }

            int q = 3;
            while (q * q <= n) {
                if (n % q == 0) {
                    set.add(q);
                    n /= q;
                } else {
                    q += 2;
                }
            }
            if (n != 1) {
                set.add(n);
            }
        }

        //配列にして返す
        int[] out = new int[set.size()];
        int j = 0;
        for (int q : set) {
            out[j] = q;
            j++;
        }
        return out;
    }

    /**
     * aがpの原始根かどうかを判定する.
     * 
     * @param a a
     * @param p p
     * @param exponent p-1の約数のうち, 検証すべき指数
     * @return aが原始根ならtrue
     */
    private static boolean isPrimitiveRoot(int a, int p, int[] exponent) {

        int maxExp = exponent[exponent.length - 1];

        /* a^1, a^2, a^4, a^8,...を格納する配列 */
        // maxExpを2進法で表現するときに必要な桁数は, 
        // 1 + floor(log_2(maxExp))である. 
        int[] power2Modular = new int[1 + Power2Util.floorLog2(maxExp)];

        { // a^(2^i)を計算する
            int r = a;
            for (int i = 0; i < power2Modular.length; i++) {
                power2Modular[i] = r;
                r = r * r % p;
            }
        }
        for (int j = 0; j < exponent.length; j++) {
            int exp = exponent[j];
            //expのビットをシフトしながらa^expを計算する
            //floor(log_2(maxExp)) 回シフトまでする必要がある
            int shiftMax = Power2Util.floorLog2(exp);

            //a^expを, expの2進展開を
            int r = 1;
            for (int shift = 0; shift <= shiftMax; shift++) {
                if ((exp & 1) == 1) {
                    r = r * power2Modular[shift] % p;
                }
                exp = exp >> 1;
            }
            if (r == 1) {
                return false;
            }
        }

        return true;
    }

}
