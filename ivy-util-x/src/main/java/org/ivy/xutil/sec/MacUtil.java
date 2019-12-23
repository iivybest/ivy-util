package org.ivy.xutil.sec;

import org.bouncycastle.util.encoders.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <p> description: Mac 工具类
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @className MacUtil
 * @author Ivybest (ivybestdev@163.com)
 * @date 2014/6/11 17:17
 * @version 1.0
 */
public class MacUtil {
	
	/*
	 * 〖1-1〗加密的应用 加密是以某种特殊的算法改变原有的信息数据,使得未授权的用户即使获得了已加密的信息,
	 * 但因不知解密的方法,仍然无法了解信息的内容。数据加密技术已经广泛应用于因特网电子商务、 手机网络和银行自动取款机等领域。
	 * 
	 * 加密系统中有如下重要概念 
	 * 1明文。被隐蔽的消息称作明文plaintext。
	 * 2密文。隐蔽后的消息称作密文ciphertext。
	 * 3加密。将明文变换成密文的过程称作加密(encryption)。
	 * 4解密。由密文恢复出原明文的过程称作解密(decryption)。
	 * 5敌方。主要指非授权者通过各种办法窃取机密信息。
	 * 6被动攻击。获密文进行分析这类攻击称作被动攻击(passive attack) 。
	 * 7主动攻击。非法入侵者(tamper)采用篡改、伪造等手段向系统注入假消息, 称为主动攻击(active attack)。
	 * 8加密算法。对明文进行加密时采用的算法。 
	 * 9解密算法。对密文进行解密时采用算法。
	 * 10加密密钥和解密密钥。加密算法和解密算法的操作通常是在一组密钥 key 的控制下进行的 分别称为加密密钥(encryption key)和解密密钥(decryption key)。
	 * 
	 * 在加密系统中 加密算法和密钥是最重要的两个概念。在这里需要对加密算法和密钥进行一个解释。
	 * 以最简单的“恺撒加密法”为例。 《高卢战记》有描述恺撒曾经使用密码来传递信息即所谓的“恺撒密码”。
	 * 它是一种替代密码通过将字母按顺序推后3位起到加密作用如将字母A换作字母D将字母B换作字母E。
	 * 如“China”可以变为“Fklqd”。解密过程相反。在这个简单的加密方法中“向右移位”可以理解为加密算法
	 * “3”可以理解为加密密钥。对于解密过程“向左移位”可以理解为解密算法“3”可以理解为解密密钥。
	 * 显然密钥是一种参数，它是在明文转换为密文或将密文转换为明文的算法中输入的数据。
	 * 
	 * 恺撒加密法的安全性来源于两个方面 
	 * 第一对加密算法的隐藏
	 * 第二对密钥的隐蔽。 
	 * 
	 * 单单隐蔽加密算法以保护信息在学界和业界已有相当讨论一般认为是不够安全的。
	 * 公开的加密算法是给黑客长年累月攻击测试对比隐蔽的加密算法要安全多。 一般说来加密之所以安全是因为其加密的密钥的隐藏并非加密解密算法的保密。
	 * 而流行的一些加密解密算法一般是完全公开的。敌方如果取得已加密的数据 即使得知加密算法若没有密钥也不能进行解密。
	 */

	/*
	 * 一、概述 MAC算法结合了MD5和SHA算法的优势，并加入密钥的支持，是一种更为安全的消息摘要算法。 
	 * MAC（Message AuthenticationCode，消息认证码算法）是含有密钥的散列函数算法，兼容了MD和SHA算法的特性，并在此基础上加入了密钥。
	 * 日次，我们也常把MAC称为HMAC（keyed-Hash Message Authentication Code）。
	 * MAC算法主要集合了MD和SHA两大系列消息摘要算法。
	 * MD系列的算法有HmacMD2、HmacMD4、HmacMD5三种算法；
	 * SHA系列的算法有HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384.HmacSHA512五种算法。
	 * 经过MAC算法得到的摘要值也可以使用十六进制编码表示，其摘要值长度与参与实现的摘要值长度相同。
	 * 例如，HmacSHA1算法得到的摘要长度就是SHA1算法得到的摘要长度，都是160位二进制码，换算成十六进制编码为40位。
	 * 
	 * 二、实现和应用 1、Sun的实现和应用
	 * 在java6中，MAC系列算法需要通过Mac类提供支持。
	 * java6中仅仅提供HmacMD5、HmacSHA1、HmacSHA256、HmacSHA384和HmacSHA512四种算法。 
	 * Mac算法是带有密钥的消息摘要算法，所以实现起来要分为两步：
	 * 		1）、构建密钥 
	 * 		2）、执行消息摘要
	 */

