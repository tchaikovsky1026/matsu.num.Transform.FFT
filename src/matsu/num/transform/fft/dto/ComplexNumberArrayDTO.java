/**
 * 2023.9.29
 */
package matsu.num.transform.fft.dto;

/**
 * 複素数列を表現するDTO.
 * 
 * <p>
 * 複素数列を実部と虚部を分けて{@code double[]}により表現する. <br>
 * 必然的にそれらのは同一の長さであり, これを「複素数列の長さ」という. <br>
 * 複素数列の長さは0以上である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 12.6
 */
public final class ComplexNumberArrayDTO {

    /**
     * この複素数列の長さ.
     */
    public final int size;

    /**
     * この複素数列の実部を表す配列.
     */
    public final double[] realPart;
    
    /**
     * この複素数列の虚部を表す配列.
     */
    public final double[] imaginaryPart;

    /**
     * @throws IllegalArgumentException sizeが0以上でない場合
     */
    private ComplexNumberArrayDTO(int size) {
        if(size < 0) {
            throw new IllegalArgumentException("sizeが0以上でない");
        }
        this.size = size;
        this.realPart = new double[size];
        this.imaginaryPart = new double[size];
    }

    /**
     * 長さを指定して, 0埋めされた複素数列を生成する.
     * 
     * @param size 複素数列の長さ
     * @return 指定した長さを持つ, 0埋めされた複素数列
     * @throws IllegalArgumentException sizeが0以上でない場合
     */
    public static ComplexNumberArrayDTO zeroFilledOf(int size) {
        return new ComplexNumberArrayDTO(size);
    }

}
