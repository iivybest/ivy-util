package org.ivy.xutil.sec;

import java.io.UnsupportedEncodingException;
import java.security.*;

/**
 * <p>SignatureUtil</p>
 * 
 * @author miao.xl
 * @date 2014-6-13 上午10:35:58
 */
public class SignatureUtil {
	/*
	 * 1.5. 数字签名 所谓数字签名就是信息发送者用其私钥对从所传报文中提取出的特征数据（或称数字指纹）进行 RSA
	 * 算法操作，以保证发信人无法抵赖曾发过该信息
	 * （即不可抵赖性），同时也确保信息报文在经签名后末被篡改（即完整性）。当信息接收者收到报文后，就可以用发送者的公钥对数字签名进行验证。　
	 * 在数字签名中有重要作用的数字指纹是通过一类特殊的散列函数（HASH 函数）生成的，对这些 HASH 函数的特殊要求是：
	 * 接受的输入报文数据没有长度限制； 对任何输入报文数据生成固定长度的摘要（数字指纹）输出 从报文能方便地算出摘要；
	 * 难以对指定的摘要生成一个报文，而由该报文反推算出该指定的摘要； 两个不同的报文难以生成相同的摘要
	 */
	/*
	 * 〖3-2〗DSA算法 数字签名算法(Digital Signature Algorithm ,DSA)也是一种非对称加密算法,
	 * 被美国NIST作为数字签名标准(DigitalSignature Standard, DSS)。
	 * DSA是基于整数有限域离散对数难题的,其安全性与RSA相比差不多。 其原理和RSA类似。DSA一般应用于数字签名中
	 */
	
	public static final String RSA = "RSA";
	public static final String DSA = "DSA";
	// EC Key size must be at most 571 bits
//	public static final String EC = "EC";
	
	public static final String NONEWITHRSA = "NONEwithRSA";
	public static final String MD2WITHRSA = "MD2withRSA";
	public static final String MD5WITHRSA = "MD5withRSA";
	public static final String SHA1WITHRSA = "SHA1withRSA";
	public static final String SHA256WITHRSA = "SHA256withRSA";
	public static final String SHA384WITHRSA = "SHA384withRSA";
	public static final String SHA512WITHRSA = "SHA512withRSA";

//	public static final String NONEWITHDSA = "NONEwithDSA";
	public static final String SHA1WITHDSA = "SHA1withDSA";
	
//	public static final String NONEWITHECDSA = "NONEwithECDSA";
//	public static final String SHA1WITHECDSA = "SHA1withECDSA";
//	public static final String SHA256WITHECDSA = "SHA256withECDSA";
//	public static final String SHA384WITHECDSA = "NONEwithECDSA";
//	public static final String SHA512WITHECDSA = "NONEwithECDSA";

	// 编码方式
	private String encoding;
	// 密钥位数
	private int keysize;
	// 种子
	private String seed;
	
	/**
	 * constructors
	 */
	private SignatureUtil() {
		this.init();
	}
	public SignatureUtil(String encoding) {
		this();
		this.encoding = encoding;
	}
	public SignatureUtil(String encoding, int keysize){
		this(encoding);
		this.keysize = keysize;
	}
	public SignatureUtil(String encoding, String seed){
		this(encoding);
		this.seed = seed;
	}
	public SignatureUtil(String encoding, int keysize, String seed){
		this(encoding);
		this.keysize = keysize;
		this.seed = seed;
	}
	
	/**
	 * initalization
	 */
	private void init() {
		this.keysize = 1024;
		this.seed = "0f22507a10bbddd07d8a3082122966e3";
	}
	
	/*
	 * ============================================================== 
	 */
	
	/**
	 * <p>获取随机密钥对</p>
	 * 
	 * @return
	 */
	public KeyPair getKeyPair(String algorithm){
		// KeyPairGenerator 用于生成密钥对（公钥和私钥对）
		KeyPairGenerator keyPairGen = null;
		// KeyPair 密钥对
		KeyPair keyPair = null;
		// 安全的随机数
		SecureRandom sr = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance(algorithm);
			// 安全的随机数
			sr = new SecureRandom();
			sr.setSeed(this.seed.getBytes(this.encoding));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 初始化密钥对生成器，大小为1024位
		keyPairGen.initialize(keysize, sr);
		keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}
	
	/**
	 * <p>取得公钥</p>
	 * 
	 * @param keyPair
	 * @return
	 */
	public PublicKey getPublicKey(KeyPair keyPair){
		if(keyPair == null) return null;
		return keyPair.getPublic();
	}
	
	/**
	 * <p>取得密钥</p>
	 * 
	 * @param keyPair
	 * @return
	 */
	public PrivateKey getPrivateKey(KeyPair keyPair){
		if(keyPair == null) return null;
		return keyPair.getPrivate();
	}
	
	/**
	 * <p>签名</p>
	 * 
	 * @param algorithm
	 * @param original
	 * @param privateKey
	 * @return
	 */
	public byte[] sign(String algorithm, byte[] original, PrivateKey privateKey){
		byte[] digest = null;
		Signature signature = null;
		try {
			signature = Signature.getInstance(algorithm);
			signature.initSign(privateKey);
			signature.update(original);
			digest = signature.sign();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		
		return digest;
	}
	
	/**
	 * <p>签名</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public byte[] sign(String algorithm, String data, PrivateKey privateKey){
		return this.sign(algorithm, data.getBytes(), privateKey);
	}
	
	/**
	 * <p>校验</p>
	 * 
	 * @param algorithm
	 * @param digest
	 * @param publicKey
	 * @return
	 */
	public boolean verify(String algorithm, byte[] data, byte[] sign, PublicKey publicKey){
		boolean valid = false;
		Signature signature = null;
		try {
			signature = Signature.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(data);  
			valid = signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		}
		return valid;
	}
	
	/**
	 * <p>校验</p>
	 * 
	 * @param algorithm
	 * @param digest
	 * @param publicKey
	 * @return
	 */
	public boolean verify(String algorithm, String data, byte[] sign, PublicKey publicKey){
		return this.verify(algorithm, data.getBytes(), sign, publicKey);
	}

}
