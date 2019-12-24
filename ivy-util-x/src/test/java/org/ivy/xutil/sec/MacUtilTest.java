package org.ivy.xutil.sec;

import org.ivy.util.common.Arrayx;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 * <p> description: Mac算法工具类测试
 * <br>--------------------------------------------------------
 * <br> 测试类
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/16/11 04:25
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@TestMethodOrder(MethodOrderer.Alphanumeric.class)
//@Tag("MacUtil default testcase")
public class MacUtilTest {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String plain;

    @Before
    public void setup() {
        this.plain = "我是一只草泥马-caonima-&^%*$#-@@#-草泥马";
    }

    @After
    public void tearDown() {
        log.debug("====> split line---------------------");
    }

    @Test
    public void test_02_mac() {
//		String key = SecurityUtil.mac.getKey();
        String key = "BLSTTWFU94424814------------------------------------------------";
        log.debug("{key: {}}", key);
        log.debug("{plain: {}}", plain);
        byte[] HMACMD5 = SecurityUtil.mac.digest(MacUtil.HMACMD5, plain, key);
        byte[] HMACSHA1 = SecurityUtil.mac.digest(MacUtil.HMACSHA1, plain, key);
        byte[] HMACSHA256 = SecurityUtil.mac.digest(MacUtil.HMACSHA256, plain, key);
        byte[] HMACSHA384 = SecurityUtil.mac.digest(MacUtil.HMACSHA384, plain, key);
        byte[] HMACSHA512 = SecurityUtil.mac.digest(MacUtil.HMACSHA512, plain, key);
        /* bouncycastal support algorithm*/
//		byte[] HMACMD2 = SecurityUtil.mac.digest(MacUtil.HMACMD2, plaintext, key);

        log.debug("{{}-{}-{}}", String.format("%-12s", "HMACMD5"), HMACMD5.length, Arrayx.printArray(HMACMD5));
        log.debug("{{}-{}-{}}", String.format("%-12s", "HMACSHA1"), HMACSHA1.length, Arrayx.printArray(HMACSHA1));
        log.debug("{{}-{}-{}}", String.format("%-12s", "HMACSHA256"), HMACSHA256.length, Arrayx.printArray(HMACSHA256));
        log.debug("{{}-{}-{}}", String.format("%-12s", "HMACSHA384"), HMACSHA384.length, Arrayx.printArray(HMACSHA384));
        log.debug("{{}-{}-{}}", String.format("%-12s", "HMACSHA512"), HMACSHA512.length, Arrayx.printArray(HMACSHA512));
//		log.debug("{{}-{}-{}}", String.format("%-10s", "HMACMD2"), HMACMD2.length, Arrayx.printArray(HMACMD2));
    }

    /**
     * jwt emulation
     */
    @Test
    public void test_03_jwtEmulation() {
        String key = "BLSTTWFU94424814";
        String header = "{v:1, algo=mac}";
        String payload = "{id:1112233, exp=20191110121212}";
        byte[] sign = SecurityUtil.mac.digest(MacUtil.HMACMD5, header + payload, key);
        String token = Base64Util.encodeToString(key) + "." + Base64Util.encodeToString(payload) + "." + Base64Util.encodeToString(sign);
        log.debug("{token: {}}", token);
    }

    @Test
    public void test_04_jwtVerify() {
        String key = "BLSTTWFU94424814";
        String payload = "{id:1112233, exp=20191210121212}";
        String sign = Base64Util.encodeToString(SecurityUtil.mac.digest(MacUtil.HMACMD5, payload, key));
        String token = Base64Util.encodeToString(payload) + "." + sign;
        log.debug("{token: {}}", token);
        // ----验签
        String[] materials = token.split("\\.");
        String _payload = Base64Util.decodeToString(materials[0]);
        String _sign = materials[1];
        String algoSign = Base64Util.encodeToString(SecurityUtil.mac.digest(MacUtil.HMACMD5, _payload, key));
        log.debug("验签-1：比对签名 {}", algoSign.equals(_sign));
        log.debug("验签-2：获取payload {}", _payload);
    }

    @Test
    public void test_05_customId() {
        String key = "YLSTFWFU93424818";
        String header = "{v:1.0, algo=mac}";
        String payload = "{id:1112233, exp=20191110121212}";
        IntStream.range(0, 10).forEach(e -> {
            String sign = SecurityUtil.mac.digest2HexString(MacUtil.HMACSHA256, header + payload, key);
            log.debug(sign);
        });
    }


}





