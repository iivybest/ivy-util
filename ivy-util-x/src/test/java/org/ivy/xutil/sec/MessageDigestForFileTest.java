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
public class MessageDigestForFileTest {


    private static final List<String> PATH = new ArrayList<>();
    private static final List<InputStream> IN = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        PATH.clear();
        IN.clear();

        PATH.add("D:\\Ivybest\\work\\aisino\\07.OFD版式文件\\02.SDK\\foxit\\gsdk\\gsdk_2.3.4_20200520\\Foxit_GSDK_Std_2.4.0_0520_r11886_CentOS_64\\lib/libfofdgsdkwrapper64.so");
        PATH.add("D:\\Ivybest\\work\\aisino\\07.OFD版式文件\\02.SDK\\foxit\\gsdk\\gsdk_2.3.4_20200519/libfofdgsdkwrapper64.so");
        PATH.add("D:/keywords.txt");


        try {
            InputStream input;
            for (String e : PATH) {
                input = new FileInputStream(e);
                IN.add(input);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    @AfterEach
    public void tearDown() {
        try {
            for (InputStream e : IN) {
                if (e != null) {
                    e.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void test_00_warmUp() throws IOException{
        String signature;
        byte[] bytes, signBytes;
        for (InputStream e : IN) {
            signBytes = MessageDigestUtil.digest(MessageDigestUtil.MD5, e);
            signature = DigitUtil.toHexString(signBytes);
        }
    }


    @Test
    public void test_01_signWithMD5() throws IOException {
        String signature;
        byte[] bytes, signBytes;
        for (InputStream e : IN) {
            signBytes = MessageDigestUtil.digest(MessageDigestUtil.MD5, e);
            signature = DigitUtil.toHexString(signBytes);
            log.info("===={algorithm: {}, signature: {}}", MessageDigestUtil.MD5, signature);
        }
    }

    @Test
    public void test_02_signWithMD5() throws IOException {
        String signature;
        byte[] bytes, signBytes;
        for (InputStream e : IN) {
            signBytes = MessageDigestUtil.digest(MessageDigestUtil.MD5, FileUtil.read(e));
            signature = DigitUtil.toHexString(signBytes);
            log.info("===={algorithm: {}, signature: {}}", MessageDigestUtil.MD5, signature);
        }
    }
    @Test
    public void test_03_signWithMD5() throws IOException {
        String signature;
        byte[] bytes, signBytes;
        for (String e : PATH) {
            bytes =  FileUtil.read(e);
            signBytes = MessageDigestUtil.digest(MessageDigestUtil.MD5, bytes);
            signature = DigitUtil.toHexString(signBytes);
//            log.info("===={origin: {}}", new String(bytes));
            log.info("===={algorithm: {}, signature: {}}", MessageDigestUtil.MD5, signature);
        }
    }

}
