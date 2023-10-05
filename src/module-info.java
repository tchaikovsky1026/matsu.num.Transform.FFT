/**
 * 高速Fourier変換(Fast Fourier transform, FFT)に関するコンポーネントを扱うモジュール. 
 * 
 * <p>
 * <i>必須モジュール:</i> <br>
 * {@code matsu.num.Commons}
 * </p>
 * 
 * @author Matsuura Y.
 * @version 16.0
 */
module matsu.num.transform.FFT {

    requires matsu.num.Commons;
    
    exports matsu.num.transform.fft;
    exports matsu.num.transform.fft.dto;
    exports matsu.num.transform.fft.dctdst;
    exports matsu.num.transform.fft.convolution;
}