/**
 * 2024.4.2
 */
package matsu.num.transform.fft.skeletal;

import java.util.Arrays;
import java.util.Objects;

import matsu.num.transform.fft.ComplexLinearTransform;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 全単射の複素数列の線形変換において,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで {@link #apply(ComplexNumberArrayDTO)} メソッドを実装しており,
 * 引数のacceptチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@link #applyInner(ComplexNumberArrayDTO)}を素朴に実装すればよい.
 * </p>
 * 
 * <p>
 * この抽象クラスで {@link #accepts(ComplexNumberArrayDTO)},
 * {@link #acceptsReal(double[])} メソッドを,
 * {@link DataSizeContract#acceptsSize(int)} と連動するように実装している.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
public abstract class ComplexLinearByScalingStability
        extends DataSizeContract implements ComplexLinearTransform {

    private final int requiredSize;
    private final int upperLimitSize;
    private final ArraysUtil arraysUtil;

    /**
     * 許容されるデータサイズを与えるコンストラクタ.
     * 
     * @param requiredSize acceptされるために必要なデータサイズの最小値
     * @param upperLimitSize acceptされるデータサイズの最大値
     * @param arraysUtil 配列ユーティリティ
     * @throws IllegalArgumentException requiredSizeが1以上でない場合,
     *             upperLimitSizeがrequiredSize以上でない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    protected ComplexLinearByScalingStability(int requiredSize, int upperLimitSize, ArraysUtil arraysUtil) {
        super();

        if (requiredSize < 1 || upperLimitSize < requiredSize) {
            throw new IllegalArgumentException("引数が規約を満たしていない");
        }

        this.requiredSize = requiredSize;
        this.upperLimitSize = upperLimitSize;
        this.arraysUtil = Objects.requireNonNull(arraysUtil);
    }

    /**
     * acceptされるデータサイズの最大値を与えるコンストラクタ. <br>
     * requiredSizeは1である.
     * 
     * @param upperLimitSize acceptされるデータサイズの最大値
     * @param arraysUtil 配列ユーティリティ
     * @throws IllegalArgumentException upperLimitSizeが1以上でない場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    protected ComplexLinearByScalingStability(int upperLimitSize, ArraysUtil arraysUtil) {
        this(1, upperLimitSize, arraysUtil);
    }

    @Override
    public final StructureAcceptance accepts(ComplexNumberArrayDTO complexNumberArray) {
        return this.acceptsSize(complexNumberArray.size);
    }

    @Override
    public final StructureAcceptance acceptsReal(double[] realNumberData) {
        return this.acceptsSize(realNumberData.length);
    }

    /**
     * 外部から呼ぶことはできない.
     */
    @Override
    protected final int requiredSize() {
        return this.requiredSize;
    }

    /**
     * 外部から呼ぶことはできない.
     */
    @Override
    protected final int upperLimitSize() {
        return this.upperLimitSize;
    }

    @Override
    public final ComplexNumberArrayDTO apply(ComplexNumberArrayDTO complexNumberArray) {
        StructureAcceptance acceptance = this.accepts(complexNumberArray);
        if (!acceptance.isAcceptState()) {
            throw acceptance.getException();
        }

        int size = complexNumberArray.size;

        //データのクローン
        ComplexNumberArrayDTO cloneData = ComplexNumberArrayDTO.zeroFilledOf(size);
        System.arraycopy(complexNumberArray.realPart, 0, cloneData.realPart, 0, size);
        System.arraycopy(complexNumberArray.imaginaryPart, 0, cloneData.imaginaryPart, 0, size);

        double[] cloneRealPart = cloneData.realPart;
        double[] cloneImaginaryPart = cloneData.imaginaryPart;

        double scale = Math.max(
                this.arraysUtil.normMax(cloneRealPart), this.arraysUtil.normMax(cloneImaginaryPart));

        //不正な値が入っている場合はNaNにしてreturn
        if (!Double.isFinite(scale)) {
            Arrays.fill(cloneRealPart, Double.NaN);
            Arrays.fill(cloneImaginaryPart, Double.NaN);
            return cloneData;
        }
        //スケールを正規化し, dataをスケーリング
        scale = scale == 0d ? 1d : scale;
        for (int j = 0; j < size; j++) {
            cloneRealPart[j] /= scale;
            cloneImaginaryPart[j] /= scale;
        }

        ComplexNumberArrayDTO out = this.applyInner(cloneData);
        double[] outRealPart = out.realPart;
        double[] outImaginaryPart = out.imaginaryPart;
        for (int k = 0; k < size; k++) {
            outRealPart[k] *= scale;
            outImaginaryPart[k] *= scale;
        }

        return out;
    }

    @Override
    public final ComplexNumberArrayDTO applyReal(double[] realNumberData) {
        StructureAcceptance acceptance = this.acceptsReal(realNumberData);
        if (!acceptance.isAcceptState()) {
            throw acceptance.getException();
        }

        ComplexNumberArrayDTO complexNumberArray = ComplexNumberArrayDTO.zeroFilledOf(realNumberData.length);
        System.arraycopy(realNumberData, 0, complexNumberArray.realPart, 0, realNumberData.length);
        return this.apply(complexNumberArray);
    }

    /**
     * {@link #apply(ComplexNumberArrayDTO)}から呼ばれる, 線形変換を実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数はacceptされていることが保証されているので,
     * 例外をスローしてはいけない. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * <p>
     * {@link #apply(ComplexNumberArrayDTO)}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param complexNumberArray 複素数列, sizeが1以上であることが保証された配列
     * @return 変換後の複素数列(入力と同じ長さである)
     */
    protected abstract ComplexNumberArrayDTO applyInner(ComplexNumberArrayDTO complexNumberArray);

    /**
     * このインスタンスの文字列表現を提供する.
     * 
     * <p>
     * バージョン間の整合性は担保されていない.
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return "ComplexLinearTransform";
    }

}
