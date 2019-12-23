package org.ivy.xutil.sec;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * <p>DesUtl</p>
 * 
 * @author miao.xl
 * @date 2014-6-12 下午02:29:33  
 */
public class DesUtil {
	
	/*
	 * DESede/DES/BlowFish<br>
	 * DES的密钥(key)长度是为8个字节,加密解密的速度是最快的<br>
	 * DESede的密钥(key)长度是24个字节<br>
	 * BlowFish的密钥(key)是可变的 加密最快，强度最高,密钥长度范围(1<=key<=16)<br>
	 */
	
	/*
	 * 3DES即三重DES是DES的加强版也是DES的一个更安全的变形。
	 * 它使用3条56位共168位的密钥对数据进行三次加密一般情况下提供了较为强大的安全性。
	 * 实际上3DES是DES向AES过渡的加密算法。1999年NIST将3-DES指定为过渡的加密标准。 
	 * 3DES以DES为基本模块通过组合分组方法设计出分组加密算法。
	 * 令Ek()和Dk()表示DES算法的加密和解密过程
	 * 		K表示DES算法使用的密钥
	 * 		P表示明文C表示密文。3DES的具体实现过程如下 
	 * 	1加密过程C=Ek3(Dk2(Ek1(P)))
	 * 	2解密过程为P=Dk1((EK2(Dk3(C))) 
	 * 从上述过程可以看出 K1、K2、K3决定了算法的安全性。
	 * 若三个密钥互不相同本质上就相当于用一个长为168位的密钥进行加密。
	 * 若数据对安全性要求不高K1可等于K3。在这种情况下密钥的有效长度为112位。 
	 * 在Java的加密体系中使用3DES非常简单程序结构和使用DES时相同
	 * 只不过在初始化时将算法名称由“DES”改为“DESede”即可
	 */
	
	/*
	 * AES在密码学中是高级加密标准Advanced Encryption Standard的缩写
	 * 该算法是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES已经被多方分析且广为全世界所使用。
	 * 最近高级加密标准已然成为对称密钥加密中最流行的算法之一。 AES算法又称Rijndael加密法，
	 * 该算法为比利时密码学家Joan Daemen和Vincent Rijmen所设计，结合两位作者的名字，以Rijndael命名。
	 * AES是美国国家标准技术研究所NIST旨在取代DES的21世纪的加密标准。 
	 * AES算法将成为美国新的数据加密标准而被广泛应用在各个领域中。
	 * 尽管人们对AES还有不同的看法，但总体来说AES作为新一代的数据加密标准汇聚了强安全性、高性能、高效率、易用和灵活等优点。
	 * AES设计有三个密钥长度128192256位，相对而言AES的128密钥比DES的56密钥强得多。
	 * AES算法主要包括三个方面 轮变化、圈数和密钥扩展。关于其具体实现，读者可以参考密码学书籍。 
	 */
	
	public static final String AES = "AES";
	public static final String ARCFOUR = "ARCFOUR";
	public static final String BLOWFISH = "Blowfish";
	
	public static final String DES = "DES";
	public static final String DESEDE = "DESede";
	
	public static final String RC2 = "RC2";
	public static final String RC4 = "RC4";
	
	private final int ENCRYPT_MODE = 1;
	private final int DECRYPT_MODE = 2;
	
	
	/**
	 * <p>加密</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] encrypt(String algorithm, byte[] data, byte[] key){
		return this.DesAlgoNG(algorithm, data, key, this.ENCRYPT_MODE);
	}
	/**
	 * <p>加密</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @return
	 */
	public byte[] encrypt(String algorithm, byte[] data, String key){
		return this.encrypt(algorithm, data, key.getBytes());
	}
	
	/**
	 * <p>译文</P>
	 * 
	 * @param algorithm 算法名称
	 * @param cipher 密文
	 * @param key 密钥
	 * @return
	 */
	public byte[] decrypt(String algorithm, byte[] cipher, byte[] key){
		return this.DesAlgoNG(algorithm, cipher, key, this.DECRYPT_MODE);
	}
	/**
	 * <p>译文</P>
	 * 
	 * @param algorithm 算法名称
	 * @param cipher 密文
	 * @param key 密钥
	 * @return
	 */
	public byte[] decrypt(String algorithm, byte[] cipher, String key){
		return this.decrypt(algorithm, cipher, key.getBytes());
	}
	
	
	
	
	
	/**
	 * <p>DES算法</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @param model
	 * @return
	 */
	public byte[] DesAlgo(String algorithm, byte[] data, byte[] key, int model){
		byte[] handled = null;
		// keygen提供对称密钥生成器的功能支持各种算法
		KeyGenerator keygen = null;
		// secretKey 负责保存密钥
		SecretKey secretKey = null;
		// 随机数源
        SecureRandom sr = null;
		// cipher 负责加密解密工作
		Cipher cipher = null;
		try {
			keygen = KeyGenerator.getInstance(this.getAlgorithmType(algorithm));
			sr = new SecureRandom(key);
			keygen.init(sr);
			// 生成密钥
			secretKey = keygen.generateKey();
			// 生成Cipher对象指定其支持DES算法
			cipher = Cipher.getInstance(algorithm);
			// 根据密钥对Cipher对象进行初始化, DECRYPT_MODE表示解密模式
			cipher.init(model, secretKey);
			// 对original解密或加密运算
			handled = cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return handled;
	}
	
	/**
	 * <p>DES算法</p>
	 * 
	 * @param algorithm
	 * @param data
	 * @param key
	 * @param model
	 * @return
	 */
	public byte[] DesAlgoNG(String algorithm, byte[] data, byte[] key, int model){
		byte[] handled = null;
		SecretKey secretKey = new SecretKeySpec(key, this.getAlgorithmType(algorithm));
		IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(key, 0, 16));
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(model, secretKey, iv);
			handled = cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return handled;
	}
	
	/**
	 * @Title 		getAlgorithmType 
	 * @param 		algorithm
	 * @return 		String
	 * @Description 
	 * ---------------------------------
	 * 		AES
	 * 		AES/CBC/PKCS5Padding
	 * ---------------------------------
	 */
	public String getAlgorithmType(String algorithm) {
		return algorithm.split("/")[0];
	}
	
}
