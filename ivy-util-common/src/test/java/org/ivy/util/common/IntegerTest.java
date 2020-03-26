package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/3/21 11:01
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegerTest {

    @Test
    public void test() {
        int record = 0b00_000_000_000_000_000_000_000_000_000_000;
        boolean isRecord = false;
        // ----设置记录使用 | 检查记录使用 &
        record |= 1;
        isRecord = (record & 1) != 0;
        log.debug("{record: {}, isRecord: {}, binary: {}}", record, isRecord, DigitUtil.toBinString(record));

        // ----记录第二个数据项
        record |= 2;
        isRecord = (record & 2) != 0;
        log.debug("{record: {}, isRecord: {}, binary: {}}", record, isRecord, DigitUtil.toBinString(record));

        // ----记录第三个数据项
        record |= 4;
        isRecord = (record & 4) != 0;
        log.debug("{record: {}, isRecord: {}, binary: {}}", record, isRecord, DigitUtil.toBinString(record));

        // ----再次检查第一项
        isRecord = (record & 1) != 0;
        log.debug("{record: {}, isRecord: {}, binary: {}}", record, isRecord, DigitUtil.toBinString(record));
    }

}





