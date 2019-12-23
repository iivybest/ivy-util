/**
 * @Filename BouncyCastalProvider
 * @author Ivybest
 * @version V1.0
 * @CreateDate 2018年5月10日 下午4:08:17
 * @Company IB.
 * @Copyright Copyright(C) 2010-
 * All rights Reserved, Designed By Ivybest
 * <p>
 * Modification History:
 * Date				Author		Version		Discription
 * --------------------------------------------------------
 * 2018年5月10日	Ivybest			1.0			new create
 */
package org.ivy.xutil.sec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ivy.xutil.log.LogUtil;

import java.security.Provider;
import java.security.Security;

/**
 * @Classname BouncyCastalProvider
 * @author Ivybest imiaodev@163.com
 * @Createdate 2018年5月10日 下午4:08:17
 * @Version 1.0
 * ------------------------------------------
 * @Description 添加bouncyCastalProvider支持
 */
public class AddBouncyCastalProvider {
    public static LogUtil log = new LogUtil(AddBouncyCastalProvider.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        for (int i = 0, len = providers.length; i < len; log.log(providers[i++])) ;
    }
}
