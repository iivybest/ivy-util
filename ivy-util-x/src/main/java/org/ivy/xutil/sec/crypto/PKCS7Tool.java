package org.ivy.xutil.sec.crypto;

import org.ivy.util.common.FileUtil;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.PKCS9Attributes;
import sun.security.pkcs.SignerInfo;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;

/**
 * <p> description: PKCS7Tool
 * <br>--------------------------------------------------------
 * <br> pkcs7格式签名工具
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/17 13:54
 */
public class PKCS7Tool {
    /**
     * 签名
     */
    private static final int SIGNER = 1;
    /**
     * 验证
     */
    private static final int VERIFIER = 2;
    /**
     * JVM 提供商
     */
    private static char jvm = 0;
    private static Class algorithmId = null;
    private static Class derValue = null;
    private static Class objectIdentifier = null;
    private static Class x500Name = null;
    private static boolean debug = false;
    /**
     * 用途
     */
    private int mode = 0;
    /**
     * 摘要算法
     */
    private String digestAlgorithm = "SHA1";
    /**
     * 签名算法
     */
    private String signatureAlgorithm = "SHA1withRSA";
    /**
     * 签名证书链
     */
    private X509Certificate[] certificates = null;
    /**
     * 签名私钥
     */
    private PrivateKey privateKey = null;
    /**
     * 根证书
     */
    private Certificate rootCertificate = null;

    /**
     * 私有构造方法
     */
    private PKCS7Tool(int mode) {
        this.mode = mode;
    }

