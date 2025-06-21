/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.transform.fft.behavior;

import org.junit.Ignore;

import matsu.num.transform.fft.DFTExecutor;
import matsu.num.transform.fft.dto.ComplexNumberArrayDTO;
import matsu.num.transform.fft.service.CommonLib;
import matsu.num.transform.fft.service.DftExecutors;
import matsu.num.transform.fft.service.FFTModuleExecutorProvider;

/**
 * {@link GenericDFTExecutor}のパフォーマンスに関する検証.
 */
@Ignore
final class GenericDFTPerformance {

    private static final int BASE_ITERATION = 400;

    private final DFTExecutor dftExecutor =
            FFTModuleExecutorProvider.by(CommonLib.defaultImplemented())
                    .get(DftExecutors.GENERIC_DFT_EXECUTOR);

    private final int minLength;
    private final int maxLength;

    private final int interval;

    public static void main(String[] args) {
        new GenericDFTPerformance(5, 4000, 1).execute();
    }

    public GenericDFTPerformance(int minLength, int maxLength) {
        this(minLength, maxLength, 1);
    }

    public GenericDFTPerformance(int minLength, int maxLength, int interval) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.interval = interval;

        if (maxLength < minLength) {
            throw new IllegalArgumentException("min > max");
        }

        if (interval <= 0) {
            throw new IllegalArgumentException("interval <= 0");
        }
    }

    public void execute() {

        this.idling();

        System.out.println();
        System.out.println("size\tcalcTime (us)");
        for (int size = minLength; size <= maxLength; size += interval) {
            double result = this.singlePerformanceTimeAtMicroSecond(size);

            System.out.println(size + "\t" + result);
        }

        System.out.println();
    }

    public void idling() {

        for (int size = minLength; size <= maxLength; size += interval) {
            ComplexNumberArrayDTO data = ComplexNumberArrayDTO.zeroFilledOf(size);
            this.dftExecutor.apply(data);
        }
    }

    public double singlePerformanceTimeAtMicroSecond(int size) {
        //パフォーマンス測定

        ComplexNumberArrayDTO data = ComplexNumberArrayDTO.zeroFilledOf(size);

        int iteration = 1 + BASE_ITERATION * 100 / size;

        int dummy = 0;
        long startNano = System.nanoTime();
        for (int c = 0; c < iteration; c++) {
            ComplexNumberArrayDTO result = this.dftExecutor.apply(data);
            dummy += result.size;
        }
        long endNano = System.nanoTime();

        return dummy > 0
                ? (double) (endNano - startNano) / iteration / 1000
                : 0;
    }
}
