/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.transform.fft.fftmodule;

import matsu.num.transform.fft.component.ComplexNumber;
import matsu.num.transform.fft.component.FourierBasis;
import matsu.num.transform.fft.component.FourierBasisComputer;
import matsu.num.transform.fft.number.PrimitiveRootOfPrimeNumber;

/**
 * 3以上の素数個の標本サイズのFFTを実行する.
 * 
 * <p>
 * このクラスが提供する{@link InnerDFTExecutor#compute(ComplexNumber[], FourierBasisComputer)}の事前条件(引数の条件)は以下である.
 * </p>
 * 
 * <p>
 * 事前条件の1番目は, 標本サイズが大きすぎないことである. <br>
 * 標本サイズが{@link #MAX_DATA_SIZE}を超過した場合は{@link IllegalArgumentException}をスローする.
 * </p>
 * 
 * <p>
 * 事前条件の2番目は, データのサイズは3以上の素数であることである. <br>
 * これの不完全な検証として, 標本サイズが3以上の奇数でない場合は{@link IllegalArgumentException}をスローする.
 * <br>
 * 3以上の奇数で合成数である場合は, 例外がスローされる場合とされない場合がある. <br>
 * ただし, 例外がスローされなかったとしても, 事後条件(戻り値が正しい結果であること)は保証されない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
public final class PrimeInnerFFTExecutor implements InnerDFTExecutor {

    /**
     * 扱うことができるデータサイズの最大値: 2<sup>28</sup>
     */
    public static final int MAX_DATA_SIZE = CyclicConvolutionModule.MAX_SEQUENCE_SIZE;

    private final CyclicConvolutionModule cyclicConvolution;

    /**
     * このクラスの機能を実行するインスタンスを返す.
     * 
     * @param computerSuppier Fourier基底コンピュータのサプライヤ
     * @throws NullPointerException 引数にnullが含まれる場合
     */
    public PrimeInnerFFTExecutor(FourierBasisComputer.Supplier computerSuppier) {
        super();
        this.cyclicConvolution = new CyclicConvolutionModule(computerSuppier);
    }

    /**
     * @throws IllegalArgumentException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public ComplexNumber[] compute(ComplexNumber[] data, FourierBasisComputer basisComputer) {
        if (data.length > MAX_DATA_SIZE) {
            throw new IllegalArgumentException("サイズが大きすぎる");
        }

        int N = data.length;
        FourierBasis basis = basisComputer.getBasis(N);

        //ここで3以上の素数かどうか, 素数かどうかを判定する
        PrimitiveRootOfPrimeNumber primitiveRoot = PrimitiveRootOfPrimeNumber.of(N);

        ComplexNumber[] out = new ComplexNumber[N];

        /* A(0) */
        out[0] = ComplexNumber.sum(data);

        /* 巡回畳み込みの準備 */
        //a
        ComplexNumber[] a = new ComplexNumber[N - 1];
        // 原始根の累乗をaに代入する, N-1のみ別処理(powerメソッドの引数要件の都合)
        // Nは3以上なのでdata[1]は成功
        a[0] = data[1];
        for (int j = 1; j < N - 1; j++) {
            a[j] = data[primitiveRoot.power(N - 1 - j)];
        }

        //W
        ComplexNumber[] w = new ComplexNumber[N - 1];
        // 原始根の累乗をaに代入する, N-1のみ別処理(powerメソッドの引数要件の都合)
        // Nは3以上なのでdata[1]は成功
        for (int j = 0; j < N - 1; j++) {
            w[j] = basis.valueAt(primitiveRoot.power(j));
        }

        ComplexNumber[] y = this.cyclicConvolution.compute(a, w);
        for (int q = 0; q < N - 1; q++) {
            ComplexNumber A_q = y[q].plus(data[0]);
            out[primitiveRoot.power(q)] = A_q;
        }

        return out;
    }
}
