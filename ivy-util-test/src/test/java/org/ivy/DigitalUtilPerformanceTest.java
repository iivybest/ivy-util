package org.ivy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/5/6 10:09
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class DigitalUtilPerformanceTest {

    private static final int DATA = 3;
    private static final int LENGTH = 8;
    private static final char MASK = '&';

    private static final int COUNT_BENCH = 1_000_000;

    @BeforeAll
    public static void setUp() {
        log.info("===={msg: {}}", "performance test");
    }


    @Test
    public void test_00_warmUp() {
        int i = 0;
        String num;
        while (i++ < 1_000_000) {
            num = DigitalUtil.getFormatNo(DATA, LENGTH, MASK);
            if(i == 1) {
                log.debug("{no: {}, method: {}}", num, "geFormatNo");
            }
            num = DigitalUtil.getFormatNo2(DATA, LENGTH, MASK);
            if(i == 1) {
                log.debug("{no: {}, method: {}}", num, "geFormatNo2");
            }
            num = DigitalUtil.getFormatNo3(DATA, LENGTH, MASK);
            if(i == 1) {
                log.debug("{no: {}, method: {}}", num, "geFormatNo3");
            }
            num = DigitalUtil.getFormatNo4(DATA, LENGTH, MASK);
            if(i == 1) {
                log.debug("{no: {}, method: {}}", num, "geFormatNo4");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {
//            1000, 10_000, 100_000,
            10_000_000})
    public void test_01_getFormatNo_perf(int count) {
        int i = 0;
        long start = System.currentTimeMillis();
        for (; i < count; i++) {
            DigitalUtil.getFormatNo(DATA, LENGTH, MASK);
        }
        long end = System.currentTimeMillis();
        log.info("===={count: {}, time: {}, method: {}}", count, end - start, "getFormatNo");
    }

    @ParameterizedTest
    @ValueSource(ints = {
//            1000, 10_000, 100_000,
            10_000_000})
    public void test_02_getFormatNo2_perf(int count) {
        int i = 0;
        long start = System.currentTimeMillis();
        for (; i < count; i++) {
            DigitalUtil.getFormatNo2(DATA, LENGTH, MASK);
        }
        long end = System.currentTimeMillis();
        log.info("===={count: {}, time: {}, method: {}}", count, end - start, "getFormatNo2");
    }

    @ParameterizedTest
    @ValueSource(ints = {
//            1000, 10_000, 100_000,
            10_000_000})
    public void test_03_getFormatNo3_perf(int count) {
        int i = 0;
        long start = System.currentTimeMillis();
        for (; i < count; i++) {
            DigitalUtil.getFormatNo3(DATA, LENGTH, MASK);
        }
        long end = System.currentTimeMillis();
        log.info("===={count: {}, time: {}, method: {}}", count, end - start, "getFormatNo3");
    }

    @ParameterizedTest
    @ValueSource(ints = {
//            1000, 10_000, 100_000,
            10_000_000})
    public void test_04_getFormatNo4_perf(int count) {
        int i = 0;
        long start = System.currentTimeMillis();
        for (; i < count; i++) {
            DigitalUtil.getFormatNo4(DATA, LENGTH, MASK);
        }
        long end = System.currentTimeMillis();
        log.info("===={count: {}, time: {}, method: {}}", count, end - start, "getFormatNo4");
    }

    @ParameterizedTest
    @ValueSource(ints = {
//            1000, 10_000, 100_000,
            10_000_000})
    public void test_05_getFormatNo5_perf(int count) {
        int i = 0;
        long start = System.currentTimeMillis();
        for (; i < count; i++) {
            DigitalUtil.getFormatNo5(DATA, LENGTH, MASK);
        }
        long end = System.currentTimeMillis();
        log.info("===={count: {}, time: {}, method: {}}", count, end - start, "getFormatNo5");
    }

}
