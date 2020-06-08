package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.UnsupportedEncodingException;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> TODO
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className StringUtilTest
 * @date 2019/12/9 16:28
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class StringUtilBreakLinePerfTest {

    private static final String[] TEXTS = {
            "012345",
            "0123456789",
            "01234567890",
            "01234567890123456",
            "01234567890123456789",
            "012345678901234567890",
            "01234567890123456789012345678901234567890",
            "ABCD",
            "ABCDEFGHIG",
            "ABCDEFGHIGABCDEFGHIG",
            "ABCDEFGHIGABCDEFGHIGA",
            "abcd",
            "abcdefghig",
            "abcdefghigabcdefghig",
            "abcdefghigabcdefghiga",
            "美丽的",
            "美丽的草原",
            "美丽的草原美丽的草原",
            "M丽的草原美丽的草原",
            "M丽的草原M丽的草原H",
            "M丽的草原美丽的草原美丽的草原WX",
    };

    private static final int BYTE_LEN_PER_LINE = 10;

    private static final String CHARSET = "GBK";

    public static final String BREAK_LINE_MARK = "^_^";

    public static final int COUNT_BENCH = 100_000;


    @BeforeAll
    public static void setUp() {

    }

    @AfterEach
    public void tearDown() {
        log.debug("----> split line --------------------------");
    }

    @Test
    public void test_00_warmup() throws UnsupportedEncodingException {
        int dataByteLen;
        for (int i = 0; i < 100_000; i++) {
            for (String e : TEXTS) {
                dataByteLen = e.getBytes(CHARSET).length;
                Object[] sequence1 = StringUtil.breakLineSequenceByByte(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
                Object[] sequence2 = StringUtil.breakLineSequenceByByteUseLoop(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
                if (i != 1) {
                    continue;
                }
                log.info("===={data: {}, count: {}, sequence: {}}", StringUtil.getFixedLengthString(e, 40), sequence1[0], sequence1[1]);
                log.info("===={data: {}, count: {}, sequence: {}}", StringUtil.getFixedLengthString(e, 40), sequence2[0], sequence2[1]);
                log.info("====");
            }
        }
    }

    @Test
    public void test_01_loop() throws UnsupportedEncodingException {
        long sta = System.currentTimeMillis();
        int dataByteLen;
        for (int i = 0; i < COUNT_BENCH; i++) {
            for (String e : TEXTS) {
                dataByteLen = e.getBytes(CHARSET).length;
                StringUtil.breakLineSequenceByByteUseLoop(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
            }
        }
        long end = System.currentTimeMillis();
        long tak = end -sta;
        long avg = tak / COUNT_BENCH;
        log.error("====={mark: {}, count: {}, tak: {}, avg: {}}", "loop", COUNT_BENCH, tak, avg);
    }

    @Test
    public void test_02_step() throws UnsupportedEncodingException {
        long sta = System.currentTimeMillis();
        int dataByteLen;
        for (int i = 0; i < COUNT_BENCH; i++) {
            for (String e : TEXTS) {
                dataByteLen = e.getBytes(CHARSET).length;
                StringUtil.breakLineSequenceByByte(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
            }
        }
        long end = System.currentTimeMillis();
        long tak = end -sta;
        long avg = tak / COUNT_BENCH;
        log.error("====={mark: {}, count: {}, tak: {}, avg: {}}", "step", COUNT_BENCH, tak, avg);
    }
    @Test
    public void test_03_loop() throws UnsupportedEncodingException {
        long sta = System.currentTimeMillis();
        int dataByteLen;
        for (int i = 0; i < COUNT_BENCH; i++) {
            for (String e : TEXTS) {
                dataByteLen = e.getBytes(CHARSET).length;
                StringUtil.breakLineSequenceByByteUseLoop(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
            }
        }
        long end = System.currentTimeMillis();
        long tak = end -sta;
        long avg = tak / COUNT_BENCH;
        log.error("====={mark: {}, count: {}, tak: {}, avg: {}}", "loop", COUNT_BENCH, tak, avg);
    }

    @Test
    public void test_04_step() throws UnsupportedEncodingException {
        long sta = System.currentTimeMillis();
        int dataByteLen;
        for (int i = 0; i < COUNT_BENCH; i++) {
            for (String e : TEXTS) {
                dataByteLen = e.getBytes(CHARSET).length;
                StringUtil.breakLineSequenceByByte(e, dataByteLen, BYTE_LEN_PER_LINE, BREAK_LINE_MARK, CHARSET);
            }
        }
        long end = System.currentTimeMillis();
        long tak = end -sta;
        long avg = tak / COUNT_BENCH;
        log.error("====={mark: {}, count: {}, tak: {}, avg: {}}", "step", COUNT_BENCH, tak, avg);
    }

}


