package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.Arrayx;
import org.ivy.util.common.DigitUtil;
import org.ivy.util.common.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class MessageDigestUtilTest {

    private static final String PLAIN = "我是一只草泥马-caonima-&^%*$#-@@#-草泥马";

    @BeforeEach
    public void setup() {
    }

    @Test
    public void test_01_md() {
        System.out.println("====> md ----------------------------------------------");
        byte[] MD2 = MessageDigestUtil.digest(MessageDigestUtil.MD2, PLAIN);
//		byte[] MD4 = MessageDigestUtil.digest(MdUtil.MD4, plain);
        byte[] MD5 = MessageDigestUtil.digest(MessageDigestUtil.MD5, PLAIN);
        byte[] SHA1 = MessageDigestUtil.digest(MessageDigestUtil.SHA1, PLAIN);
//		byte[] SHA224 = MessageDigestUtil.digest(MdUtil.SHA224, plain);
        byte[] SHA256 = MessageDigestUtil.digest(MessageDigestUtil.SHA256, PLAIN);
        byte[] SHA384 = MessageDigestUtil.digest(MessageDigestUtil.SHA384, PLAIN);
        byte[] SHA512 = MessageDigestUtil.digest(MessageDigestUtil.SHA512, PLAIN);

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
            sign = DigitUtil.toHexString(MessageDigestUtil.digest(MessageDigestUtil.MD5, FileUtil.read(e)));
            log.info("{file: {}, sign: {}}", e.getName(), sign);
        }

    }


    @Test
    public void test_03_opposite() {
        int count = Integer.bitCount(4);
        System.out.println(count);
    }

    @Test
    public void test_04_encoding() throws UnsupportedEncodingException {
        String[] args = {
                "我",
                "爱",
                "你",
                "中",
                "国",
                "I",
                "L",
        };

        String[] charset = {"utf-8", "gbk"};

        byte[] bytes;
        int len;
        for (String e : args) {
            for (String c : charset) {
                bytes = e.getBytes(c);
                len = bytes.length;
                log.info("===={character: {}, length: {}, charset: {}}", new String(bytes, c), len, c);
            }
            System.out.println();
        }
    }
    @Test
    public void test_05_string() {
        String text = "刘柳期起捌拔九九\r\n环球寰宇音律音乐英瑞诺贝荣膺英勇第一第二京贸精修韵律优先有限公司";
        String[] array = text.split("\r\n");
        System.out.println(text);
        log.info("===={1: {}, 2: {}}", array[0], array[1]);
        String result = text.replaceAll("[\\r\\n]", "-");
        System.out.println(result);

    }


    private static final int COUNT_BENCH = 10;
    private static final String TEXT = "美丽ABC1";
    @Test
    public void test_06_string() throws UnsupportedEncodingException {
        for (int i = 0; i < COUNT_BENCH; i++) {
            byte[] bytes = TEXT.getBytes("GBK");
            int length = bytes.length;
            if (i == 0) {
                log.info("===={length: {}}",length);
            }
        }
    }

    @Test
    public void test_07_string() throws UnsupportedEncodingException {
        for (int i = 0; i < COUNT_BENCH; i++) {
            char c;
            int length = 0;
            for (int x = 0; x < TEXT.length(); x ++) {
                c = TEXT.charAt(x);
                length += charByteLen(c);
            }
            if (i == 0) {
                log.info("===={length: {}}",length);
            }
        }
    }



    private int charByteLen(char arg) {
        byte lByte = (byte) (arg & 0xFF00 >> 8);
        return (lByte > 0) ? 2 : 1;
    }
}





