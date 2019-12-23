/**
 * Filename 	CetificateUtilsTest
 *
 * @Description TODO
 * @author ivybest
 * @version V1.0
 * CreateDate 	2017年6月20日 下午2:43:57
 * Company 		IB.
 * Copyright 	Copyright(C) 2010-
 * All rights Reserved, Designed By ivybest
 * <p>
 * Modification History:
 * Date			Author		Version		Discription
 * --------------------------------------------------------
 * 2017年6月20日		ivybest		1.0			new create
 */
package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.FileUtil;
import org.ivy.util.common.SystemUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * <p> description: 证书工具类测试
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @className CetificateUtilsTest
 * @author Ivybest (ivybestdev@163.com)
 * @date 2017/6/20 14:43
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Slf4j
public class CetificateUtilsTest {

    // ----classpath 程序执行路径
    private String classpath = SystemUtil.getClasspath();
    // ----测试所需物料路径--基于classpath的相对路径
    private String pfx;
    private String cer;
    private String license;
    // ----测试成员变量
    private String pass = "123456";
    private String original;
    private KeyStore keyStore;

    @Before
    public void setUp() throws Exception {
        // ----测试所需物料路径--基于classpath的相对路径
        this.pfx = this.classpath + "material/cert/kimi.pfx";
        this.cer = this.classpath + "material/cert/kimi.cer";
        this.license = this.classpath + "material/cert/kimi.license";
        // ----私钥密码
        this.pass = "123456";
        // ----源文件信息
        this.original = "{product_name=your_product,product_version=1219,license_expiry=2020-10-21}";
        // ----密钥库
        this.keyStore = CertificateUtil.getKeyStore(pfx, pass, CertificateUtil.STORE_TYPE_PKCS12);
    }

    @After
    public void tearDown() {
        log.debug("====> split line -------------------------------------------");
    }


    @Test
    public void test_01_sign_and_verify() throws Exception {
        // ----keystore alias
        String alias = CertificateUtil.getKeySotreAliases(keyStore).get(0);
        // ----signature
        byte[] sign = CertificateUtil.sign(original.getBytes(), keyStore, alias, pass);
        // ----verify signature
        boolean verify = CertificateUtil.verify(original.getBytes(), sign, FileUtil.read(cer));
        log.debug("{alias:{}, verify: {}}", alias, verify);
        // ----expect verify is true
        assertEquals(verify, true);
    }

    @Test
    public void test_02_rsa_encrypt_and_decrypt() throws Exception {
        String alias = CertificateUtil.getKeySotreAliases(keyStore).get(0);
        PrivateKey privateKey = CertificateUtil.getPrivateKey(keyStore, alias, pass);
        byte[] cipher = CertificateUtil.encryptByPrivateKey(original.getBytes(), privateKey);
        byte[] plain = CertificateUtil.decryptByPublicKey(cipher, cer);

        log.debug("{\r\n origin:{}, \r\n cipher: {}, \r\n plain: {}\r\n}",
                this.original,
                Base64.getEncoder().encodeToString(cipher),
                new String(plain));
        assertEquals("decrypt failure", this.original, new String(plain));
    }
}











