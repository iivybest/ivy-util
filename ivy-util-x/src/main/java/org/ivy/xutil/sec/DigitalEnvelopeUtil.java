package org.ivy.xutil.sec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * 数字信封工具
 * @author Ivybest
 */
public class DigitalEnvelopeUtil {
	private static String encoding = "utf-8";
	
	public static void main(String[] args) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		
		// 明文
		String plain = "hello digital envelope";
		// alice、anthony、martial 的 公钥证书
		X509Certificate aliceCert = (X509Certificate) CertificateUtil.getCertificate("D:/alice.cer");
		X509Certificate anthonyCert = (X509Certificate) CertificateUtil.getCertificate("D:/anthony.cer");
		X509Certificate martialCert = (X509Certificate) CertificateUtil.getCertificate("D:/martial.cer");
		
		// 数字信封生成器
		CMSEnvelopedDataGenerator generator = new CMSEnvelopedDataGenerator();
		// 添加收件人 -- 可以添加多人
		generator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(aliceCert).setProvider("BC"));
		generator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(anthonyCert).setProvider("BC"));
		generator.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(martialCert).setProvider("BC"));
		// 数据处理
		CMSTypedData msg = new CMSProcessableByteArray(plain.getBytes());
		// 将数据装入数字信封中 -- 密文
//		CMSEnvelopedData envelopedData = generator.generate(msg, new JceCMSContentEncryptorBuilder(PKCSObjectIdentifiers.rc4).setProvider("BC").build());
		CMSEnvelopedData envelopedData = generator.generate(msg, new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC).setProvider("BC").build());
		String cipher = Hex.encodeHexString(envelopedData.getEncoded());
		
		System.out.println("--------> plain: " + plain);
		System.out.println("--------> cipher_len: " + cipher.length());
		System.out.println("--------> cipher: " + cipher);
		
		
		
		/* alice 读取电子信封中数据 */
		KeyStore aliceKeyStore = CertificateUtil.getKeyStore("D:/alice.pfx", "123456", CertificateUtil.STORE_TYPE_PKCS12);
		PrivateKey alicePrivateKey = CertificateUtil.getPrivateKey(aliceKeyStore, "alice", "123456");
		System.out.println("--------> alice-plain:" + openEnvelope(cipher, alicePrivateKey, encoding));
		
		/* anthony 读取电子信封中数据 */
		KeyStore anthonyKeyStore = CertificateUtil.getKeyStore("D:/anthony.pfx", "123456", CertificateUtil.STORE_TYPE_PKCS12);
		PrivateKey anthonyPrivateKey = CertificateUtil.getPrivateKey(anthonyKeyStore, "anthony", "123456");
		System.out.println("--------> anthony-plain:" + openEnvelope(cipher, anthonyPrivateKey, encoding));
		
		/* martial 读取电子信封中数据 */
		KeyStore martialKeyStore = CertificateUtil.getKeyStore("D:/martial.pfx", "123456", CertificateUtil.STORE_TYPE_PKCS12);
		PrivateKey martialPrivateKey = CertificateUtil.getPrivateKey(martialKeyStore, "martial", "123456");
		System.out.println("--------> martial-plain:" + openEnvelope(cipher, martialPrivateKey, encoding));
		
	}
	
	
	
	/**
	 * @Title: openEnvelope
	 * @param @param cipher
	 * @param @param privateKey
	 * @param @param encoding
	 * @param @throws CMSException
	 * @param @throws DecoderException
	 * @param @throws UnsupportedEncodingException
	 * @return String
	 * @throws
	 * @Description: 打开信封
	 */
	public static String openEnvelope(String cipher, PrivateKey privateKey, String encoding) 
			throws CMSException, DecoderException, UnsupportedEncodingException {
		CMSEnvelopedData ed = new CMSEnvelopedData(Hex.decodeHex(cipher.toCharArray()));
		RecipientInformationStore store = ed.getRecipientInfos();
		 
		Recipient recipient = new JceKeyTransEnvelopedRecipient(privateKey);
		List<RecipientInformation> recipientInfos = (List<RecipientInformation>) store.getRecipients();
		
		byte[] plainBytes = null;
		for (RecipientInformation e : recipientInfos) {
			try {
				plainBytes = e.getContent(recipient);
				break;
			} catch (Exception ex) {
//				ex.printStackTrace();
			}
		}
		if(plainBytes == null) return cipher;
		return new String(plainBytes, encoding);
	}
	
}



















