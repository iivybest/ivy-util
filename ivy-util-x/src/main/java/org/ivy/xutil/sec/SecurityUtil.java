/**
 * @Package edu.hit.utility.xutil.sec
 * @author miao.xl
 * @date 2016年3月22日-下午3:44:52
 */
package org.ivy.xutil.sec;

import org.ivy.xutil.log.LogUtil;

import java.security.Provider;
import java.security.Security;

/**
 * <p>SecurityUtil</p>
 * <p>安全相关工具</p>
 *
 * @author miao.xl
 * @date 2016年3月22日-下午3:44:52
 * @version 1.0
 *
 */
public interface SecurityUtil {
    public static LogUtil log = new LogUtil(SecurityUtil.class);

    // 添加bouncycastalprovider支持
//	AddBouncyCastalProvider abcp = new AddBouncyCastalProvider();


    //	String ENCODING = UtilityCfg.getProperty(UtilityConstant.ENCODING);
    String ENCODING = "UTF-8";

    Base64Util base64 = new Base64Util();
    CaesarCodeUtil caesarcode = new CaesarCodeUtil();
    DesUtil des = new DesUtil();
    MacUtil mac = new MacUtil();
    MessageDigestUtil md = new MessageDigestUtil();
    RsaUtil rsa = new RsaUtil(ENCODING);
    Keygen keygen = new Keygen();
    SignatureUtil signature = new SignatureUtil(ENCODING);


    default void addBouncyCastalProvider() {
        Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; log.log(providers[i++].getName())) ;
    }


}
