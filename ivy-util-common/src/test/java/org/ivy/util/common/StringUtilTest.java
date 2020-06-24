package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.math.BigDecimal;

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
/* FixMethodOrder 是 Junit 1.4 注解*/
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class StringUtilTest {

    /**
     * <p>
     * <br>---------------------------------------------------------------
     * <br> description: 字符序列断句处理
     * <br>     * e.g. punctuate("111122223333", 4, "-") -> 1111-2222-3333
     * <br>---------------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param sequence 字符序列
     * @param len      断句长度
     * @param mark     断句标记
     * @return 断句结果
     */
    public static String punctuate(String sequence, int len, String mark) {
        if (sequence == null) {
            return null;
        }
        int sLen = sequence.length();
        if (sLen <= len) {
            return sequence;
        }
        int remainder = sLen % len;
        int count = sLen / len + (remainder > 0 ? 1 : 0);
        StringBuilder builder = new StringBuilder();
        int offset, end;
        String part;
        for (int i = 0; i < count; i++) {
            offset = i * len;
            end = (offset + len) < sLen ? (offset + len) : sLen;
            part = sequence.substring(offset, end);
            builder.append(part);
            if (i != count - 1) {
                builder.append(mark);
            }
        }
        return builder.toString();
    }

    @AfterEach
    public void tearDown() {
        log.debug("----> split line --------------------------");
    }

    @Test
    public void test_01_toBinString() {
    }

    @Test
    public void test_02_toHexString() {
        String text = "A";
        String hex = StringUtil.toHexString(text);
        log.debug("{value:{}, type: {}", hex, "String");
        hex = DigitUtil.toHexString(text.getBytes());
        log.debug("{value:{}, type: {}", hex, "byte array");
        hex = DigitUtil.toHexString(text.charAt(0));
        log.debug("{value:{}, type: {}", hex, "char");
        hex = DigitUtil.toHexString((int) text.charAt(0));
        log.debug("{value:{}, type: {}", hex, "int");
        hex = DigitUtil.toHexString((long) text.charAt(0));
        log.debug("{value:{}, type: {}", hex, "long");
    }

    @Test
    public void test_03_printBinString() {
        byte[] b = {0, 15, -117, -23, -30, -118, -111, 103, 47, 19, 66, -95, 74, -35, 110, 60, -119, -86};
        log.debug(DigitUtil.toHexString(b));
        log.debug(Arrayx.printArray(b));
    }

    @Test
    public void test_04_fixedLength() {
        int len = 10;
        String[] data = {
                "hello",
                "hello henry",
                "hello123456789",
                "hi",
                "1234567890my",
        };
        String fixed;
        for (String e : data) {
            fixed = StringUtil.getFixedLengthString(e, len, '-', 0);
            log.info("{fixed: {}, data: {}}", fixed, e);
            fixed = StringUtil.getFixedLengthString(e, len);
            log.info("{fixed: {}, data: {}}", fixed, e);
        }

    }

    @Test
    public void test_05_formate() {
        int len = 5;
        String mark = " ";
        String[] data = {
                "1111122222333334444455555",
                "11111222223333344444555556",
                "111112222233333444445555",
                "1111",
        };
        String fixed;
        for (String e : data) {
            fixed = StringUtil.punctuate(e, len, mark);
            log.info("{fixed: {}, data: {}}", fixed, e);
        }
    }

    @Test
    public void test_06_isNumberic() {
        int len = 5;
        String mark = " ";
        String[] data = {
                "1111122222333334444455555",
                "11111222223333344444555556",
                "111112222233333444445555",
                "0.17",
                ".17",
                "1_000",
                "1-000",
        };
        boolean fixed;
        for (String e : data) {
            fixed = StringUtil.isNumeric(e);
            log.info("===={fixed: {}, data: {}}", fixed, e);
        }
    }

    @Test
    public void test_07_groupLayout() {
        int len = 5;
        String mark = " ";
        String[] data = {
                "1111122222333334444455555",
                "11111222223333344444555556",
                "111112222233333444445555",
                "0.17",
                ".17",
                "1_000",
                "1-000",
        };
        String seqence;
        for (String e : data) {
            seqence = StringUtil.groupLayout(e, len, mark);
            log.info("===={seqence: {}, data: {}}", seqence, e);
        }
    }








    /**
     * 字符串匹配，有一个能匹配上，结果为匹配
     *
     * @param key   比较键值
     * @param items 键值集合
     * @return 结果
     */
    private static <T> boolean match(T key, T... items) {
        boolean matched = false;
        for (T bench : items) {
            matched = bench.equals(key);
            if (matched) {
                break;
            }
        }
        return matched;
    }
}




