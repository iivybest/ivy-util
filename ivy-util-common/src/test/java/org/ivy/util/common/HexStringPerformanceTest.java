package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.xml.bind.DatatypeConverter;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> byte array convert to Hex String performance test
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className HexStringPerformanceTest
 * @date 2019/12/9 9:21
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class HexStringPerformanceTest {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private byte[] bytes;
    private String text;
    private String hex;

    // ----执行次数
    private int count;


    @Before
    public void setUp() {
        this.bytes = FileUtil.read(SystemUtil.getClasspath() + "/material/Helloworld.txt");
        this.text = new String(this.bytes);
        this.count = 10000;
    }

    @Test
    public void test_00_IntergerStaticMethod_wrongDemo() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            int byteLen = this.bytes.length;
            int v;
            for (int j = 0; j < byteLen; j++) {
                stringBuilder.append(Integer.toHexString(this.bytes[j] & 0xFF));
            }
            hex = stringBuilder.toString();
        }
        log.debug("---->[01_00]-{}-[wrong demo]", hex);
    }

    @Test
    public void test_01_encode_01_IntergerStaticMethod() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            int byteLen = this.bytes.length;
            char[] hexChars = new char[byteLen * 2];
            int v;
            for (int j = 0; j < byteLen; j++) {
                v = this.bytes[j] & 0xFF;
                hexChars[j * 2] = Integer.toHexString(v >>> 4).charAt(0);
                hexChars[j * 2 + 1] = Integer.toHexString(v & 0xF).charAt(0);
            }
            hex = new String(hexChars);
        }
        log.debug("---->[01_01]-{}", hex);
    }

    @Test
    public void test_01_encode_02_IntergerStaticMethod() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            int byteLen = this.bytes.length;
            StringBuilder stringBuilder = new StringBuilder();
            int v;
            for (int j = 0; j < byteLen; j++) {
                v = this.bytes[j] & 0xFF;
                stringBuilder.append(Integer.toHexString(v >>> 4)).append(Integer.toHexString(v & 0xF));
            }
            hex = stringBuilder.toString();
        }
        log.debug("---->[01_02]-{}", hex);

    }

    @Test
    public void test_01_encode_03_DatatypeConverterStaticMethod() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            hex = DatatypeConverter.printHexBinary(this.bytes);
        }
        log.debug("---->[01_03]-{}", hex);
    }

    @Test
    public void test_01_encode_04_MyMethod() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            int byteLen = this.bytes.length;
            char[] hexChars = new char[byteLen * 2];
            // 每个 byte 转换为 2 个hex char
            int v;
            for (int j = 0; j < byteLen; j++) {
                v = this.bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0xF];
            }
            hex = new String(hexChars);
        }
        log.debug("---->[01_04]-{}", hex);
    }

    @Test
    public void test_01_encode_05_MyMethod() {
        String hex = null;
        for (int i = 0; i < this.count; i++) {
            int byteLen = this.bytes.length;
            StringBuilder sb = new StringBuilder(byteLen * 2);
            // 每个 byte 转换为 2 个hex char
            int v;
            for (int j = 0; j < byteLen; j++) {
                v = (this.bytes[j] & 0xFF);
                sb.append(hexArray[v >>> 4]).append(hexArray[v & 0xF]);
            }
            hex = sb.toString();
        }

        log.debug("---->[01_05]-{}", hex);
    }

}
