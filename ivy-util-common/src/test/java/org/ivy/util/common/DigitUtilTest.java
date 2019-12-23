package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.stream.IntStream;

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
 * @className DigitUtilTest
 * @date 2019/12/11 13:22
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DigitUtilTest {

    /* 中文数字小写*/
    private static final char[] cuArr = {'十', '百', '千', '万', '亿'};
    private static final char[] cnArr = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

    private final String[] numbers = {
            "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "20",
            "91",
            "110",
            "102",
            "-1234",
            "-1023",
            "1002",
            "10000",
            "12345",
            "10234",
            "10023",
            "10002",
            "12034",
            "12003",
            "12000",
            "12300",
            "12340",
            "100000",
            "123456",
            "102345",
            "200234",
            "100023",
            "100002",
            "120345",
            "120034",
            "120003",
            "120000",
            "123000",
            "123400",
            "123450",
            "1000000",
            "1234567",
            "2023456",
            "1002345",
            "3000234",
            "1000023",
            "1000002",
            "1203456",
            "1200345",
            "1200034",
            "1200003",
            "1200000",
            "1230000",
            "1234000",
            "1234500",
            "1234560",
            "10000000",
            "12345678",
            "10234567",
            "10023456",
            "10002345",
            "10000234",
            "10000023",
            "12034567",
            "12003456",
            "12000345",
            "12000034",
            "12000003",
            "12000000",
            "12300000",
            "12340000",
            "12345600",
            "12345670",
            "1000000000",
            "1234567890",
            "1023456789",
            "1002300078",
            "1000230067",
            "1000023456",
            "1000002345",
            "1203456789",
            "1200345678",
            "1200034567",
            "1200003456",
            "1200000345",
            "1200000034",
            "1200000003",
            "1230000000",
            "1234000000",
            "1234500000",
            "1234560000",
            "1234567000",
            "1234567800",
            "1234567890",
            String.valueOf(Integer.MAX_VALUE),
            "9223372036854",
            "9002233007200",
            "1010003060201",
            "10360201",
            "1000000000"
    };

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        log.debug("====> split line------------------------------------");
    }

    @Test
    public void test_00_initial() {
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;

        int[] data = {min, max, 0, 1, -1, 3, -3, 123456789};
        for (int e : data)
            log.debug("{\nval: {}, \nhex: {}, \nbin: {}, \nbin: {}}",
                    e, DigitUtil.toHexString(e), DigitUtil.toBinString(e), Integer.toBinaryString(e));

        log.debug("[{}, {}]", min, max);
        log.debug("[{}, {}]", 0x80000000, 0x7fffffff);

        for (char e : cnArr) log.debug("{char: {}, int: {}, hex: {}}", e, (int) e, Integer.toHexString((int) e));
        for (char e : cuArr) log.debug("{char: {}, int: {}, hex: {}}", e, (int) e, Integer.toHexString((int) e));

        IntStream.range(0, 10).mapToObj(e -> String.valueOf(e).toCharArray()[0]).forEach(e -> {
            log.debug("{char: {}, int: {}, hex: {}}", e, (int) e, Integer.toHexString((int) e));
        });
    }


    @Test
    public void test_01_chineseDigit2Integer() {
        String[] digits = {
                "一十三亿零三百零六万零二百",
                "十三亿零三百零六万零二百",
                "三百零六万零二百",
                "四万万",
                "万三",
                "三十万",
                "三十三",
                "十三",
                "六"
        };
        for (String e : digits) {
            log.debug("====>{cn: {}, nu: {}}", e, DigitUtil.chineseDigit2Integer(e));
        }
    }

    @Test
    public void test_02_chineseDigit2Long() {
        String[] digits = {
                "一万零一百亿零三百零六万零二百",
                "三百亿零三百零六万零二百",
                "三百零二亿零三百零六万零二百",
                "一十三亿零三百零六万零二百",
                "十三亿零三百零六万零二百",
                "三百零六万零二百",
                "四万万",
                "万三",
                "三十万",
                "三十三",
                "十三",
                "六"
        };
        for (String e : digits) {
            log.debug("====>{cn: {}, nu: {}}", e, DigitUtil.chineseDigit2Long(e));
        }
    }

    @Test
    public void test_03_long2ChineseDigit() {
        long data = 1_010_003_060_200L;
        String format = DigitUtil.long2ChineseDigit(data);
        log.debug("====>{\nno: {}, \ncn: {}\n}", data, format);
    }

    @Test
    public void test_04_chineseDigit2ArabicDigit() {
        String text = "第五十天 系统基本文件管理肆拾伍十五";
        String format = DigitUtil.chineseDigit2Arabic(text, 0, 6, 5);
        log.debug("====>{\ntext: {}, \nformat: {}\n}", text, format);
    }

    @Test
    public void test_05_long2ChineseDigit() {
        String format;
        long num;
        for (int i = 0; i < 10000; i++) {
            for (String e : this.numbers) {
                num = Long.parseLong(e);
                format = Long2ChineseDigitTest.long2ChineseDigit(num, true);
//                log.debug(String.format("%-12s \t %s", e, format));
            }
        }
    }

    @Test
    public void test_06_advLong2ChineseDigit() {
        long num;
        String format;
        for (int i = 0; i < 1; i++) {
            for (String e : this.numbers) {
                num = Long.parseLong(e);
                format = AdvLong2ChineseDigitTest.long2ChineseDigit(num, true);
//                log.log(String.format("%-12s \t %s", e, format));
            }
        }
    }

    @Test
    public void test_07_double2ChineseDigit() {
        double num = 1020034.34D;
        String digit = DigitUtil.double2ChineseDigit(num, true);
        log.debug("====>{\nnum: {}, \ndigit: {}\n}", num, digit);
    }


}
