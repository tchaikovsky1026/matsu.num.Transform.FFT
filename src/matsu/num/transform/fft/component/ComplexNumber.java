/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.26
 */
package matsu.num.transform.fft.component;

import java.util.Objects;

/**
 * 複素数と関係する処理を扱う.
 * 
 * <p>
 * 実部と虚部の値に基づく等価性を提供する.
 * </p>
 * 
 * <p>
 * このクラスは生成時の契約や演算の精緻化に欠けているため, 外部に公開されることは想定されていない.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class ComplexNumber {

    /**
     * 複素数0を表す定数.
     */
    public static final ComplexNumber ZERO = ComplexNumber.of(0, 0);

    /**
     * 複素数1を表す定数.
     */
    public static final ComplexNumber ONE = ComplexNumber.of(1, 0);

    /**
     * 複素数iを表す定数.
     */
    public static final ComplexNumber I = ComplexNumber.of(0, 1);

    private final double real;
    private final double imaginary;

    private volatile Integer hashCode;

    private ComplexNumber(double real, double imaginary) {
        super();
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * 実部を返す.
     * 
     * @return 実部
     */
    public double real() {
        return this.real;
    }

    /**
     * 虚部を返す.
     * 
     * @return 虚部
     */
    public double imaginary() {
        return this.imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ComplexNumber)) {
            return false;
        }

        ComplexNumber target = (ComplexNumber) obj;

        return Double.compare(this.real, target.real) == 0
                && Double.compare(this.imaginary, target.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        Integer out = this.hashCode;
        if (Objects.nonNull(out)) {
            return out.intValue();
        }

        out = this.calcHashCode();
        this.hashCode = out;
        return out.intValue();
    }

    private int calcHashCode() {
        int result = 1;
        result = 31 * result + Double.hashCode(this.real);
        result = 31 * result + Double.hashCode(this.imaginary);
        return result;
    }

    /**
     * このオブジェクトの文字列表現を返す.
     * 
     * <p>
     * 文字列表現はバージョン間で整合するとは限らない. <br>
     * おそらく次のようなものだろう. <br>
     * {@code [real:%real, imaginary:%imaginary]}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "[real:%.16G, imaginary:%.16G]",
                this.real,
                this.imaginary);
    }

    /**
     * 和を計算する.
     * 
     * @param other 演算相手
     * @return 和
     */
    public ComplexNumber plus(ComplexNumber other) {
        return ComplexNumber.of(this.real + other.real, this.imaginary + other.imaginary);
    }

    /**
     * 積を計算する.
     * 
     * @param other 演算相手
     * @return 積
     */
    public ComplexNumber times(ComplexNumber other) {
        double a_re = this.real;
        double a_im = this.imaginary;
        double b_re = other.real;
        double b_im = other.imaginary;
        return ComplexNumber.of(a_re * b_re - a_im * b_im, a_re * b_im + a_im * b_re);
    }

    /**
     * 実数を乗算した結果を返す.
     * 
     * @param multiplier 乗数
     * @return 乗算結果
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public ComplexNumber timesReal(double multiplier) {
        return ComplexNumber.of(multiplier * this.real, multiplier * this.imaginary);
    }

    /**
     * 複素数を生成する.
     * 
     * @param real 実部
     * @param imaginary 虚部
     * @return 相当する複素数
     */
    public static ComplexNumber of(double real, double imaginary) {
        return new ComplexNumber(real, imaginary);
    }

    /**
     * 複素数列の総和を計算する. <br>
     * 空の場合は0が返る.
     * 
     * @param complexNumbers 複素数列
     * @return 総和
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static ComplexNumber sum(ComplexNumber... complexNumbers) {

        double re = 0;
        double im = 0;

        for (ComplexNumber c : complexNumbers) {
            re += c.real;
            im += c.imaginary;
        }

        return ComplexNumber.of(re, im);
    }

    /**
     * 2系列の複素数列の成分積の総和を計算する. <br>
     * 空の場合は0が返る.
     * 
     * @param a
     * @param b
     * @return 総和
     * @throws IllegalArgumentException 引数の長さが異なる場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static ComplexNumber sumProduct(ComplexNumber[] a, ComplexNumber[] b) {

        if (a.length != b.length) {
            throw new IllegalArgumentException("サイズが一致しない");
        }

        double re = 0;
        double im = 0;

        for (int j = 0, len = a.length; j < len; j++) {
            ComplexNumber a_j = a[j];
            ComplexNumber b_j = b[j];

            double a_j_re = a_j.real;
            double a_j_im = a_j.imaginary;
            double b_j_re = b_j.real;
            double b_j_im = b_j.imaginary;

            re += a_j_re * b_j_re - a_j_im * b_j_im;
            im += a_j_re * b_j_im + a_j_im * b_j_re;

        }

        return ComplexNumber.of(re, im);
    }

    /**
     * 実部を表す配列と虚部を表す配列から複素数列を作成する. <br>
     * 配列の長さが等しいことが必要である.
     * 
     * @param realPart 実部配列
     * @param imaginaryPart 虚部配列
     * @return 複素数列
     * @throws IllegalArgumentException 配列のサイズが整合しない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static ComplexNumber[] from(double[] realPart, double[] imaginaryPart) {

        int size = realPart.length;
        if (size != imaginaryPart.length) {
            throw new IllegalArgumentException("サイズが整合しない");
        }

        ComplexNumber[] array = new ComplexNumber[size];
        for (int i = 0; i < size; i++) {
            array[i] = ComplexNumber.of(realPart[i], imaginaryPart[i]);
        }

        return array;
    }

    /**
     * 複素数列を実部配列と虚部配列に分離する. <br>
     * 戻り値は{@code double[2][size]}であり,
     * 第0配列が実部, 第1配列が虚部である.
     * 
     * 
     * @param complexNumbers 複素数列
     * @return 実部と虚部の配列
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public static double[][] separateToArrays(ComplexNumber[] complexNumbers) {

        int size = complexNumbers.length;
        double[][] out = new double[2][size];
        double[] outReal = out[0];
        double[] outImaginary = out[1];

        for (int i = 0; i < size; i++) {
            ComplexNumber v = complexNumbers[i];
            outReal[i] = v.real;
            outImaginary[i] = v.imaginary;
        }

        return out;
    }
}
