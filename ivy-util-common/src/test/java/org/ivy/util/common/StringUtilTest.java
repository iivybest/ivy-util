package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class StringUtilTest {

    @After
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


}




