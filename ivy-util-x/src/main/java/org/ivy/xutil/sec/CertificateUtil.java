/**
 * Filename 	CertificateUtil
 *
 * @author ivybest
 * @version V1.0
 * CreateDate 	2017年6月19日 下午8:18:35
 * Company 		IB.
 * Copyright 	Copyright(C) 2010-
 * All rights Reserved, Designed By ivybest
 * <p>
 * Modification History:
 * Date			Author		Version		Discription
 * --------------------------------------------------------
 * 2017年6月19日	ivybest		1.0			new create
 */
package org.ivy.xutil.sec;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * <p> classname: CertificateUtil
 * <p> description: 证书操作工具类
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2017/06/19 20:18
 * @since 1.7+
 */
public class CertificateUtil {

    public static String STORE_TYPE_JKS = "JKS";
    public static String STORE_TYPE_JCEKS = "JCEKS";
    public static String STORE_TYPE_PKCS12 = "PKCS12";

    public static String X509 = "X.509";

    /**
     * get keyStore
     *
     * @param keyStorePath keyStore path
     * @param keyStorePass keyStore password
     * @param storeType    keyStore type
     * @return KeyStore
     * @throws Exception Exception
     * @since 1.7+
     */
    public static KeyStore getKeyStore(String keyStorePath, String keyStorePass, String storeType) throws Exception {
        try (FileInputStream is = new FileInputStream(keyStorePath)) {
            KeyStore ks = KeyStore.getInstance(storeType);
            ks.load(is, keyStorePass.toCharArray());
            return ks;
        }
    }

    /**
     * get keyStore
     *
     * @param keyStore     keyStore byte array
     * @param keyStorePass keyStore password
     * @param storeType    keyStore type
     * @return KeyStore
     * @throws Exception Exception
     * @since 1.7+
     */
    public static KeyStore getKeyStore(byte[] keyStore, String keyStorePass, String storeType) throws Exception {
        try (ByteArrayInputStream is = new ByteArrayInputStream(keyStore)) {
            KeyStore ks = KeyStore.getInstance(storeType);
            ks.load(is, keyStorePass.toCharArray());
            return ks;
        }
    }

    /**
     * get cerificate
     *
     * @param cert certificate byte array
     * @return certificate
     * @throws Exception Exception
     */
    public static Certificate getCertificate(byte[] cert) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        try (ByteArrayInputStream in = new ByteArrayInputStream(cert)) {
            Certificate certificate = certificateFactory.generateCertificate(in);
            return certificate;
        }
    }

    /**
     * get cerificate
     *
     * @param certificatePath certificate path
     * @return Certificate
     * @throws Exception Exception
     */
    public static Certificate getCertificate(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        try (FileInputStream in = new FileInputStream(certificatePath)) {
            return certificateFactory.generateCertificate(in);
        }
    }

    /**
     * get cerificate
     *
     * @param keyStore keyStore
     * @param alias    alias
     * @return Certificate
     * @throws Exception Exception
     */
    public static Certificate getCertificateFromKeyStore(KeyStore keyStore, String alias) throws Exception {
        return keyStore.getCertificate(alias);
    }

    /**
     * get keyStore alias
     *
     * @param keyStore keyStore
     * @return List
     * @throws KeyStoreException Exception
     */
    public static List<String> getKeySotreAliases(KeyStore keyStore) throws KeyStoreException {
        List<String> aliasList = new ArrayList<String>();
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            aliasList.add(aliases.nextElement());
        }
        return aliasList;
    }

    /**
     * get private key
     *
     * @param keyStore  keyStore
     * @param alias     alias
     * @param aliasPass alias password
     * @return Private Key
     * @throws Exception Exception
     */
    public static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String aliasPass) throws Exception {
        return (PrivateKey) keyStore.getKey(alias, aliasPass.toCharArray());
    }

    /**
     * get public key
     *
     * @param cert certificate byte array
     * @return public Key
     * @throws Exception Exception
     */
    public static PublicKey getPublicKey(byte[] cert) throws Exception {
        return getCertificate(cert).getPublicKey();
    }

    /**
     * get public key
     *
     * @param certificatePath certificate path
     * @return public Key
     * @throws Exception Exception
     */
    public static PublicKey getPublicKey(String certificatePath) throws Exception {
        Certificate certificate = getCertificate(certificatePath);
        PublicKey keubKey = certificate.getPublicKey();
        return keubKey;
    }

    /**
     * encrypt by private key
     *
     * @param data       data
     * @param privateKey private key
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * decrypt by private key
     *
     * @param data       data
     * @param privateKey private key
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * encrypt by public key
     *
     * @param data      data
     * @param publicKey public key
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {
        return encryptByPublicKey(data, getPublicKey(certificatePath));
    }

    public static byte[] encryptByPublicKey(byte[] data, byte[] cer) throws Exception {
        return encryptByPublicKey(data, getPublicKey(cer));
    }

    /**
     * decrypt by public key
     *
     * @param data      data
     * @param publicKey public key
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * decrypt by public key
     *
     * @param data            data
     * @param certificatePath certificate path
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
        return decryptByPublicKey(data, getPublicKey(certificatePath));
    }

    /**
     * decrypt by public key
     *
     * @param data data
     * @param cert certificate baty array
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] cert) throws Exception {
        return decryptByPublicKey(data, getPublicKey(cert));
    }

    /**
     * verify cetificate
     *
     * @param certificate certificate
     * @return boolean
     */
    public static boolean verifyCertificate(Certificate certificate) {
        return verifyCertificate(new Date(), certificate);
    }

    /**
     * verify cetificate
     *
     * @param cert certificate byte array
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verifyCertificate(byte[] cert) throws Exception {
        return verifyCertificate(new Date(), cert);
    }


    /**
     * verify cetificate
     *
     * @param date        date
     * @param certificate certificate
     * @return boolean
     */
    public static boolean verifyCertificate(Date date, Certificate certificate) {
        boolean status = true;
        try {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     * verify cetificate
     *
     * @param date            date
     * @param certificatePath certificate path
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verifyCertificate(Date date, String certificatePath) throws Exception {
        return verifyCertificate(date, getCertificate(certificatePath));
    }

    /**
     * verify cetificate
     *
     * @param date date
     * @param cert certificate byte array
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verifyCertificate(Date date, byte[] cert) throws Exception {
        return verifyCertificate(date, getCertificate(cert));
    }

    /**
     * signature
     *
     * @param data      data
     * @param keyStore  keyStore
     * @param alias     alias
     * @param aliasPass alias password
     * @return byte[]
     * @throws Exception Exception
     */
    public static byte[] signature(byte[] data, KeyStore keyStore, String alias, String aliasPass) throws Exception {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificateFromKeyStore(keyStore, alias);
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStore, alias, aliasPass);
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * verify signature
     *
     * @param data        data
     * @param sign        signature
     * @param certificate certificate
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verify(byte[] data, byte[] sign, Certificate certificate) throws Exception {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) certificate;
        // 获得公钥
        PublicKey publicKey = x509Certificate.getPublicKey();
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(data);

        return signature.verify(sign);
    }

    /**
     * verify signature
     *
     * @param data            data
     * @param sign            signature
     * @param certificatePath certificate path
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verify(byte[] data, byte[] sign, String certificatePath) throws Exception {
        return verify(data, sign, getCertificate(certificatePath));
    }

    /**
     * verify signature
     *
     * @param data data
     * @param sign signature
     * @param cert certificate byte array
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] cert) throws Exception {
        return verify(data, sign, getCertificate(cert));
    }

}
