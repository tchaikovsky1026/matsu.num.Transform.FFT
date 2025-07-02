/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * 高速Fourier変換(Fast Fourier transform, FFT) に関する機能を提供するモジュール.
 * 
 * <p>
 * このモジュールは, 各計算
 * (離散Fourier変換, 離散cosine &middot; sine変換, 巡回畳み込み 等)
 * の実行を表現するインターフェースが定義されたパッケージと, <br>
 * そのインターフェースを実装した具象クラスのインスタンスを取得するためのサービスパッケージから構成されている.
 * </p>
 * 
 * <p>
 * 各計算を実行するインターフェースは,
 * 多くは {@code XxxExecutor}
 * といったインターフェース名である.
 * </p>
 * 
 * <p>
 * {@link matsu.num.transform.fft.service} パッケージには,
 * 前述の計算実行インスタンスを取得するための仕組みが用意されている. <br>
 * ユーザーは
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider}
 * のメソッドを経由して各インスタンスを取得する.
 * </p>
 * 
 * <p>
 * 詳しくは, 各パッケージの説明文を参照すること.
 * </p>
 * 
 * <p>
 * <i>依存モジュール:</i> <br>
 * (無し)
 * </p>
 * 
 * @author Matsuura Y.
 * @version 24.3.0
 */
module matsu.num.Transform.FFT {

    exports matsu.num.transform.fft;
    exports matsu.num.transform.fft.convolution;
    exports matsu.num.transform.fft.dctdst;
    exports matsu.num.transform.fft.dto;
    exports matsu.num.transform.fft.lib;
    exports matsu.num.transform.fft.service;
    exports matsu.num.transform.fft.validation;
}
