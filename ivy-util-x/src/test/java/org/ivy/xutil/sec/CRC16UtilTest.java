package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.UnsupportedEncodingException;

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
 * @date 2020/6/16 13:58
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class CRC16UtilTest {

    private static final String TEXT = "01,10,111000010011,14358589,10000.00,20200401,13745889284511930162,";
    private static final String[] MESSAGE = {
            "01,10,111000010011,14358589,10000.00,20200401,13745889284511930162,",
            "01,10,150003528888,58934826,12800.00,20180622,83279021241637908615,"
    };

    @Test
    public void test_00_init() {
        int crc = CRC16Util.CRC16_MODBUS(TEXT.getBytes());
        String crcHex = Integer.toHexString(crc);
        log.info("{crc: {}, hex: {}, charset: {}}", crc, crcHex, "default");
    }
    @Test
    public void test_01_init() throws UnsupportedEncodingException {
        int crc = CRC16Util.CRC16_MODBUS(TEXT.getBytes("GBK"));
        String crcHex = Integer.toHexString(crc);
        log.info("{crc: {}, hex: {}, charset: {}}", crc, crcHex, "GBK");
    }
    @Test
    public void test_02_init() throws UnsupportedEncodingException {
        int crc = CRC16Util.CRC16_MODBUS(TEXT.getBytes("UTF-8"));
        String crcHex = Integer.toHexString(crc);
        log.info("{crc: {}, hex: {}, charset: {}}", crc, crcHex, "UTF-8");
    }

    @Test
    public void test_03_message() throws UnsupportedEncodingException {
        String charset = "GBK";
        int crc;
        String crcHex;
        for (String e : MESSAGE) {
            crc = CRC16Util.CRC16_MODBUS(e.getBytes(charset));
            crcHex = Integer.toHexString(crc);
            log.info("{crc: {}, hex: {}, charset: {}, data: {}}", crc, crcHex, charset, e);
        }


    }
}
