/**
 * @Filename BouncyCastalProvider
 * @author Ivybest
 * @version V1.0
 * @date 2018年5月10日 下午4:08:17
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
 * <p>  classname: AddBouncyCastalProvider
 * <br> description: 添加 bouncyCastalProvider支持
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2018/5/10 13:52
 */
public class AddBouncyCastalProvider {
    public static LogUtil log = new LogUtil(AddBouncyCastalProvider.class);

    static {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        for (int i = 0, len = providers.length; i < len; log.log(providers[i++])) ;
    }


    public static void main(String[] args) {
        System.out.println("hello bouncy castal");
    }

}
