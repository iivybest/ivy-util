package org.ivy.xutil.sec;

import junit.framework.TestCase;
import org.apache.commons.lang.ArrayUtils;
import org.ivy.util.common.Arrayx;
import org.ivy.util.common.DateTimeUtil;
import org.ivy.util.common.SystemUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p> classname: CertMgrTest
 * <p> description: test java read data in X.509 Cert
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2018/3/9 14:49
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CertMgrTest extends TestCase {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String classpath = SystemUtil.getClasspath();
    /**
     * cerificate path
     */
    private String path;
    private X509Certificate cert;

    @Before
    public void setUp() {
        this.path = this.classpath + "material/cert/51.cer";

        try (FileInputStream in = new FileInputStream(new File(this.path))) {
            this.cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取*.cer公钥证书文件， 获取公钥证书信息
     */
    @Test
    public void test_01_readX509Cert() throws Exception {
        log.debug("    证书版本: {}", this.cert.getVersion());
        log.debug("  证书序列号: {}", this.cert.getSerialNumber().toString(16));
        log.debug("证书生效日期: {}", DateTimeUtil.format(this.cert.getNotBefore()));
        log.debug("证书失效日期: {}", DateTimeUtil.format(this.cert.getNotAfter()));
        log.debug("  证书拥有者: {}", this.cert.getSubjectDN().getName());
        log.debug("  证书颁发者: {}", this.cert.getIssuerDN().getName());
        log.debug("证书签名算法: {}", this.cert.getSigAlgName());
    }

    @Test
    public void test_02_readX509CertExtended() throws Exception {
        // 证书扩展字段
        Set<String> sets = this.cert.getCriticalExtensionOIDs();
        Iterator<String> it = sets.iterator();
        for (; it.hasNext(); log.debug(it.next())) ;

        List<String> extendedKeys = this.cert.getExtendedKeyUsage();
        if (null != extendedKeys)
            for (int i = 0; i < extendedKeys.size(); log.debug(extendedKeys.get(i++))) ;


        // 根据扩展字段 key = "2.5.29.999.1" 查询对应 value，即证书税号
        // getExtensionValue 获取信息为 val 的 DER 编码信息。
        String key = "2.5.29.999.1";
        // ----扩展域值的 bytes with DER code
        // ----DER 非典型编码规则
        byte[] extensionValDERCode = this.cert.getExtensionValue(key);
        if (extensionValDERCode != null) {
            // ----扩展域值的 bytes
            byte[] extensionValBytes = ArrayUtils.subarray(extensionValDERCode, 2, extensionValDERCode.length);
            log.debug("{\r\n extensionValDERCode: {}, \r\n extensionValBytes: {}\r\n}",
                    Arrays.toString(extensionValDERCode),
                    Arrayx.printArray(extensionValBytes));
            log.debug("\r\ncer扩展域：{key:{}, val: {}}", key, new String(extensionValBytes));
        } else {
            log.debug("cer扩展域中没有key：{}", key);
        }
    }
}







