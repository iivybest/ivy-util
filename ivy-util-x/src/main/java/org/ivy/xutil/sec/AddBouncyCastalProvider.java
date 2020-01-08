package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ivy.xutil.log.LogUtil;

import java.security.Provider;
import java.security.Security;
import java.util.stream.Stream;

/**
 * <p> description:
 * <br>---------------------------------------------------------
 * <br> add bouncyCastalProvider support
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2018/5/10 13:52
 */
@Slf4j
public class AddBouncyCastalProvider {

    static {
        Security.addProvider(new BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        Stream.of(providers).map(e -> e.getName()).forEach(log::debug);
    }

    public static void main(String[] args) {
    }

}
