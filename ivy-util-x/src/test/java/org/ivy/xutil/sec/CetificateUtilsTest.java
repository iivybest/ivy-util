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
import static org.junit.Assert.assertTrue;

/**
 * <p> className: CetificateUtilsTest
 * <p> description: cerificate util testcase
 * <br>--------------------------------------------------------
 * <br> 1、init cerificate、pfx
 * <br> 2、signature、verify
 * <br> 3、encrypt、decrypt
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2017/6/20 14:43
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@Slf4j
public class CetificateUtilsTest {

    // ----classpath
    private String classpath = SystemUtil.getClasspath();
    // ----all materials that was used in this testcase--based on classpath
    private String pfx;
    private String cer;
    private String license;
    // ----variables
    private String pass;
    private String origin;
    private KeyStore keyStore;

    @Before
    public void setUp() throws Exception {
        // ----material path--based on classpath
        this.pfx = this.classpath + "material/cert/kimi.pfx";
        this.cer = this.classpath + "material/cert/kimi.cer";
        this.license = this.classpath + "material/cert/kimi.license";
        // ----private cert key
        this.pass = "123456";
        // ----source text
        this.origin = "{product_name=SkyNet,product_version=1.0_release,license_expiry=2022-11-10}";
        // ----key store
        this.keyStore = CertificateUtil.getKeyStore(pfx, pass, CertificateUtil.STORE_TYPE_PKCS12);
    }

    @After
    public void tearDown() {
        log.debug("====> split line ----------------------------------------");
    }


    @Test
    public void test_01_sign_and_verify() throws Exception {
        // ----keystore alias
        String alias = CertificateUtil.getKeySotreAliases(keyStore).get(0);
        // ----signature
        byte[] sign = CertificateUtil.signature(origin.getBytes(), keyStore, alias, pass);
        // ----verify signature
        boolean verify = CertificateUtil.verify(origin.getBytes(), sign, FileUtil.read(cer));
        log.debug("{alias:{}, verify: {}}", alias, verify);
        // ----expect verify is true. assertEquals(verify, true);
        assertTrue(verify);
    }

    @Test
    public void test_02_rsa_encrypt_and_decrypt() throws Exception {
        String alias = CertificateUtil.getKeySotreAliases(keyStore).get(0);
        PrivateKey privateKey = CertificateUtil.getPrivateKey(keyStore, alias, pass);
        byte[] cipher = CertificateUtil.encryptByPrivateKey(origin.getBytes(), privateKey);
        byte[] plain = CertificateUtil.decryptByPublicKey(cipher, cer);

        log.debug("{\r\norigin: {}, \r\ncipher: {}, \r\n plain: {}\r\n}",
                this.origin,
                Base64.getEncoder().encodeToString(cipher),
                new String(plain));
        assertEquals("decrypt failure", this.origin, new String(plain));
    }
}











