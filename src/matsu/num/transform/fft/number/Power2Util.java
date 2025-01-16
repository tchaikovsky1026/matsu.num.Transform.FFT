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

/**
 * 2のべき乗の整数に関する計算補助.
 * 
 * @author Matsuura Y.
 */
public final class Power2Util {
    private Power2Util() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 整数が2の累乗かどうかを判定する.
     * 
     * @param n n
     * @return nが2の累乗ならtrue
     * @throws IllegalArgumentException nが0以下の場合
     */
    public static boolean isPowerOf2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("自然数(1以上)でない");
        }
        return (n & (n - 1)) == 0;
    }

    /**
     * 整数nに対する log <sub>2</sub> n (切り捨て)を計算する.
     * 
     * @param n n
     * @return log <sub>2</sub> n
     * @throws IllegalArgumentException nが0以下の場合
     */
    public static int floorLog2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("自然数(1以上)でない");
        }

        //floor(log_2(maxExp))
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    /**
     * 整数nに対して, 2のべき乗になるように切り上げる.
     * 
     * @param n n
     * @return 切り上げられた値
     * @throws IllegalArgumentException nが0以下の場合, nが2^30を超える場合
     */
    public static int ceilToPower2(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("自然数(1以上)でない");
        }

        if (n > 0x4000_0000) {
            throw new IllegalArgumentException("2の30乗を超えている");
        }

        return Power2Util.isPowerOf2(n)
                ? n
                : (Integer.highestOneBit(n) << 1);
    }
}
