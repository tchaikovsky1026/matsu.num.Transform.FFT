/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.skeletal;

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
 * データサイズに制約があるエグゼキュータを構築するためのヘルパ抽象クラス.
 * </p>
 * 
 * <p>
 * このクラスは, データサイズがacceptedかrejectedを判定する
 * {@link #acceptsSize(int)} の提供が主務である. <br>
 * サイズが {@link #requiredSize()} 未満,
 * {@link #upperLimitSize()} 超過の場合はrejectされる.
 * </p>
 * 
 * <ul>
 * <li>{@link #requiredSize()} のデフォルト値: 0</li>
 * <li>{@link #upperLimitSize()} のデフォルト値: {@code Integer.MAX_VALUE}</li>
 * </ul>
 * 
 * <p>
 * {@link #requiredSize()}, {@link #upperLimitSize()}
 * はオーバーライドされることは想定されている.
 * </p>
 * 
 * <p>
 * データサイズの範囲以外の複雑なreject条件の場合は,
 * {@link #addRejectionContract(IntPredicate, StructureAcceptance)}
 * で追加することで,
 * {@link #acceptsSize(int)} により判定が可能になる.
 * </p>
 * 
 * <p>
 * <u><i>実装契約</i></u> <br>
 * {@link #addRejectionContract(IntPredicate, StructureAcceptance)}
 * は, コンストラクタ内(あるいはイニシャライザ内)でのみ呼ぶことが許される.
 * </p>
 * 
 * @author Matsuura Y.
 * @deprecated データサイズの契約を抽象クラスにするのは構造が複雑になるので, 分離を行った. このクラスは使われていない.
 */
@Deprecated
abstract class DataSizeContract {

    private final List<RejectionContract> contract;

    /**
     * 唯一のコンストラクタ.
     */
    protected DataSizeContract() {
        this.contract = new ArrayList<>();
        this.addRejectionContract(
                size -> size < this.requiredSize(),
                StructureRejected.by(
                        () -> new NotRequiredDataSizeException("データサイズが必要サイズに満たない"), "REJECT_BY_NOT_REQUIRED_SIZE"));
        this.addRejectionContract(
                size -> size > this.upperLimitSize(),
                StructureRejected.by(
                        () -> new DataSizeTooLargeException("データサイズが大きすぎる"), "REJECT_BY_TOO_LARGE_SIZE"));
    }

    /**
     * 与えたサイズがrejectかどうかを判定する.
     * 
     * @param size サイズ
     * @return 判定結果
     */
    protected final StructureAcceptance acceptsSize(int size) {
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
    protected final void addRejectionContract(IntPredicate isRejected, StructureAcceptance rejection) {
        if (rejection == StructureAcceptance.ACCEPTED) {
            throw new IllegalArgumentException("acceptanceにACCEPTEDが渡されている");
        }

        this.contract.add(new RejectionContract(isRejected, rejection));
    }

    /**
     * {@link #acceptsSize(int)} でacceptされるために必要なデータサイズの最小値を返す. <br>
     * オーバーライドする場合は0以上の値を返さなければならない.
     * 
     * @return acceptされるために必要なデータサイズの最小値
     */
    protected int requiredSize() {
        return 0;
    }

    /**
     * {@link #acceptsSize(int)} でacceptされるデータサイズの最大値を返す. <br>
     * オーバーライドする場合は {@link #requiredSize()} 以上の値を返さなければならない.
     * 
     * @return acceptされるデータサイズの最大値
     */
    protected int upperLimitSize() {
        return Integer.MAX_VALUE;
    }

    /**
     * reject判定器と
     */
    private static final class RejectionContract {

        final IntPredicate isRejected;
        final StructureAcceptance rejection;

        RejectionContract(IntPredicate isRejected, StructureAcceptance rejection) {
            super();
            this.isRejected = Objects.requireNonNull(isRejected);
            this.rejection = Objects.requireNonNull(rejection);
        }
    }
}
