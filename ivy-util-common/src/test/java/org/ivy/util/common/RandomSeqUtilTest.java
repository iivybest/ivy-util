package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.annotation.Description;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * <p> description: 随机序列工具测试类
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className RandomSeqUtilTest
 * @date 2019/12/18 18:46
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
@Description("随机序列工具测试类")
public class RandomSeqUtilTest {

    private int count;

    @Before
    public void setUp() {
        this.count = 10;
    }


    @Test
    public void test_01_generateArabic() {
        String sequence = null;
        for (int i = 0; i < this.count; i++) {
            sequence = RandomSeqUtil.generateRandomArabic(8);
            log.debug("{sequence: {}}", sequence);
        }
    }

    @Test
    public void test_02_generateLetter() {
        String sequence = null;
        for (int i = 0; i < this.count; i++) {
            sequence = RandomSeqUtil.generateRandomLetter(8);
            log.debug("{sequence: {}}", sequence);
        }
    }

    @Test
    public void test_03_generateCapitalLetter() {
        String sequence = null;
        for (int i = 0; i < this.count; i++) {
            sequence = RandomSeqUtil.generateRandomCapitalLetter(8);
            log.debug("{sequence: {}}", sequence);
        }
    }

    @Test
    public void test_04_generateHex() {
        String sequence = null;
        for (int i = 0; i < this.count; i++) {
            sequence = RandomSeqUtil.generateRandomHex(8);
            log.debug("{sequence: {}}", sequence);
        }
    }

    @Test
    @Description("生成随机序列测试")
    public void test_05_generateSequence() {
        String sequence = null;
        for (int i = 0; i < this.count; i++) {
            sequence = RandomSeqUtil.generateRandomSequence(16);
            log.debug("{sequence: {}}", sequence);
        }
    }
}
