/**
 *
 */
package org.ivy.util.common;


import org.ivy.util.annotation.Recommend;

import java.util.UUID;

/**
 * <p>  classname: UniSeqUtil
 * <br> description: 唯一序列工具类
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @date 2018/12/13 9:27
 * @version 1.0
 */
public class UniSeqUtil {

    /**
     * <p> description: 生成一个唯一序列号
     * <br>--------------------------------------------------------
     * <br> uniseq = timestamp-uuid-sead
     * <br> uuid 按照 “-” 进行分割，取第 5 部分
     * <br>--------------------------------------------------------
     * </p>
     * @param sead sead
     * @return String String
     */
    @Recommend(value = false, msg = "高并发下有重复的可能性")
    public static String generateUniSeq(String sead) {
        String uuid = generateUUID();
        long timestamp = DateTimeUtil.getTimestamp(null);
        return timestamp + "-" + uuid.split("-")[4].toUpperCase() + "-" + (StringUtil.isBlank(sead) ? "0000" : sead);
    }

    public static String generateUniSeq() {
        return generateUniSeq(null);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateUUIDFormat() {
        return generateUUID().replace("-", "").toUpperCase();
    }

}









