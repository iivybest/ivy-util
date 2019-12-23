/**   
 * Filename 	CertificateUtil 
 * @Description TODO 
 * @author 		ivybest   
 * @version 	V1.0   
 * CreateDate 	2017年6月19日 下午8:18:35
 * Company 		IB.
 * Copyright 	Copyright(C) 2010-
 * All rights Reserved, Designed By ivybest
 *
 * Modification History:
 * Date			Author		Version		Discription
 * --------------------------------------------------------
 * 2017年6月19日	ivybest		1.0			new create
 *
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
 * <p> description: 证书操作工具类
 * <br>--------------------------------------------------------
 * <br> TODO
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @className CertificateUtil
 * @author Ivybest (ivybestdev@163.com)
 * @date 2017/06/19 20:18
 * @version 1.0
 */
public class CertificateUtil {

	public static String STORE_TYPE_JKS = "JKS";
	public static String STORE_TYPE_JCEKS = "JCEKS";
	public static String STORE_TYPE_PKCS12 = "PKCS12";

	public static String X509 = "X.509";
	
	/**
	 * @Title 		getKeyStore 
	 * @Description 获取证书库, 支持jdk7+
	 * @param 		keyStorePath
	 * @param 		keyStorePass
	 * @param 		storeType
	 * @throws 		Exception     
	 * @return 		KeyStore
	 */
	public static KeyStore getKeyStore(String keyStorePath, String keyStorePass, String storeType) throws Exception {
		try(FileInputStream is = new FileInputStream(keyStorePath)){
			KeyStore ks = KeyStore.getInstance(storeType);
			ks.load(is, keyStorePass.toCharArray());
			return ks;
		}
	}
	public static KeyStore getKeyStore(byte[] keyStore, String keyStorePass, String storeType) throws Exception {
		try(ByteArrayInputStream is = new ByteArrayInputStream(keyStore)){
			KeyStore ks = KeyStore.getInstance(storeType);
			ks.load(is, keyStorePass.toCharArray());
			return ks;
		}
	}
	
	/**
	 * @Title 		getCertificate 
	 * @Description 获取证书
	 * @param 		cer
	 * @throws 		Exception     
	 * @return 		Certificate
	 */
	public static Certificate getCertificate(byte[] cer) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
		try(ByteArrayInputStream in = new ByteArrayInputStream(cer)) {
			Certificate certificate = certificateFactory.generateCertificate(in);
			return certificate;
		}
	}
	public static Certificate getCertificate(String certificatePath) throws Exception {
		CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
		try (FileInputStream in = new FileInputStream(certificatePath);) {
			return certificateFactory.generateCertificate(in);
		}
	}
	
	/**
	 * @Title 		getCertificateFromKeyStore 
	 * @Description 从公钥中获取证书
	 * @param 		keyStore
	 * @param 		alias
	 * @throws 		Exception     
	 * @return 		Certificate
	 */
	public static Certificate getCertificateFromKeyStore(KeyStore keyStore, String alias) throws Exception {
		return keyStore.getCertificate(alias);
	}
	
	/**
	 * @Title 		getKeySotreAliases 
	 * @Description 获取证书库别名
	 * @param 		keyStore
	 * @return
	 * @throws 		KeyStoreException     
	 * @return 		List<String>
	 */
	public static List<String> getKeySotreAliases(KeyStore keyStore) throws KeyStoreException {
		List<String> aliases = new ArrayList<String>();
		Enumeration<String> _aliases = keyStore.aliases();
		while(_aliases.hasMoreElements()) aliases.add(_aliases.nextElement());
		return aliases;
	}
	
	/**
	 * @Title 		getPrivateKey 
	 * @Description 由 KeyStore获得私钥
	 * @param 		keyStore
	 * @param 		alias
	 * @param 		aliasPass
	 * @throws 		Exception     
	 * @return 		PrivateKey
	 */
	public static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String aliasPass) throws Exception {
		return (PrivateKey) keyStore.getKey(alias, aliasPass.toCharArray());
	}

	/**
	 * @Title 		getPublicKey 
	 * @Description 从certificate中获取公钥
	 * @param 		cer
	 * @throws 		Exception     
	 * @return 		PublicKey
	 */
	public static PublicKey getPublicKey(byte[] cer) throws Exception {
		return getCertificate(cer).getPublicKey();
	}
	public static PublicKey getPublicKey(String certificatePath) throws Exception {
		Certificate certificate = getCertificate(certificatePath);
		PublicKey keubKey = certificate.getPublicKey();
		return keubKey;
	}
	
	/**
	 * @Title 		encryptByPrivateKey 
	 * @Description 利用私钥加密
	 * @param 		data
	 * @param 		privateKey
	 * @throws 		Exception     
	 * @return 		byte[]
	 */
	public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * @Title 		decryptByPrivateKey 
	 * @Description 利用私钥解密
	 * @param 		data
	 * @param 		privateKey
	 * @throws 		Exception     
	 * @return 		byte[]
	 */
	public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
		// 对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * @Title 		encryptByPublicKey 
	 * @Description 利用公钥加密
	 * @param 		data
	 * @param 		publicKey
	 * @throws 		Exception     
	 * @return 		byte[]
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
	 * @Title 		decryptByPublicKey 
	 * @Description 利用公钥解密
	 * @param 		data
	 * @param 		publicKey
	 * @throws 		Exception     
	 * @return 		byte[]
	 */
	public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
		// 对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}
	public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
		return decryptByPublicKey(data, getPublicKey(certificatePath));
	}
	public static byte[] decryptByPublicKey(byte[] data, byte[] cer) throws Exception {
		return decryptByPublicKey(data, getPublicKey(cer));
	}
	
	/**
	 * @Title 		verifyCertificate 
	 * @Description 验证证书
	 * @param 		certificate
	 * @return 		boolean
	 */
	public static boolean verifyCertificate(Certificate certificate) {
		return verifyCertificate(new Date(), certificate);
	}
	public static boolean verifyCertificate(byte[] cer) throws Exception {
		return verifyCertificate(new Date(), cer);
	}
	
	
	/**
	 * 验证证书是否过期或无效
	 * 
	 * @param date
	 * @param certificate
	 * @return
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
	public static boolean verifyCertificate(Date date, String certificatePath) throws Exception {
		return verifyCertificate(date, getCertificate(certificatePath));
	}
	public static boolean verifyCertificate(Date date, byte[] cer) throws Exception {
		return verifyCertificate(date, getCertificate(cer));
	}
	
	/**
	 * @Title 		sign 
	 * @Description 签名
	 * @param 		data
	 * @param 		keyStore
	 * @param 		alias
	 * @param 		aliasPass
	 * @throws 		Exception     
	 * @return 		byte[]
	 */
	public static byte[] sign(byte[] data, KeyStore keyStore, String alias, String aliasPass) throws Exception {
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
	 * @Title 		verify 
	 * @Description 验签
	 * @param 		data
	 * @param 		sign
	 * @param 		certificate
	 * @throws 		Exception     
	 * @return 		boolean
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
	public static boolean verify(byte[] data, byte[] sign, String certificatePath) throws Exception {
		return verify(data, sign, getCertificate(certificatePath));
	}
	public static boolean verify(byte[] data, byte[] sign, byte[] cer) throws Exception {
		return verify(data, sign, getCertificate(cer));
	}

}
