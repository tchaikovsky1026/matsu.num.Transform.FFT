/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.26
 */
package matsu.num.transform.fft.component;

import java.util.Arrays;
import java.util.Objects;

import matsu.num.transform.fft.BiLinearTransform;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.validation.DataSizeNotMismatchException;
import matsu.num.transform.fft.validation.StructureAcceptance;
import matsu.num.transform.fft.validation.StructureRejected;

/**
 * 実数列の双線形変換において,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで{@link #apply(double[], double[])}メソッドを実装しており,
 * 引数のチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@link #applyInner(double[], double[])}を素朴に実装すればよい
 * (詳細はメソッド説明を参照).
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
public abstract non-sealed class BiLinearByScalingStability implements BiLinearTransform {

    protected final DataSizeContract dataSizeContract = new DataSizeContract();

    private static final StructureAcceptance REJECT_BY_SIZE_MISMATCH =
            StructureRejected.by(() -> new DataSizeNotMismatchException("データサイズが整合しない"), "REJECT_BY_SIZE_MISMATCH");

    private final ArraysUtil arraysUtil;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    protected BiLinearByScalingStability(ArraysUtil arraysUtil) {
        super();

        this.arraysUtil = Objects.requireNonNull(arraysUtil);
        this.dataSizeContract.bindRequiredSize(1);
    }

    @Override
    public final StructureAcceptance accepts(double[] f, double[] g) {
        int sizeF = f.length;
        int sizeG = g.length;
        if (sizeF != sizeG) {
            return REJECT_BY_SIZE_MISMATCH;
        }
        return this.dataSizeContract.acceptsSize(sizeF);
    }

    @Override
    public final double[] apply(double[] f, double[] g) {
        StructureAcceptance acceptance = this.accepts(f, g);
        if (!acceptance.isAcceptState()) {
            throw acceptance.getException();
        }

        int size = f.length;

        double[] cloneF = f.clone();
        double[] cloneG = g.clone();
        double scaleF = this.arraysUtil.normMax(cloneF);
        double scaleG = this.arraysUtil.normMax(cloneG);

        //不正な値が入っている場合,NaNで埋めてreturn
        if (!(Double.isFinite(scaleF) && Double.isFinite(scaleG))) {
            //cloneFの配列を使いまわす
            Arrays.fill(cloneF, Double.NaN);
            return cloneF;
        }

        //スケールを正規化し,cloneF,Gをスケーリング
        scaleF = scaleF == 0d ? 1d : scaleF;
        scaleG = scaleG == 0d ? 1d : scaleG;
        for (int j = 0; j < size; j++) {
            cloneF[j] /= scaleF;
            cloneG[j] /= scaleG;
        }

        double[] result = this.applyInner(cloneF, cloneG);
        double scaleFG = scaleF * scaleG;
        for (int j = 0; j < size; j++) {
            result[j] *= scaleFG;
        }

        return result;
    }

    /**
     * {@link #apply(double[], double[])}から呼ばれる,
     * 実数列の双線形変換を実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数はacceptされていることが保証されているので,
     * 例外をスローしてはいけない. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * <p>
     * {@link #apply(double[], double[])}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param f f
     * @param g g
     * @return 変換後の実数列(入力と同じ長さである)
     */
    protected abstract double[] applyInner(double[] f, double[] g);

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
        return "BiLinearTransform";
    }
}
