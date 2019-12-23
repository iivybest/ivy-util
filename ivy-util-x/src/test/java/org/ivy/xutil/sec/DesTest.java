package org.ivy.xutil.sec;

import org.apache.commons.codec.binary.Base64;
import org.ivy.util.common.Arrayx;
import org.ivy.util.common.StringUtil;
import org.ivy.xutil.log.LogUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;


/**
 * <p>DesTest</p>
 * 
 * @author miao.xl
 * @date 2014-6-11 下午04:25:34  
 */
public class DesTest {
	private static LogUtil log = new LogUtil(DesTest.class);
	
	private String plain;
	private byte[] key;
	
	@Before
	public void setup() {
		this.plain = "我是一只草泥马-caonima-&^%*$#-@@#-草泥马";
		this.key = Base64.decodeBase64("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG=");
	}
	
	@After
	public void tearDown() {
		log.log("-------------------one test operation completed-------------------->");
	}
	
	
	@Test
	public void test_03_des() throws UnsupportedEncodingException {
		log.log("key : " + key + " \n" + "keyByte : " + Arrayx.printArray(key));
		
		String[] algos = {
//				DesUtil.DES
//				, DesUtil.DESEDE
//				"AES"
//				, DesUtil.BLOWFISH
//				, DesUtil.RC2
//				, DesUtil.RC4
//				, "AES/CBC/NoPadding"		
				 "AES/CFB/PKCS5Padding"
				, "AES/CTR/PKCS5Padding"
//				, "AES/CBC/PKCS7Padding"
				};
		
		for(int i = 0; i < algos.length; this.desTestUnit(algos[i++]));
		
	}
	
	
	private void desTestUnit(String algorithm) {
		try {
			log.log("algorithm : " + algorithm);
			
			byte[] cipher = SecurityUtil.des.encrypt(algorithm, plain.getBytes(), key);
			log.log("cipher (len) : " + cipher.length + ", " + Arrayx.printArray(cipher));
			
			byte[] plain = SecurityUtil.des.decrypt(algorithm, cipher, key);
			log.log("plain : " + new String(plain));
			
			log.log("--------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}