    /**
     * 取得签名工具 加载证书库, 取得签名证书链和私钥
     *
     * @param keyStorePath     证书库路径
     * @param keyStorePassword 证书库口令
     * @param keyPassword      key password
     * @return PKCS7Tool
     * @throws GeneralSecurityException Exception
     * @throws IOException              IOException
     */
    public static PKCS7Tool getSigner(String keyStorePath, String keyStorePassword, String keyPassword)
            throws GeneralSecurityException, IOException {
        init();
        // 加载证书库
        KeyStore keyStore = null;
        if (keyStorePath.toLowerCase().endsWith(".pfx"))
            keyStore = KeyStore.getInstance("PKCS12");
        else
            keyStore = KeyStore.getInstance("JKS");


        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            keyStore.load(fis, keyStorePassword.toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        // 在证书库中找到签名私钥
        Enumeration<String> aliases = keyStore.aliases();
        String keyAlias = null;
        if (aliases != null) {
            while (aliases.hasMoreElements()) {
                keyAlias = (String) aliases.nextElement();
                Certificate[] certs = keyStore.getCertificateChain(keyAlias);
                if (certs == null || certs.length == 0) continue;

                X509Certificate cert = (X509Certificate) certs[0];
                if (matchUsage(cert.getKeyUsage(), 1)) {
                    try {
                        cert.checkValidity();
                    } catch (CertificateException e) {
                        continue;
                    }
                    break;
                }
            }
        }

        // 没有找到可用签名私钥
        if (keyAlias == null)
            throw new GeneralSecurityException("None certificate for sign in this keystore");
        if (debug) System.out.println(keyAlias);
        X509Certificate[] certificates = null;
        if (keyStore.isKeyEntry(keyAlias)) {
            // 检查证书链
            Certificate[] certs = keyStore.getCertificateChain(keyAlias);
            for (int i = 0; i < certs.length; i++) {
                if (!(certs[i] instanceof X509Certificate))
                    throw new GeneralSecurityException("Certificate[" + i + "] in chain '" + keyAlias + "' is not a X509Certificate.");
            }
            // 转换证书链
            certificates = new X509Certificate[certs.length];
            for (int i = 0; i < certs.length; i++)
                certificates[i] = (X509Certificate) certs[i];
        } else if (keyStore.isCertificateEntry(keyAlias)) {
            // 只有单张证书
            Certificate cert = keyStore.getCertificate(keyAlias);
            if (cert instanceof X509Certificate) {
                certificates = new X509Certificate[]{(X509Certificate) cert};
            }
        } else {
            throw new GeneralSecurityException(keyAlias + " is unknown to this keystore");
        }
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
        // 没有私钥抛异常
        if (privateKey == null) {
            throw new GeneralSecurityException(keyAlias + " could not be accessed");
        }
        PKCS7Tool tool = new PKCS7Tool(SIGNER);
        tool.certificates = certificates;
        tool.privateKey = privateKey;
        return tool;
    }

    /**
     * 取得验签名工具 加载信任根证书
     *
     * @param rootCertificatePath 根证书路径
     * @throws GeneralSecurityException GeneralSecurityException
     * @throws IOException              IOException
     */
    public static PKCS7Tool getVerifier(String rootCertificatePath) throws GeneralSecurityException, IOException {
        init();
        // 加载根证书
        Certificate rootCertificate = null;
        try (FileInputStream fis = new FileInputStream(rootCertificatePath)) {
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            try {
                rootCertificate = certificatefactory.generateCertificate(fis);
            } catch (Exception exception) {
                byte[] content = FileUtil.read(fis);
                InputStream is = new ByteArrayInputStream(Base64.getDecoder().decode(content));
                rootCertificate = certificatefactory.generateCertificate(is);
            }
        }
        PKCS7Tool tool = new PKCS7Tool(VERIFIER);
        tool.rootCertificate = rootCertificate;
        return tool;
    }

    /**
     * 匹配私钥用法
     *
     * @param keyUsage keyUsage
     * @param usage    usage
     * @return boolean
     */
    private static boolean matchUsage(boolean[] keyUsage, int usage) {
        if (usage == 0 || keyUsage == null)
            return true;
        for (int i = 0; i < Math.min(keyUsage.length, 32); i++) {
            if ((usage & (1 << i)) != 0 && !keyUsage[i])
                return false;
        }
        return true;
    }

    private static void init() {
        if (jvm != 0)
            return;
        String vendor = System.getProperty("java.vm.vendor");
        if (vendor == null)
            vendor = "";
        String vendorUC = vendor.toUpperCase();
        try {
            if (vendorUC.indexOf("SUN") >= 0) {
                jvm = 'S';
                algorithmId = Class.forName("sun.security.x509.AlgorithmId");
                derValue = Class.forName("sun.security.util.DerValue");
                objectIdentifier = Class.forName("sun.security.util.ObjectIdentifier");
                x500Name = Class.forName("sun.security.x509.X500Name");
            } else if (vendorUC.indexOf("IBM") >= 0) {
                jvm = 'I';
                algorithmId = Class.forName("com.ibm.security.x509.AlgorithmId");
                derValue = Class.forName("com.ibm.security.util.DerValue");
                objectIdentifier = Class.forName("com.ibm.security.util.ObjectIdentifier");
                x500Name = Class.forName("com.ibm.security.x509.X500Name");
            } else {
                System.out.println("Not support JRE: " + vendor);
                System.exit(-1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static void setDebug(boolean debug) {
        PKCS7Tool.debug = debug;
    }

    /**
     * signature
     *
     * @param data 数据
     * @return signature    签名结果
     * @throws GeneralSecurityException Exception
     * @throws IOException              Exception
     * @throws IllegalArgumentException Exception
     */
    public String sign(byte[] data) throws Exception {
        if (mode != SIGNER) throw new IllegalStateException("====call a PKCS7Tool instance not for signature.");
        Signature signer = Signature.getInstance(signatureAlgorithm);
        signer.initSign(privateKey);
        signer.update(data, 0, data.length);
        byte[] signedAttributes = signer.sign();
        ContentInfo contentInfo = null;
        Field data_oidField = ContentInfo.class.getField("DATA_OID");
        Object data_oid = data_oidField.get(null);
        Constructor contentInfoConstructor = ContentInfo.class.getConstructor(new Class[]{data_oid.getClass(), derValue});
        contentInfo = (ContentInfo) contentInfoConstructor.newInstance(new Object[]{data_oid, null});
        // 根证书
        X509Certificate x509 = certificates[certificates.length - 1];
        BigInteger serial = x509.getSerialNumber();
        // X500Name
        Constructor x500NameConstructor = x500Name.getConstructor(new Class[]{String.class});
        Object x500NameObject = x500NameConstructor.newInstance(new Object[]{x509.getIssuerDN().getName()});
        // AlgorithmId
        Method algorithmIdGet = algorithmId.getMethod("get", new Class[]{String.class});
        Object digestAlgorithmId = algorithmIdGet.invoke(null, new Object[]{digestAlgorithm});
        Field algorithmIdfield = algorithmId.getField("RSAEncryption_oid");
        Object rsaOid = algorithmIdfield.get(null);
        Constructor algorithmConstructor = algorithmId.getConstructor(new Class[]{objectIdentifier});
        Object algorithmRsaOid = algorithmConstructor.newInstance(new Object[]{rsaOid});
        // SignerInfo
        Constructor signerInfoConstructor = SignerInfo.class.getConstructor(new Class[]{x500Name, BigInteger.class,
                algorithmId, PKCS9Attributes.class, algorithmId, byte[].class, PKCS9Attributes.class});
        // 签名信息
        SignerInfo si = (SignerInfo) signerInfoConstructor.newInstance(new Object[]{x500NameObject, // X500Name,
                // issuerName,
                serial,            // x509.getSerialNumber(), BigInteger serial,
                digestAlgorithmId,    // AlgorithmId, digestAlgorithmId,
                null,                // PKCS9Attributes, authenticatedAttributes,
                algorithmRsaOid,    // AlgorithmId, digestEncryptionAlgorithmId,
                signedAttributes,    // byte[] encryptedDigest,
                null                // PKCS9Attributes unauthenticatedAttributes)
        });
        SignerInfo[] signerInfos = {si};
        // 构造PKCS7数据
        Object digestAlgorithmIds = Array.newInstance(algorithmId, 1);
        Array.set(digestAlgorithmIds, 0, digestAlgorithmId);
        // PKCS7
        Constructor pkcs7Constructor = PKCS7.class.getConstructor(new Class[]{
                digestAlgorithmIds.getClass(),
                ContentInfo.class,
                X509Certificate[].class,
                signerInfos.getClass()});
        PKCS7 p7 = (PKCS7) pkcs7Constructor
                .newInstance(new Object[]{digestAlgorithmIds, contentInfo, certificates, signerInfos});
        // PKCS7 p7 = new PKCS7(digestAlgorithmIds, contentInfo, certificates,
        // signerInfos);
        // public PKCS7(com.ibm.security.x509.AlgorithmId[] arg0,
        // sun.security.pkcs.ContentInfo arg1,
        // java.security.cert.X509Certificate[] arg2,
        // sun.security.pkcs.SignerInfo[] arg3);
        // public PKCS7(sun.security.x509.AlgorithmId[] arg0,
        // sun.security.pkcs.ContentInfo arg1,
        // java.security.cert.X509Certificate[] arg2,
        // sun.security.pkcs.SignerInfo[] arg3);
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        p7.encodeSignedData(baout);
        // Base64编码
        return Base64.getEncoder().encodeToString(baout.toByteArray());
    }

    /**
     * 验证签名(无CRL)
     *
     * @param signature 签名签名结果
     * @param data      被签名数据
     * @param dn        签名证书dn, 如果为空则不做匹配验证
     * @throws IOException              Exception
     * @throws NoSuchAlgorithmException Exception
     * @throws SignatureException       Exception
     * @throws InvalidKeyException      Exception
     * @throws CertificateException     Exception
     * @throws NoSuchProviderException  Exception
     */
    public void verify(String signature, byte[] data, String dn) throws IOException, NoSuchAlgorithmException,
            SignatureException, InvalidKeyException, CertificateException, NoSuchProviderException {
        if (mode != VERIFIER)
            throw new IllegalStateException("call a PKCS7Tool instance not for verify.");
        byte[] sign = Base64.getDecoder().decode(signature);
        PKCS7 p7 = new PKCS7(sign);
        X509Certificate[] certs = p7.getCertificates();
        if (debug)
            for (int i = 0; i < certs.length; i++) {
                X509Certificate cert = certs[i];
                System.out.println("SIGNER " + i + "=\n" + cert);
                System.out.println("SIGNER " + i + "=\n" + Base64.getEncoder().encodeToString(cert.getEncoded()));
            }
        // 验证签名本身、证书用法、证书扩展
        SignerInfo[] sis = p7.verify(data);
        // check the results of the verification
        if (sis == null)
            throw new SignatureException("Signature failed verification, data has been tampered");
        for (int i = 0; i < sis.length; i++) {
            SignerInfo si = sis[i];
            X509Certificate cert = si.getCertificate(p7);
            // 证书是否过期验证，如果不用系统日期可用cert.checkValidity(date);
            cert.checkValidity();
            if (!cert.equals(rootCertificate)) {
                // 验证证书签名
                cert.verify(rootCertificate.getPublicKey());
            }
            // 验证dn
            if (i == 0 && dn != null) {
                X500Principal name = cert.getSubjectX500Principal();
                if (!dn.equals(name.getName(X500Principal.RFC1779)) && !new X500Principal(dn).equals(name))
                    throw new SignatureException(
                            "Signer dn '" + name.getName(X500Principal.RFC1779) + "' does not matchs '" + dn + "'");
            }
        }
    }

    /**
     * @return 返回 digestAlgorithm。
     */
    public final String getDigestAlgorithm() {
        return digestAlgorithm;
    }

    /**
     * @param digestAlgorithm 要设置的 digestAlgorithm。
     */
    public final void setDigestAlgorithm(String digestAlgorithm) {
        this.digestAlgorithm = digestAlgorithm;
    }

    /**
     * @return signatureAlgorithm 返回 signatureAlgorithm。
     */
    public final String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * @param signatureAlgorithm 要设置的 signingAlgorithm。
     */
    public final void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }
}