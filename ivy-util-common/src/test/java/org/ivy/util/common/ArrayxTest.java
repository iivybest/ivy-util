package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.annotation.Description;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

/**
 * <p>  classname: ArrayxTest
 * <br> description: Arrayx tase case
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/23 11:27
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ArrayxTest {

    private byte[] data;
    private int count = 1_000_000;

    @Before
    public void setUp() {
        this.data = new byte[]{12, 11, -9, 36, 1, 0, -1, 41, 54};
    }

    //    @After
    public void tearDown() {
        log.debug("====> split line-----------------------------");
    }

    @Test
    public void test_00_initial() {
        log.debug("{Arrays.toString: {}}", Arrays.toString(data));
        log.debug("{Arrayx.printArray: {}}", Arrayx.printArray(data));
    }

    @Description({"性能测试对比", "test_01_ArrayxPrintArray", "test_02_ArraysToString"})
    @Test
    public void test_01_ArrayxPrintArray() {
        for (int i = 0; i < this.count; i++) {
            Arrayx.printArray(this.data);
        }
    }

    @Description({"性能测试对比", "test_01_ArrayxPrintArray", "test_02_ArraysToString"})
    @Test
    public void test_02_ArraysToString() {
        for (int i = 0; i < this.count; i++) {
            Arrays.toString(this.data);
        }
    }

    @Test
    public void test_03_derange() {
        Arrayx.derange(this.data, 0, this.data.length - 2);
        log.debug("{derange: {}}", Arrayx.printArray(this.data));
    }

    @Test
    public void test_04_shuffle() {
        Arrayx.shuffle(this.data, 0, this.data.length - 2);
        log.debug("{shuffle: {}}", Arrayx.printArray(this.data));
    }

    @Test
    public void test_05_derange() {
        Arrayx.derange(this.data, 0, this.data.length - 2);
        log.debug("{derange: {}}", Arrayx.printArray(this.data));
    }

    @Test
    public void test_06_subArray() {
        String[] arr = {"corolla", "audi", "buggati", "benz", "ford", "bently"};
        String[] subarr = Arrayx.subarray(arr, 1, 3);
        log.debug("{arr; {}}", Arrays.toString(arr));
        log.debug("{subarr; {}}", Arrays.toString(subarr));
    }
}
