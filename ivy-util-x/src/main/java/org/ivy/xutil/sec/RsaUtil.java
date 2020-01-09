package org.ivy.xutil.sec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <p> description: RsaUtil
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/06/12 14:42
 */
public class RsaUtil {
    /*
     * 【3】实现非对称加密
     * 在非对称加密算法过程中，接收方产生一个公开密钥和一个私有密钥，前者公开。
     * 发送方将明文用接收方的公开密钥进行处理变成密文发送出去，接收方收到密文后，
     * 使用自己的私有密钥对密文解密，恢复为明文。在这种通信过程中，密钥由接收方产生，
     * 公开密钥公开，私有密钥保密。这里面有几个特点：
     * 		1加密时使用的公开密钥解密时必须使用对应的私有密钥否则无法解密。
     * 		2对同样的信息，可以用私有密钥加密，用公开密钥解密；也可以用公开密钥加密，用私有密钥解密。
     * 			但是在应付窃听上后者用得较多。
     * RSA和DSA。
     *
     * 〖3-1〗用Java实现RSA
     * RSA算法出现于20世纪70年代年，它是第一个既能用于数据加密也能用于数字签名的算法。
     * 它易于理解和操作，也很流行。算法的名字以发明者的名字命名Ron Rivest、 AdiShamir和Leonard Adleman。
     * 不过，RSA的安全性一直未能得到理论上的证明。RSA是被研究得最广泛的公钥算法，从提出到现在已近二十年，
     * 经历了各种攻击的考验，逐渐为人们接受。普遍认为是目前最优秀的公钥方案之一。
     * RSA的安全性依赖于大数的因子分解，但并没有从理论上证明破译RSA的难度与大数分解难度等价。
     * RSA的缺点主要有：
     * 		1产生密钥很麻烦受到素数产生技术的限制因而难以做到一次一密
     * 		2分组长度太大为保证安全性运算代价很高尤其是速度较慢较对称密码算法慢几个数量级
     * 			且随着大数分解技术的发展这个长度还在增加不利于数据格式的标准化。
     * RSA的安全性依赖于大数分解。公钥和私钥都是两个大素数的函数。
     * 据猜测，从一个密钥和密文推断出明文的难度等同于分解两个大素数的积。
     * 关于RSA算法的描述读者可以参考相关文献。RSA 可用于数字签名，具体操作时考虑到安全性和信息量较大等因素，
     * 一般可以先作HASH运算。  如前所述RSA的安全性依赖于大数分解，但是否等同于大数分解一直未能得到理论上的证明，
     * 因为没有证明破解RSA就一定需要作大数分解。由于进行的都是大数计算，使得RSA最快的情况也比DES慢很多倍，无论是软件还是硬件实现。
     * 速度一直是RSA的缺陷。一般来说只用于少量数据加密。
     */

    /*
     * 公开密钥加密，私有密钥解密
     */
    private final String RSA = "RSA";

    private final int ENCRYPT_MODE = 1;
    private final int DECRYPT_MODE = 2;
    // 密钥位数
    private final int KEYSIZE = 1024;
    private String encoding;

    public RsaUtil(String encoding) {
        this.encoding = encoding;
    }

    /**
     * get one random key pair
     *
     * @return KeyPair
     */
    public KeyPair getKeyPair() {
        // KeyPairGenerator 用于生成密钥对（公钥和私钥对）
        KeyPairGenerator keyPairGen = null;
        // KeyPair 密钥对
        KeyPair keyPair = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(this.RSA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 安全的随机数
        SecureRandom sr = new SecureRandom();
        // 初始化密钥对生成器，大小为1024位
        keyPairGen.initialize(KEYSIZE, sr);
        keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    /**
     * get public key
     *
     * @param keyPair keyPair
     * @return RSAPublicKey
     */
    public RSAPublicKey getPublicKey(KeyPair keyPair) {
        RSAPublicKey publicKey = null;
        publicKey = (RSAPublicKey) keyPair.getPublic();
        return publicKey;
    }

    /**
     * get private key
     *
     * @param keyPair keyPair
     * @return RSAPrivateKey
     */
    public RSAPrivateKey getPrivateKey(KeyPair keyPair) {
        RSAPrivateKey privateKey = null;
        privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return privateKey;
    }


    /**
     * encrypt
     *
     * @param data      data
     * @param publicKey publicKey
     * @return byte[]
     */
    public byte[] encrypt(byte[] data, RSAPublicKey publicKey) {
        return this.rsaAlgo(data, publicKey, ENCRYPT_MODE);
    }

    /**
     * encrypt
     *
     * @param data      data
     * @param publicKey publicKey
     * @return byte[]
     */
    public byte[] encrypt(String data, RSAPublicKey publicKey) {
        byte[] msg = null;
        try {
            msg = data.getBytes(this.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this.encrypt(msg, publicKey);
    }

    public byte[] decrypt(byte[] original, RSAPrivateKey privateKey) {
        return this.rsaAlgo(original, privateKey, DECRYPT_MODE);
    }

    /**
     * decrypt to string
     *
     * @param data       data
     * @param privateKey privateKey
     * @return String
     */
    public String decryptToString(byte[] data, RSAPrivateKey privateKey) {
        String plaintext = null;
        byte[] temp = this.decrypt(data, privateKey);
        try {
            plaintext = new String(temp, this.encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return plaintext;
    }

    /**
     * RSA algorithm
     *
     * @param data  data
     * @param key   key
     * @param model model
     * @return byte[]
     */
    private byte[] rsaAlgo(byte[] data, Key key, int model) {
        byte[] handled = null;
        // Cipher负责完成加密解密工作
        Cipher cipher = null;
        try {
            // 生成Cipher对象指定其支持RSA算法
            cipher = Cipher.getInstance(this.RSA);
            // 根据密钥对Cipher对象进行初始化, ENCRYPT_MODE表示加密模式
            if (ENCRYPT_MODE == model) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else if (DECRYPT_MODE == model) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            // 对original解密或加密运算
            handled = cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return handled;
    }

//	public void encrypt(){
//		// KeyPairGenerator 用于生成密钥对（公钥和私钥对）
//		KeyPairGenerator keyPairGen = null;
//		// KeyPair 密钥对
//		KeyPair keyPair = null;
//		// RSAPrivateKey 私钥
//		RSAPrivateKey privateKey = null;
//		// RSApublicKey 公钥
//		RSAPublicKey publicKey = null;
//		// Cipher负责完成加密解密工作
//		Cipher cipher = null;
//		try {
//			keyPairGen = KeyPairGenerator.getInstance(RSA);
//			// 初始化密钥对生成器，大小为1024位
//			keyPairGen.initialize(KEYSIZE);
//			keyPair = keyPairGen.generateKeyPair();
//			privateKey = (RSAPrivateKey) keyPair.getPrivate();
//			publicKey = (RSAPublicKey) keyPair.getPublic();
//			cipher = Cipher.getInstance(RSA);
//			String original = "我是一只草泥马---------------------";
//			// 公钥加密
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			byte[] cipherByte = cipher.doFinal(original.getBytes(this.encoding));
//			System.out.println("cipher : " + TestDigest.byte2str(cipherByte));
//			
//			// 密钥解密
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			byte[] plainByte = cipher.doFinal(cipherByte);
//			System.out.println("plain : " + new String(plainByte, this.encoding));
//			
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (NoSuchPaddingException e) {
//			e.printStackTrace();
//		} catch (InvalidKeyException e) {
//			e.printStackTrace();
//		} catch (IllegalBlockSizeException e) {
//			e.printStackTrace();
//		} catch (BadPaddingException e) {
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//	}

}
