/**
 * @Title: Base64UtilTest
 * @Description: TODO
 * @author miao.xl
 * @date 2017年1月12日 下午9:01:52
 * @version V1.0
 */
package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Arrays;

/**
 * @Title: Base64UtilTest
 * @Description: TODO
 *
 * @author miao.xl
 * @date 2017年1月12日 下午9:01:52 
 * @version V1.0
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
    public void Decode() throws IOException {
        String plain = new String(SecurityUtil.base64.decode(this.text), "UTF-8");
        System.out.println(plain);
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




}



