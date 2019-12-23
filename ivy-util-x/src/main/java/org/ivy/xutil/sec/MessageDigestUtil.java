package org.ivy.xutil.sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>  classname: MessageDigestUtil
 * <br> description: 摘要工具
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/6/11 15:58
 */
public class MessageDigestUtil {

    /*
     * 摘要算法主要有MD、SHA、MAC加密三个家族
     */
    /*
     * 摘要算法是一种能产生特殊输出格式的算法，这种算法的特点是：无论用户输入什么长度的原始数据，
     * 经过计算后输出的密文都是固定长度的，这种算法的原理是根据一定的运算规则对原数据进行某种形式的提取，
     * 这种提取就是摘要，被摘要的数据内容与原数据有密切联系，只要原数据稍有改变，输出的“摘要”便完全不同，
     * 因此，基于这种原理的算法便能对数据完整性提供较为健全的保障。但是，由于输出的密文是提取原数据经过处理的定长值，
     * 所以它已经不能还原为原数据，即消息摘要算法是不可逆的，理论上无法通过反向运算取得原数据内容， 因此它通常只能被用来做数据完整性验证。
     * 如今常用的“消息摘要”算法经历了多年验证发展而保留下来的算法已经不多，
     * 这其中包括MD2、MD4、MD5、SHA、SHA-1/256/384/512等。
     * 常用的摘要算法主要有MD5和SHA1。MD5的输出结果为16字节，sha1的输出结果为20字节。
     */
    /*
     * 一、概述
     * MAC算法结合了MD5和SHA算法的优势，并加入密钥的支持，是一种更为安全的消息摘要算法。
     * MAC（Message AuthenticationCode，消息认证码算法）是含有密钥的散列函数算法，
     * 兼容了MD和SHA算法的特性，并在此基础上加入了密钥，我们也常把MAC称为HMAC（keyed-Hash Message Authentication Code）。
     * MAC算法主要集合了MD和SHA两大系列消息摘要算法。MD系列的算法有HmacMD2、HmacMD4、HmacMD5三种算法；
     * SHA系列的算法有HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384.HmacSHA512五种算法。
     * 经过MAC算法得到的摘要值也可以使用十六进制编码表示，其摘要值长度与参与实现的摘要值长度相同。
     * 例如，HmacSHA1算法得到的摘要长度就是SHA1算法得到的摘要长度，都是160位二进制码，换算成十六进制编码为40位。
     *
     * 二、实现和应用 1、Sun的实现和应用
     * 在java6中，MAC系列算法需要通过Mac类提供支持。java6中仅仅提供HmacMD5、HmacSHA1
     * 、HmacSHA256、HmacSHA384和HmacSHA512四种算法。 Mac算法是带有密钥的消息摘要算法，所以实现起来要分为两步：
     * 1）、构建密钥
     * 2）、执行消息摘要
     */

    /* Sun support */
    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";

    /* Bouncy castal support */
    public static final String MD4 = "MD4";
    public static final String SHA224 = "SHA-224";
    public static final String TIGER = "Tiger";


    /**
     * 计算摘要
     *
     * @param data      data
     * @param algorithm 摘要算法
     * @return byte[]
     */
    public static byte[] digest(String algorithm, byte[] data) {
        MessageDigest instance = null;
        try {
            // 创建具有指定算法名称的信息摘要
            instance = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 使用指定的 byte[] 更新摘要
        instance.update(data);
        // 通过执行诸如填充之类的最终操作完成哈希计算
        return instance.digest();
    }

    public static byte[] digest(String algorithm, String original) {
        return digest(algorithm, original.getBytes());
    }

}

