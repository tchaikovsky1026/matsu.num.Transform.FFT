/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.26
 */
package matsu.num.transform.fft.number;

/**
 * 素数の原始根に関する振る舞いを扱う.
 * 
 * <p>
 * このクラスは素数と原始根の値に基づく等価性を提供する.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class PrimitiveRootOfPrimeNumber {

    private final int prime;
    private final int primitiveRoot;

    /**
     * n から a^n を得るマッピング. <br>
     * nは0からp-2が意味ある.
     */
    private final int[] expMapping;

    /**
     * a^n から n を得るマッピング. <br>
     * a^nは1からp-1が意味あるので, それをindex: {@literal 0 <= n <= p-2}に記憶する.
     */
    private final int[] inverseExpMapping;

    /**
     * インスタンスを生成する. <br>
     * 与えた素数と原始根が正当かどうかを判定しないため, このコンストラクタを公開してはいけない.
     * 
     * @param prime 素数
     * @param primitiveRoot primeの原始根
     */
    private PrimitiveRootOfPrimeNumber(int prime, int primitiveRoot) {
        this.prime = prime;
        this.primitiveRoot = primitiveRoot;

        this.expMapping = this.calcExpMapping();
        this.inverseExpMapping = this.calcInverseExpMapping();
    }

    private int[] calcExpMapping() {
        int[] mapping = new int[this.prime - 1];

        final int a = this.primitiveRoot;
        final int p = this.prime;

        int a_n = 1;
        for (int i = 0; i < mapping.length; i++) {
            mapping[i] = a_n;
            a_n = a_n * a % p;
        }

        return mapping;
    }

    private int[] calcInverseExpMapping() {

        int[] inverseMapping = new int[prime - 1];
        for (int n = 0; n < this.expMapping.length; n++) {
            int a_n = this.expMapping[n];
            inverseMapping[a_n - 1] = n;
        }

        return inverseMapping;
    }

    /**
     * このクラスが扱う素数pを返す.
     * 
     * @return p
     */
    public int prime() {
        return this.prime;
    }

    /**
     * このクラスが扱う素数pの原始根aを返す.
     * 
     * @return a
     */
    public int primitiveRoot() {
        return this.primitiveRoot;
    }

    /**
     * このクラスの扱う素数pとその原始根aと, 与えられたnについて, <br>
     * a^n mod p <br>
     * を返す.
     * 
     * <p>
     * {@literal 0 <= n <= p-2}をサポートする.
     * </p>
     * 
     * @param n n
     * @return a^n mod p (1以上p-1以下である)
     * @throws IllegalArgumentException nが0以上p-2以下でない場合
     */
    public int power(int n) {
        if (!(0 <= n && n < this.prime - 1)) {
            throw new IllegalArgumentException("nが0以上p-2以下でない");
        }
        return this.expMapping[n];
    }

    /**
     * このクラスの扱う素数pとその原始根aと, 与えられたrについて, <br>
     * a^n mod p = r <br>
     * を満たすnを返す.
     * 
     * <p>
     * {@literal 1 <= r <= p-1}をサポートする.
     * </p>
     * 
     * @param r r
     * @return n (0以上p-2以下である)
     * @throws IllegalArgumentException rが1以上p-1以下でない場合
     */
    public int invPower(int r) {
        if (!(1 <= r && r < this.prime)) {
            throw new IllegalArgumentException("a^nが1以上p-1以下でない");
        }
        return this.inverseExpMapping[r - 1];
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrimitiveRootOfPrimeNumber)) {
            return false;
        }
        PrimitiveRootOfPrimeNumber target = (PrimitiveRootOfPrimeNumber) obj;

        return this.prime == target.prime
                && this.primitiveRoot == target.primitiveRoot;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + Integer.hashCode(this.prime);
        result = 31 * result + Integer.hashCode(this.primitiveRoot);

        return result;
    }

    @Override
    public String toString() {
        return String.format("[prime:%d, primitiveRoot:%d]", this.prime, this.primitiveRoot);
    }

    /**
     * 3以上の素数を与えて, その素数に関する原始根の振る舞いを返す.
     * 
     * <p>
     * 素数判定は行わない. <br>
     * 合成数を与えた場合の動作は保証されない.
     * </p>
     * 
     * @param prime 3以上の素数
     * @return 素数の原始根
     * @throws IllegalArgumentException 引数が3以上の奇数でない場合, primeが合成数と予測される場合
     *             (ただし, 合成数ならば必ずスローされるわけではない)
     */
    public static PrimitiveRootOfPrimeNumber of(int prime) {
        if (!(prime >= 3 && (prime & 1) == 1)) {
            throw new IllegalArgumentException("引数が3以上の奇数でない");
        }

        return new PrimitiveRootOfPrimeNumber(prime, new MinimumPrimitiveRootSearch(prime).primitiveRoot());
    }

}
