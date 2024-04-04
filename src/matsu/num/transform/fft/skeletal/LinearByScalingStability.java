/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.skeletal;

import java.util.Arrays;
import java.util.Objects;

import matsu.num.transform.fft.LinearTransform;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.validation.StructureAcceptance;

/**
 * 全単射の実数列の線形変換において,
 * 引数のスケーリングによる前処理, 後処理によって計算安定性を高めた抽象クラス.
 * 
 * <p>
 * この抽象クラスで{@link #apply(double[])}メソッドを実装しており,
 * 引数のチェック, スケーリングと復元を行っている. <br>
 * 不正値(inf, NaN)が混入している場合は{@code Double#NaN}で埋めてreturnされる. <br>
 * このクラスの継承先では{@link #applyInner(double[])}を素朴に実装すればよい
 * (詳細はメソッド説明を参照).
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public abstract class LinearByScalingStability
        extends DataSizeContract implements LinearTransform {

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
    protected LinearByScalingStability(int requiredSize, int upperLimitSize, ArraysUtil arraysUtil) {
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
    protected LinearByScalingStability(int upperLimitSize, ArraysUtil arraysUtil) {
        this(1, upperLimitSize, arraysUtil);
    }

    @Override
    public final StructureAcceptance accepts(double[] data) {
        return this.acceptsSize(data.length);
    }

    @Override
    protected final int requiredSize() {
        return this.requiredSize;
    }

    @Override
    protected final int upperLimitSize() {
        return this.upperLimitSize;
    }

    @Override
    public final double[] apply(double[] data) {
        StructureAcceptance acceptance = this.accepts(data);
        if (!acceptance.isAcceptState()) {
            throw acceptance.getException();
        }

        double[] cloneData = data.clone();
        int size = cloneData.length;
        double scale = this.arraysUtil.normMax(cloneData);

        //不正な値が入っている場合はNaNにしてreturn
        if (!Double.isFinite(scale)) {
            //cloneData配列を使いまわす
            Arrays.fill(cloneData, Double.NaN);
            return cloneData;
        }
        //スケールを正規化し, dataをスケーリング
        scale = scale == 0d ? 1d : scale;
        for (int j = 0; j < size; j++) {
            cloneData[j] /= scale;
        }

        double[] out = this.applyInner(cloneData);
        for (int k = 0; k < size; k++) {
            out[k] *= scale;
        }

        return out;
    }

    /**
     * {@link #apply(double[])}から呼ばれる, 線形変換を実行する抽象メソッド. <br>
     * 外部から呼ばれることを想定されていない.
     * 
     * <p>
     * 内部から呼ばれた場合, 引数はacceptされていることが保証されているので,
     * 例外をスローしてはいけない. <br>
     * また, 引数はスケーリングされている.
     * </p>
     * 
     * <p>
     * {@link #apply(double[])}内部で配列の複製を行っている. <br>
     * したがって, このメソッド内で配列を複製する必要はない. <br>
     * また, 次のことも許される.
     * </p>
     * 
     * <ul>
     * <li>引数と同じ参照を持つオブジェクトを返しても良い.</li>
     * <li>引数の内部の値を書き換えても良い.</li>
     * </ul>
     * 
     * @param data 実数列, lengthが1以上であることが保証された配列
     * @return 変換後の実数列(入力と同じ長さである)
     */
    protected abstract double[] applyInner(double[] data);

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
        return "LinearTransform";
    }
}
