package org.ivy;

import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import com.github.houbb.junitperf.core.report.impl.HtmlReporter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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
public class DigitalUtilTest {

    public static final int DATA = 3;
    public static final int LENGTH = 8;
    public static final char MASK = '&';


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

    @JunitPerfConfig(
            duration = 10_000,
            warmUp = 100,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test_01_getFormatNo_perf() {
        DigitalUtil.getFormatNo(DATA, LENGTH, MASK);
    }

    @JunitPerfConfig(
            duration = 10_000,
            warmUp = 100,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test_02_getFormatNo2_perf() {
        DigitalUtil.getFormatNo2(DATA, LENGTH, MASK);
    }

    @JunitPerfConfig(
            duration = 10_000,
            warmUp = 100,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test_03_getFormatNo3_perf() {
        DigitalUtil.getFormatNo3(DATA, LENGTH, MASK);
    }

    @JunitPerfConfig(
            duration = 10_000,
            warmUp = 100,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test_04_getFormatNo4_perf() {
        DigitalUtil.getFormatNo4(DATA, LENGTH, MASK);
    }

    @JunitPerfConfig(
            duration = 10_000,
            warmUp = 100,
            reporter = {HtmlReporter.class, ConsoleReporter.class})
    public void test_05_getFormatNo5_perf() {
        DigitalUtil.getFormatNo5(DATA, LENGTH, MASK);
    }


}
