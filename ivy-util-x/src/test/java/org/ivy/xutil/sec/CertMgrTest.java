package org.ivy.xutil.sec;

import junit.framework.TestCase;
import org.apache.commons.lang.ArrayUtils;
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
import java.text.SimpleDateFormat;

/**
 * @author Ivybest imiaodev@163.com
 * @Classname CertMgrTest
 * @Createdate 2018年3月9日 下午7:52:28
 * @Version 1.0 ------------------------------------------
 * @Description java读取X.509 Cer中数据测试
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CertMgrTest extends TestCase {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // ----classpath 程序执行路径
    private String classpath = SystemUtil.getClasspath();

    // 证书路径
    private String path;
    private X509Certificate cer;
    private SimpleDateFormat formator;

    @Before
    public void setUp() {
        this.path = this.classpath + "material/cert/51.cer";
        this.formator = new SimpleDateFormat("yyyy-MM-dd");

        try (FileInputStream in = new FileInputStream(new File(this.path))) {
            this.cer = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
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
    public void testReadX509Cer() throws Exception {
        // 获得证书版本
        logger.debug("证书版本:" + this.cer.getVersion());
        // 获得证书序列号
        logger.debug("证书序列号:" + this.cer.getSerialNumber().toString(16));
        // 获得证书有效期
        logger.debug("证书生效日期:" + formator.format(this.cer.getNotBefore()));
        logger.debug("证书失效日期:" + formator.format(this.cer.getNotAfter()));
        // 获得证书主体信息
        logger.debug("证书拥有者:" + this.cer.getSubjectDN().getName());
        // 获得证书颁发者信息
        logger.debug("证书颁发者:" + this.cer.getIssuerDN().getName());
        // 获得证书签名算法名称
        logger.debug("证书签名算法:" + this.cer.getSigAlgName());
    }

    /**
     * @return void
     * @throws Exception
     * @Title testReadX509CerExtendedS
     * @Description 读取证书扩展字段
     */
    @Test
    public void testReadX509CerExtendedS() throws Exception {
        // 证书扩展字段
//		Set<String> sets = this.cer.getCriticalExtensionOIDs();
//		Iterator<String> it = sets.iterator();
//		for(; it.hasNext(); logger.debug(it.next()));

//		List<String> extendedKeys = this.cer.getExtendedKeyUsage();
//		if(null != extendedKeys) for(int i = 0; i < extendedKeys.size(); logger.debug(extendedKeys.get(i++)));

        // 根据扩展字段 key = "2.5.29.999.1" 查询 对应value ，即证书税号
        // getExtensionValue 获取信息为val的DER编码信息。
        String key = "2.5.29.999.1";
        // ----扩展域值的 bytes with DER code
        // ----DER 非典型编码规则
        byte[] extensionValDERCode = this.cer.getExtensionValue(key);
        if (extensionValDERCode != null) {
            // ----扩展域值的 bytes
            byte[] extensionValBytes = ArrayUtils.subarray(extensionValDERCode, 2, extensionValDERCode.length);
            logger.debug("{\r\n extensionValDERCode: {}, \r\n extensionValBytes: {}\r\n}",
                    this.bytearray2String(extensionValDERCode),
                    this.bytearray2String(extensionValBytes));
            logger.debug("cer扩展域：{key:{}, val: {}}", key, new String(extensionValBytes));
        } else {
            logger.debug("cer扩展域中没有key：{}", key);
        }
    }


    private String bytearray2String(byte[] bytearray) {
        StringBuffer sb = new StringBuffer("[");
        for (byte b : bytearray) sb.append(b).append(", ");
        sb.append(']');
        return sb.toString();
    }

}







