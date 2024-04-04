/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.dft.impl;

import matsu.num.transform.fft.GenericIDFTExecutor;
import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.component.FourierType;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.fftmodule.GenericInnerFFTExecutor;
import matsu.num.transform.fft.lib.Trigonometry;
import matsu.num.transform.fft.lib.privatelib.ArraysUtil;
import matsu.num.transform.fft.skeletal.ComplexLinearByScalingStability;

/**
 * {@link GenericIDFTExecutor} の実装.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class GenericIDFTExecutorImpl
        extends ComplexLinearByScalingStability implements GenericIDFTExecutor {

    private static final FourierType TYPE = FourierType.IDFT;

    private final FourierBasisComputer.Supplier computerSupplier;
    private final GenericInnerFFTExecutor innerDFTExecutor;

    /**
     * IDFTExecutorを構築する.
     * 
     * @param trigonometry 三角関数ライブラリ
     * @param arraysUtil 配列ユーティリティ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public GenericIDFTExecutorImpl(Trigonometry trigonometry, ArraysUtil arraysUtil) {
        super(MAX_DATA_SIZE, arraysUtil);
        this.computerSupplier = new FourierBasisComputer.Supplier(trigonometry);
        this.innerDFTExecutor = new GenericInnerFFTExecutor(this.computerSupplier);
    }

    /**
     * 外部からの呼び出し不可.
     */
    @Override
    protected ComplexNumberArrayDTO applyInner(ComplexNumberArrayDTO complexNumberArray) {
        int size = complexNumberArray.size;

        //複素数の配列
        ComplexNumber[] data =
                ComplexNumber.from(complexNumberArray.realPart, complexNumberArray.imaginaryPart);

        //変換を実行
        ComplexNumber[] result = this.innerDFTExecutor
                .compute(data, this.computerSupplier.covering(data.length, TYPE));
        double[][] resultArray = ComplexNumber.separateToArrays(result);

        //DTOに
        ComplexNumberArrayDTO out = ComplexNumberArrayDTO.zeroFilledOf(size);
        System.arraycopy(resultArray[0], 0, out.realPart, 0, size);
        System.arraycopy(resultArray[1], 0, out.imaginaryPart, 0, size);

        return out;
    }

    @Override
    public String toString() {
        return "GenericIDFTExecutor";
    }
}
