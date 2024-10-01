/**
 * 高速Fourier変換(Fast Fourier transform, FFT)に関する機能を提供するモジュール.
 * 
 * <p>
 * 各機能 (エグゼキュータ) はインターフェースとして公開されている. <br>
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider}
 * のメソッドを経由して各エグゼキュータのインスタンスを取得するのが,
 * 標準的な利用方法である.
 * </p>
 * 
 * <p>
 * <i>必須モジュール:</i> <br>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 21.1
 */
module matsu.num.transform.FFT {

    exports matsu.num.transform.fft;
    exports matsu.num.transform.fft.convolution;
    exports matsu.num.transform.fft.dctdst;
    exports matsu.num.transform.fft.dto;
    exports matsu.num.transform.fft.lib;
    exports matsu.num.transform.fft.service;
    exports matsu.num.transform.fft.validation;
}