	/*
	 * Java密码学结构设计遵循两个原则: 
	 * 		1) 算法的独立性和可靠性。
	 * 		2) 实现的独立性和相互作用性。
	 * 算法的独立性是通过定义密码服务类来获得。用户只需了解密码算法的概念, 而不用去关心如何实现这些概念。
	 * 实现的独立性和相互作用性通过密码服务提供器来实现。
	 * 密码服务提供器是实现一个或多个密码服务的一个或多个程序包。软件开发商根据一定接口, 将各种算法实现后,打包成一个提供器,用户可以安装不同的提供器。
	 * 安装和配置提供器,可将包含提供器的ZIP和JAR文件放在CLASSPATH下,再编辑Java安全属性文件来设置定义一个提供器。
	 * 
	 * DES算法及如何利用DES算法加密和解密类文件的步骤： 
	 * DES算法简介 
	 * DES（Data EncryptionStandard）是发明最早的最广泛使用的分组对称加密算法。
	 * DES算法的入口参数有三个：Key、Data、Mode。其中Key为8个字节共64位，
	 * 是DES算法的工作密钥；Data也为8个字节64位，是要被加密或被解密的数据；
	 * Mode为DES的工作方式，有两种：加密或解密。
	 */
	
	/* jdk has algorithm */
	public static final String HMACMD5 = "HmacMD5";
	public static final String HMACSHA1 = "HmacSHA1";
	public static final String HMACSHA256 = "HmacSHA256";
	public static final String HMACSHA384 = "HmacSHA384";
	public static final String HMACSHA512 = "HmacSHA512";
	
	/* BoncyCastle has algorithm */
	public static final String HMACMD2 = "HmacMD2";
	
	
	/**
	 * <p>Mac摘要加密</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] digest(String algorithm, byte[] data, String key) {
		if(algorithm == null || algorithm.length() == 0) return null;
		if(data == null || data.length == 0) return null;
		if(key == null || key.length() == 0) return null;
		
		byte[] cipherByte = digest(data, generateSecretKey(algorithm, key.getBytes()));
		return cipherByte;
	}

	/**
	 * <p>Mac摘要加密</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] digest(String algorithm, String data, String key) {
		if(data == null || data.length() == 0) return null;
		return digest(algorithm, data.getBytes(), key);
	}
	
	public static String digest2HexString(String algorithm, byte[] data, String key) {
		byte[] encryptBytes = digest(algorithm, data, key);
		return Hex.toHexString(encryptBytes);
	}
	
	public static String digest2HexString(String algorithm, String data, String key) {
		byte[] encryptBytes = digest(algorithm, data, key);
		return Hex.toHexString(encryptBytes);
	}
	
	
	/**
	 * <p>计算数据加密摘要</p>
	 * 
	 * @param original
	 * @param key
	 * @return
	 */
	private static byte[] digest(byte[] original, SecretKey key) {
		Mac mac = null;
		try {
			mac = Mac.getInstance(key.getAlgorithm());
			mac.init(key);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		mac.update(original);
		return mac.doFinal();
	}

	/**
	 * <p>创建密钥</p>
	 * 
	 * @param algorithm
	 * @return
	 */
	private static SecretKey createKey(String algorithm, byte[] key){
		// keygen提供对称密钥生成器功能，支持各种算法
		KeyGenerator keygen = null;
		// secretKey负责保存对称密钥
		SecretKey secretKey = null;
		// 随机数源
        SecureRandom sr = null;
		try {
			// 创建一个可信任的随机数源, 将key放到里边
			sr = new SecureRandom(key);
			keygen = KeyGenerator.getInstance(algorithm);
			keygen.init(sr);
			// 生成密匙
			secretKey = keygen.generateKey();
			/*
			 * 生成密钥 strategy 2，比较简洁
			 * 密钥为原生key字节码
			 * 密钥与SecureRandom生成的不同
			 */
//			secretKey = new SecretKeySpec(key.getBytes(), algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return secretKey;
	}
	/**
	 * <p>创建密钥</p>
	 * 
	 * @param algorithm
	 * @return
	 */
	private static SecretKey generateSecretKey(String algorithm, byte[] key){
		SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
		return secretKey;
	}
	
	/**
	 * <p>生成一个随机key</p>
	 * 
	 * @return
	 */
	public static String getKey(){
		StringBuffer sb = new StringBuffer();
		final int numCount = 8;
		final int letterCount = 8;
		final int numLen = 10;
		final int letterLen = 26;
		char[] num = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		char[] letter = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		
		Random random = new Random();
		for (int i = 0; i < letterCount; i++) sb.append(letter[random.nextInt(letterLen)]);
		for (int i = 0; i < numCount; i++) sb.append(num[random.nextInt(numLen)]);
		
		return sb.toString();
	}
}
