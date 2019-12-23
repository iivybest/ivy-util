package org.ivy.xutil.sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Util {

    /* Test */
    public static void main(String[] args) {
        String origin = "航天信息股份有限公司";
        String signature = byteArr2Hex(sha256(origin));
        System.out.println(signature);
    }

    /**
     * 计算 SHA256 摘要
     *
     * @param data
     * @return
     */
    public static byte[] sha256(String data) {
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return instance.digest(data.getBytes());
    }

    /**
     * byte array 转 Hex String
     *
     * @param arr
     * @return
     */
    public static String byteArr2Hex(byte[] arr) {
        StringBuilder builder = new StringBuilder();
        String stmp;
        for (int i = 0; arr != null && i < arr.length; i++) {
            stmp = Integer.toHexString(arr[i] & 0XFF);
            if (stmp.length() == 1)
                builder.append('0');
            builder.append(stmp);
        }
        return builder.toString().toUpperCase();
    }
}













