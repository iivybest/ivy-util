package org.ivy.xutil.sec;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.Arrayx;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

/**
 * <p> description: CaesarCodeUtil testcase
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/25 9:45
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CaesarCodeUtilTest {
    private char[] data;
    private char[] cipher;
    private int step;

    @Before
    public void setUp() {
        this.data = "helloworld!".toCharArray();
        this.cipher = "khoorzruog$".toCharArray();
        this.step = 1;
    }

    @Test
    public void test_01_encryptByDefaultStep() {
        char[] plain = Arrayx.subarray(this.data, 0, this.data.length - 1);
        String encrypt = CaesarCodeUtil.encryptToString(plain);

        log.debug("{\nplain: {}, \ncipher: {}\n}", Arrays.toString(this.data), cipher);

        Assert.assertEquals(encrypt, new String(this.cipher));
    }

    @Test
    public void test_02_decryptByDefaultStep() {
        char[] ciphers = Arrayx.subarray(this.cipher, 0, this.cipher.length - 1);
        char[] decrypt = CaesarCodeUtil.decrypt(ciphers);
        log.debug("{\nplain: {}, \ncipher: {}\n}", Arrays.toString(decrypt), this.cipher);

        Assert.assertEquals(new String(decrypt), new String(this.data));
    }




}
