package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.annotation.Description;
import org.ivy.util.annotation.Recommend;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> to bin performance testcase
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
public class DigitToBinPerformanceTest {
    private final static char[] DIGIT_ARR = "0123456789".toCharArray();

    private int[] data = {
            Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, -1, 3, -3, 123456789
    };
    private int count = 100_000;


    public static String toBinString(int data) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 32; j++) {
            sb.append((data & 0x80000000 >>> j) >>> (31 - j));
        }
//		for (int j = 31; j >= 0; sb.append((data >>> j--) & 1));
        return sb.toString();
    }

    public static String toBinString2(int data) {
        StringBuilder sb = new StringBuilder();
        for (int j = 31; j >= 0; sb.append((data >>> j--) & 1)) ;
        return sb.toString();
    }

    @Recommend(msg = "//use char arr instead of StringBuilder/StringBuffer")
    public static String toBinString3(int data) {
        char[] sequence = new char[32];
        for (int j = 31, cursor = 0; j >= 0; sequence[cursor++] = DIGIT_ARR[((data >>> j--) & 1)]) {
        }
        return new String(sequence);
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
        for (int e : this.data)
            log.debug("{\nval: {}, \nhex: {}, \nbin: {}, \nbi1: {}, \nbi2: {}, \nbi3: {}}",
                    e, DigitUtil.toHexString(e), Integer.toBinaryString(e),
                    toBinString(e),
                    toBinString2(e),
                    toBinString3(e)
            );
    }

    @Description({
            "// toBin Performance testcase",
            "// test_01",
            "// test_02",
            "// test_03"
    })
    @Test
    public void test_01() {
        for (int i = 0; i < this.count; i++) {
            for (int e : this.data) {
                toBinString(e);
            }
        }
    }

    @Test
    public void test_02() {
        for (int i = 0; i < this.count; i++) {
            for (int e : this.data) {
                toBinString2(e);
            }
        }
    }

    @Test
    public void test_03() {
        for (int i = 0; i < this.count; i++) {
            for (int e : this.data) {
                toBinString3(e);
            }
        }
    }

}
