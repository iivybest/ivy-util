package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.ivy.util.common.DigitUtil;
import org.ivy.util.common.FileUtil;
import org.ivy.util.common.StringUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
 * @date 2020/5/21 12:10
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MessageDigestForMultiFileTest {

//    private static final String REQUEST_PATH = "D:\\Ivybest\\test\\tpls/";
    private static final String REQUEST_PATH = "D:\\Ivybest\\work\\aisino\\07.OFD版式文件\\00.OFD发票模板\\";
//    private static final String REQUEST_PATH = "D:\\Ivybest\\test\\tpls\\jar/";

    @Test
    public void test_01_signWithMD5() throws IOException {
        FileInputStream in;
        String signature;
        for (File e : FileUtil.getNonDirFileList(REQUEST_PATH)) {
            in = new FileInputStream(e);
            signature = DigitUtil.toHexString(MessageDigestUtil.digest(MessageDigestUtil.MD5, in));
            log.info("file: {}, algo: {},  sign: {}", StringUtil.getFixedLengthString(e.getName(), 40), "MD5", signature);
            in.close();
        }
        for (File e : FileUtil.getNonDirFileList(REQUEST_PATH)) {
            in = new FileInputStream(e);
            signature = DigestUtils.md5Hex(in);
            log.info("file: {}, algo: {},  sign: {}", StringUtil.getFixedLengthString(e.getName(), 40), "MD5", signature);
            in.close();
        }
    }

    @Test
    public void test_01_signWithSha256() throws IOException {
        FileInputStream in;
        String signature;
        for (File e : FileUtil.getAllNonDirFileList(REQUEST_PATH)) {
            in = new FileInputStream(e);
            signature = DigitUtil.toHexString(MessageDigestUtil.digest(MessageDigestUtil.SHA256, in));
            log.info("file: {}, algo: {}, sign: {}", StringUtil.getFixedLengthString(e.getName(), 40), "SHA256", signature);
            in.close();
        }


    }
}
