/**
 * @Filename ClassPathTest
 * @author Ivybest
 * @version V1.0
 * @date 2018年2月7日 下午3:35:18
 * @Company IB.
 * @Copyright Copyright(C) 2010-
 * All rights Reserved, Designed By Ivybest
 * <p>
 * Modification History:
 * Date				Author		Version		Discription
 * --------------------------------------------------------
 * 2018年2月7日	Ivybest			1.0			new create
 */
package org.ivy;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ivybest imiaodev@163.com
 * @version 1.0
 * ------------------------------------------
 * @className ClassPathTest
 * @date 2018年2月7日 下午3:35:18
 */
public class ClassPathTest {

    public static void main(String[] args) {
        InputStream in = ClassPathTest.class.getResourceAsStream("/ecc-protocol-temp.json");
        String content = new String(read(in));
        System.out.println(content);
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Integer[] ints = {1,2,3};


        List<Integer> list = Arrays.asList(ints);
        System.out.println(list.size());
    }


    public static byte[] read(InputStream in) {
        if (null == in) return null;
        byte[] original = null;
        try (
                BufferedInputStream bis = new BufferedInputStream(in);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = bis.read(buf)) > 0) baos.write(buf, 0, len);
            original = baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return original;
    }
}
