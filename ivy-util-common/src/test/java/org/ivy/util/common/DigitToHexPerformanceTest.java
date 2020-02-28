package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.annotation.Description;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.xml.bind.DatatypeConverter;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> to Hex performance testcase
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/11 13:22
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DigitToHexPerformanceTest {

    private final static String[] DATA = {
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
            "1000000000"
    };

    private int count = 100_000;


    public static String toHexByDigitUtil(byte... data) {
        return DigitUtil.toHexString(data);
    }

    public static String toHexByInteger(byte... data) {
        StringBuilder builder = new StringBuilder(data.length);
        for (byte e : data) {
            builder.append(Integer.toHexString(e));
        }
        return builder.toString();
    }

    public static String toHexByDatatypeConverter(byte... data) {
        return DatatypeConverter.printHexBinary(data);
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
        log.debug("====> split line------------------------------------");
    }

    @Test
    public void test_00_initial() {
        for (String e : this.DATA)
            log.debug("{\nval: {}, \nhex: {}, \nbi1: {}, \nbi2: {}, \nbi3: {}}",
                    e,
                    DigitUtil.toHexString2(e.getBytes()),
                    toHexByDigitUtil(e.getBytes()),
                    toHexByInteger(e.getBytes()),
                    toHexByDatatypeConverter(e.getBytes())
            );
    }

    @Description({
            "// toBin Performance testcase",
            "// test_01",
            "// test_02",
            "// test_03"
    })
    @Test
    public void test_00_setUp() {
        for (int i = 0; i < this.count; i++) {
            for (String e : this.DATA) {
                DigitUtil.toHexString2(e.getBytes());
            }
        }
    }
    @Test
    public void test_01_toHexByDigitUtil() {
        for (int i = 0; i < this.count; i++) {
            for (String e : this.DATA) {
                toHexByDigitUtil(e.getBytes());
            }
        }
    }
    @Test
    public void test_01_toHexByDigitUtil2() {
        for (int i = 0; i < this.count; i++) {
            for (String e : this.DATA) {
                DigitUtil.toHexString2(e.getBytes());
            }
        }
    }

    @Test
    public void test_02_toHexByInteger() {
        for (int i = 0; i < this.count; i++) {
            for (String e : this.DATA) {
                toHexByInteger(e.getBytes());
            }
        }
    }

    @Test
    public void test_03_toHexByDatatypeConverter() {
        for (int i = 0; i < this.count; i++) {
            for (String e : this.DATA) {
                toHexByDatatypeConverter(e.getBytes());
            }
        }
    }

}
