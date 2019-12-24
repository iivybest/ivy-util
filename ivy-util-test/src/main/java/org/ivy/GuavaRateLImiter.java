/**
 * Filename 	GuavaRateLImiter
 *
 *
 * @author ivybest
 * @version V1.0
 * CreateDate 	2017年9月13日 下午8:04:05
 * Company 		IB.
 * Copyright 	Copyright(C) 2010-
 * All rights Reserved, Designed By ivybest
 * <p>
 * Modification History:
 * Date			Author		Version		Discription
 * --------------------------------------------------------
 * 2017年9月13日		ivybest		1.0			new create
 */
package org.ivy;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @className GuavaRateLImiter
 *
 * @author
 * Createdate    2017年9月13日 下午8:04:05
 */
public class GuavaRateLImiter {

    public static void main(String[] args) {
        // 按比例限流，tps为10， 大约100 mills可通过一个请求
        RateLimiter limiter = RateLimiter.create(100);
        for (int i = 0; i < 10; i++) {
            System.out.println(limiter.acquire());
            // tryAcquire 试图获取通过请求，超时时间 100 毫秒
            System.out.println(limiter.tryAcquire(100, TimeUnit.MILLISECONDS));
        }
    }
}
