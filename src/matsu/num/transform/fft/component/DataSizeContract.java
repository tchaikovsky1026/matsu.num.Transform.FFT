/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.1
 */
package matsu.num.transform.fft.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntPredicate;

import matsu.num.transform.fft.validation.DataSizeTooLargeException;
import matsu.num.transform.fft.validation.NotRequiredDataSizeException;
import matsu.num.transform.fft.validation.StructureAcceptance;
import matsu.num.transform.fft.validation.StructureRejected;

/**
 * <p>
 * データサイズの制約 (契約) を表現する.
 * </p>
 * 
 * <p>
 * このクラスは, データサイズがacceptedかrejectedを判定する
 * {@link #acceptsSize(int)} の提供が主務である. <br>
 * 基本の契約として, {@code requiredSize}, {@code upperLimitSize} が設定されており,
 * {@code requiredSize <= size <= upperLimitSize} の場合にacceptedになる.
 * </p>
 * 
 * <ul>
 * <li>{@code requiredSize} のデフォルト値: 0</li>
 * <li>{@code upperLimitSize} のデフォルト値: {@code Integer.MAX_VALUE}</li>
 * </ul>
 * 
 * <p>
 * データサイズの範囲以外の複雑なreject条件の場合は,
 * {@link #addRejectionContract(IntPredicate, StructureAcceptance)}
 * で追加することで,
 * {@link #acceptsSize(int)} により判定が可能になる.
 * </p>
 * 
 * <p>
 * セッターに相当する
 * {@link #bindRequiredSize(int)},
 * {@link #bindUpperLimitSize(int)},
 * {@link #addRejectionContract(IntPredicate, StructureAcceptance)}
 * が呼ばれる状況ではスレッドセーフでないことに注意が必要である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
public final class DataSizeContract {

    private final List<RejectionContract> contract;

    private int requiredSize = 0;
    private int upperLimitSize = Integer.MAX_VALUE;

    /**
     * 唯一のコンストラクタ.
     */
    public DataSizeContract() {
        this.contract = new ArrayList<>();
        this.addRejectionContract(
                size -> size < this.requiredSize,
                StructureRejected.by(
                        () -> new NotRequiredDataSizeException("データサイズが必要サイズに満たない"), "REJECT_BY_NOT_REQUIRED_SIZE"));
        this.addRejectionContract(
                size -> size > this.upperLimitSize,
                StructureRejected.by(
                        () -> new DataSizeTooLargeException("データサイズが大きすぎる"), "REJECT_BY_TOO_LARGE_SIZE"));
    }

    /**
     * 与えたサイズがrejectかどうかを判定する.
     * 
     * @param size サイズ
     * @return 判定結果
     */
    public final StructureAcceptance acceptsSize(int size) {
        for (RejectionContract rc : this.contract) {
            if (rc.isRejected.test(size)) {
                return rc.rejection;
            }
        }

        return StructureAcceptance.ACCEPTED;
    }

    /**
     * rejectの契約を追加する.
     * 
     * <p>
     * このメソッドは, エグゼキュータのコンストラクタ(あるいはイニシャライザ)の中でしか呼んではいけない.
     * </p>
     * 
     * @param isRejected sizeがrejectかどうかを判定する判定器
     * @param rejection rejectの場合に戻すべきStructureAcceptance
     * @throws IllegalArgumentException rejectionにACCEPTEDが渡された場合
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public final void addRejectionContract(IntPredicate isRejected, StructureAcceptance rejection) {
        this.contract.add(new RejectionContract(isRejected, rejection));
    }

    /**
     * {@link #acceptsSize(int)} でacceptされるために必要なデータサイズの最小値に関する制限を強める
     * (最小値をより大きい値に変更する).
     * 
     * <p>
     * 新しい値は, 現状の [requiredSize, upperLimitSize] に含まれなければならない.
     * </p>
     * 
     * @param requiedSize acceptされるために必要なデータサイズの最小値
     * @throws IllegalArgumentException 引数が不適の場合
     */
    public void bindRequiredSize(int requiedSize) {
        if (this.requiredSize > requiedSize || this.upperLimitSize < requiedSize) {
            throw new IllegalArgumentException(
                    String.format(
                            "引数が不適: %s notin [%s, %s]",
                            requiedSize, this.requiredSize, this.upperLimitSize));
        }

        this.requiredSize = requiedSize;
    }

    /**
     * {@link #acceptsSize(int)} でacceptされるために必要なデータサイズの最大値に関する制限を強める
     * (最大値をより小さい値に変更する).
     * 
     * <p>
     * 新しい値は, 現状の [requiredSize, upperLimitSize] に含まれなければならない.
     * </p>
     * 
     * @param upperLimitSize acceptされるために必要なデータサイズの最大値
     * @throws IllegalArgumentException 引数が不適の場合
     */
    public void bindUpperLimitSize(int upperLimitSize) {
        if (this.requiredSize > upperLimitSize || this.upperLimitSize < upperLimitSize) {
            throw new IllegalArgumentException(
                    String.format(
                            "引数が不適: %s notin [%s, %s]",
                            upperLimitSize, this.requiredSize, this.upperLimitSize));
        }

        this.upperLimitSize = upperLimitSize;
    }

    /**
     * reject判定器とrejectになった場合の原因を扱う構造体.
     */
    private static final class RejectionContract {

        final IntPredicate isRejected;
        final StructureAcceptance rejection;

        /**
         * @throws IllegalArgumentException rejectionにACCEPTEDが渡された場合
         * @throws NullPointerException 引数にnullが含まれる場合
         */
        RejectionContract(IntPredicate isRejected, StructureAcceptance rejection) {
            super();
            this.isRejected = Objects.requireNonNull(isRejected);
            this.rejection = Objects.requireNonNull(rejection);

            if (rejection == StructureAcceptance.ACCEPTED) {
                throw new IllegalArgumentException("acceptanceにACCEPTEDが渡されている");
            }
        }
    }
}
