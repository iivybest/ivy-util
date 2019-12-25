package org.ivy.xutil.sec;

import org.ivy.util.common.Arrayx;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <p>TestDigest</p>
 *
 * @author miao.xl
 * @date 2014-6-11 下午04:25:34
 */
public class RsaTest {
    private String plaintext;

    @Before
    public void setup() {
        this.plaintext = "我是一只草泥马-caonima-&^%*$#-@@#-草泥马";
    }


    @Test
    public void test_rsa() {
        System.out.println("====> RSA ----------------------------------------------");

        KeyPair keyPair = SecurityUtil.rsa.getKeyPair();
        RSAPrivateKey privateKey = SecurityUtil.rsa.getPrivateKey(keyPair);
        RSAPublicKey publicKey = SecurityUtil.rsa.getPublicKey(keyPair);

        byte[] RSA = SecurityUtil.rsa.encrypt(plaintext, publicKey);
        String original_rsa = SecurityUtil.rsa.decryptToString(RSA, privateKey);

        System.out.println(RSA.length + " - " + Arrayx.printArray(RSA) + "\n"
                + "plain : " + original_rsa);
    }

}





