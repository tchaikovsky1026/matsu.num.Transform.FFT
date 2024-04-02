/**
 * 高速Fourier変換(Fast Fourier transform, FFT)に関するコンポーネントを扱うモジュール.
 * 
 * <p>
 * {@link matsu.num.transform.fft.service.FFTModuleExecutorProvider} を経由して生成することが,
 * このモジュールが提供する各エグゼキュータの標準的な利用法である.
 * </p>
 * 
 * <p>
 * <i>必須モジュール:</i> <br>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
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