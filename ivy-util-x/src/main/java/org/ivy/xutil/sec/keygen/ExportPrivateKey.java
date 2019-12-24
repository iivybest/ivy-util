package org.ivy.xutil.sec.keygen;

import org.ivy.util.common.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Base64;

/**
 * <p> description: 导出私钥证书中私钥
 * <br>--------------------------------------------------------
 * <br> TODO
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className ExportPrivateKey
 * @date 2019/12/18 9:31
 */
public class ExportPrivateKey {
    private File keystoreFile;
    private String keyStoreType;
    private char[] password;
    private String alias;
    private File exportedFile;

    public static void main(String args[]) throws Exception {
        ExportPrivateKey export = new ExportPrivateKey();
        export.keystoreFile = new File("E:/Ivybest/test/Aisino.einv.cmcc.CA.pfx");
        export.keyStoreType = "PKCS12";
        export.password = "123456".toCharArray();
        export.alias = "Aisino.einv.cmcc.CA";
        export.exportedFile = new File("E:/Ivybest/test/Aisino.einv.cmcc.CA.privkey");
        export.export();
    }

    public KeyPair getKeyPair(KeyStore keystore, String alias, char[] password) {
        try {
            Key key = keystore.getKey(alias, password);
            if (key instanceof PrivateKey) {
                Certificate cert = keystore.getCertificate(alias);
                PublicKey publicKey = cert.getPublicKey();
                return new KeyPair(publicKey, (PrivateKey) key);
            }
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void export() throws Exception {
        KeyStore keystore = KeyStore.getInstance(keyStoreType);
        keystore.load(new FileInputStream(keystoreFile), password);
        KeyPair keyPair = this.getKeyPair(keystore, alias, password);
        PrivateKey privateKey = keyPair.getPrivate();
        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        StringBuilder sb = new StringBuilder();
        sb.append("----BEGIN PRIVATE KEY----")
                .append("\n")
                .append(encoded)
                .append("\n")
                .append("----END PRIVATE KEY----");
        FileUtil.write(exportedFile, sb.toString().getBytes());
    }
}