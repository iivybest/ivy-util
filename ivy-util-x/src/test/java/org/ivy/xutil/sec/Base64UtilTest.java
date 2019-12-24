package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Arrays;

/**
 * <p>  classname: Base64UtilTest
 * <br> description: base64 util testcase
 * <br>---------------------------------------------------------
 * <br> base64 util testcase
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/24 17:17
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Base64UtilTest {

    private String text;
    private String textBase64Code;


    @Before
    public void setup() throws IOException {
        this.text = "iamivybest";
        this.textBase64Code = "aWFtaXZ5YmVzdA==";
    }

    @Test
    public void test_01_base64EncodeByteArr() {
        String encode = new String(Base64Util.encode(this.text.getBytes()));

    }

    @Test
    public void test_02_base64EncodeString() {
        String[] arr = {"", this.text, "helloworld", null};
        Arrays.stream(arr).forEach(e -> {
            log.debug("{\norigin: {}, \nencode: {}}", e, Base64Util.encodeToString(e));
        });
    }

    @Test
    public void test_03_decode() throws IOException {
        String plain = new String(Base64Util.decode(this.text), "UTF-8");
        System.out.println(plain);
    }
}



