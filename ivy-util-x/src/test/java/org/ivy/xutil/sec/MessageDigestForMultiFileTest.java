package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.DigitUtil;
import org.ivy.util.common.FileUtil;
import org.ivy.util.common.StringUtil;
import org.junit.jupiter.api.*;

import java.io.*;
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
 * @date 2020/5/21 12:10
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MessageDigestForMultiFileTest {

    private static final String REQUEST_PATH = "D:\\Ivybest\\test\\img/";

    @Test
    public void test_01_signWithMD5() throws IOException {
        FileInputStream in;
        String signature;
        for (File e : FileUtil.getAllNonDirFileList(REQUEST_PATH)) {
            in = new FileInputStream(e);
            signature = DigitUtil.toHexString(MessageDigestUtil.digest(MessageDigestUtil.MD5, in));
            log.info("file: {}, sign: {}", StringUtil.getFixedLengthString(e.getName(), 40), signature);
        }
    }

}
