/**
 * @Title: Base64UtilTest
 * @Description: TODO
 * @author miao.xl
 * @date 2017年1月12日 下午9:01:52
 * @version V1.0
 */
package org.ivy.xutil.sec;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @Title: Base64UtilTest
 * @Description: TODO
 *
 * @author miao.xl
 * @date 2017年1月12日 下午9:01:52 
 * @version V1.0
 */
public class Base64UtilTest {
    private String text;

    @Before
    public void setup() throws IOException {
        this.text = "iamivybest";
    }

    @Test
    public void Decode() throws IOException {
        String plain = new String(SecurityUtil.base64.decode(this.text), "UTF-8");
        System.out.println(plain);
    }
}



