package org.ivy.xutil.sec;


import org.ivy.util.common.Arrayx;
import org.ivy.xutil.log.LogUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * <p>TestSign</p>
 *
 * @author miao.xl
 * @date 2014-6-13 下午07:02:42
 */
public class SignatureTest {
    private static LogUtil LOG = new LogUtil(SignatureTest.class, "E:/test\\sinlog.log");

    public static void main(String[] args) throws Exception {
        String original = "我是一只草泥马 . . . *&^%$#@!~?><《》$￥%&==++--__--[]{}";

        KeyPair _keyPair = SecurityUtil.signature.getKeyPair(SignatureUtil.RSA);
        PrivateKey _privateKey = SecurityUtil.signature.getPrivateKey(_keyPair);
        byte[] priKeys = _privateKey.getEncoded();
        LOG.log(Arrayx.printArray(priKeys));

        PublicKey _publicKey = SecurityUtil.signature.getPublicKey(_keyPair);
        byte[] pubKeys = _publicKey.getEncoded();
        LOG.log(Arrayx.printArray(pubKeys), true);


        byte[] sign = SecurityUtil.signature.signature(SignatureUtil.SHA1WITHRSA, original, _privateKey);
        LOG.log("sign : " + Arrayx.printArray(sign));

        boolean valid = SecurityUtil.signature.verify(SignatureUtil.SHA1WITHRSA, original, sign, _publicKey);
        LOG.log("valid : " + valid);

    }
}
