package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.DigitUtil;
import org.ivy.util.common.FileUtil;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
 * @description TODO
 * @date 2020/5/21 12:10
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MessageDigestForFilePerfTest {


    private static final List<String> PATH = new ArrayList<>();
    private static final int COUNT = 100;
    private static final boolean PRINT_ABLE = true;


    @BeforeAll
    public static void setUp() {
        PATH.clear();

        PATH.add("D:\\Ivybest\\work\\aisino\\07.OFD版式文件\\02.SDK\\foxit\\gsdk\\gsdk_2.3.4_20200520\\Foxit_GSDK_Std_2.4.0_0520_r11886_CentOS_64\\lib/libfofdgsdkwrapper64.so");

        log.debug("=====[set up]==================================");
    }

    @AfterAll
    public static void tearDown() {
        log.debug("=====[tear down]==================================");
    }

    @Test
    public void test_00_warmUp() throws IOException {
        byte[] bytes, signature;
        InputStream in;
        for (int i = 0; i < 10; i ++) {
            for (String e : PATH) {
                in = new FileInputStream(e);
                signature = MessageDigestUtil.digest(MessageDigestUtil.MD5, in);
                if (PRINT_ABLE && i == 0) {
                    log.info("===={algorithm: {}, signature: {}}", MessageDigestUtil.MD5, DigitUtil.toHexString(signature));
                }
                in.close();
                in = null;
            }
        }
    }


    @Test
    public void test_01_signWithMD5() throws IOException {
        byte[] bytes, signature;
        InputStream in;
        for (int i = 0; i < COUNT; i ++) {
            for (String e : PATH) {
                in = new FileInputStream(e);
                signature = MessageDigestUtil.digest(MessageDigestUtil.MD5, in);
                if (PRINT_ABLE && i == 0) {
                    log.info("===={algorithm: {}, signature: {}}", MessageDigestUtil.MD5, DigitUtil.toHexString(signature));
                }
                in.close();
            }
        }

    }

}
