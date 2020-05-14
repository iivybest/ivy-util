package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.Arrayx;
import org.ivy.util.common.DigitUtil;
import org.ivy.util.common.FileUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <p> description: MdUtil Test
 * <br>--------------------------------------------------------
 * <br> Test case
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className MdUtilTest
 * @date 2014/6/11 16:25
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class MessageDigestUtilTest {
    private String plain;

    @Before
    public void setup() {
        this.plain = "我是一只草泥马-caonima-&^%*$#-@@#-草泥马";
    }

    @Test
    public void test_01_md() {
        System.out.println("====> md ----------------------------------------------");
        byte[] MD2 = SecurityUtil.md.digest(MessageDigestUtil.MD2, plain);
//		byte[] MD4 = SecurityUtil.md.digest(MdUtil.MD4, plain);
        byte[] MD5 = SecurityUtil.md.digest(MessageDigestUtil.MD5, plain);
        byte[] SHA1 = SecurityUtil.md.digest(MessageDigestUtil.SHA1, plain);
//		byte[] SHA224 = SecurityUtil.md.digest(MdUtil.SHA224, plain);
        byte[] SHA256 = SecurityUtil.md.digest(MessageDigestUtil.SHA256, plain);
        byte[] SHA384 = SecurityUtil.md.digest(MessageDigestUtil.SHA384, plain);
        byte[] SHA512 = SecurityUtil.md.digest(MessageDigestUtil.SHA512, plain);

        log.debug("{{}-{}-{}}", String.format("%-12s", "MD2"), MD2.length, Arrayx.printArray(MD2));
//		log.debug("{{}-{}-{}}", String.format("%-12s", "MD4"), MD4.length, Arrayx.printArray(MD4));
        log.debug("{{}-{}-{}}", String.format("%-12s", "MD5"), MD5.length, Arrayx.printArray(MD5));
        log.debug("{{}-{}-{}}", String.format("%-12s", "SHA1"), SHA1.length, Arrayx.printArray(SHA1));
//		log.debug("{{}-{}-{}}", String.format("%-12s", "SHA224"), SHA224.length, Arrayx.printArray(SHA224));
        log.debug("{{}-{}-{}}", String.format("%-12s", "SHA256"), SHA256.length, Arrayx.printArray(SHA256));
        log.debug("{{}-{}-{}}", String.format("%-12s", "SHA384"), SHA384.length, Arrayx.printArray(SHA384));
        log.debug("{{}-{}-{}}", String.format("%-12s", "SHA512"), SHA512.length, Arrayx.printArray(SHA512));
    }

    @Test
    public void test_02_templateSign() throws IOException {
        String path = "E:\\Ivybest\\work\\aisino\\07.OFD版式文件\\00.OFD发票模板\\电子发票通用模板-20200312/";
        File[] files = FileUtil.getAllNonDirFileList(path);
        String sign = null;
        for (File e : files) {
            sign = DigitUtil.toHexString(SecurityUtil.md.digest(MessageDigestUtil.MD5, FileUtil.read(e)));
            log.info("{file: {}, sign: {}}", e.getName(), sign);
        }

    }

}





