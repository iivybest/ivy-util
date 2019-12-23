/**
 * @Filename BloomFilter
 * @author Ivybest
 * @version V1.0
 * @CreateDate 2018年5月16日 下午7:47:43
 * @Company IB.
 * @Copyright Copyright(C) 2010-
 * All rights Reserved, Designed By Ivybest
 * <p>
 * Modification History:
 * Date				Author		Version		Discription
 * --------------------------------------------------------
 * 2018年5月16日	Ivybest			1.0			new create
 */
package org.ivy.xutil;

import java.util.BitSet;

/**
 * @Classname BloomFilter
 * @author Ivybest imiaodev@163.com
 * @Createdate 2018年5月16日 下午7:47:43
 * @Version 1.0
 * ------------------------------------------
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class BloomFilter {
    private int bit_size;
    private int[] seeds;

    private BitSet bits;

    private BloomFilter() {
    }

    private BloomFilter setBit_size(int bit_size) {
        this.bit_size = bit_size;
        return this;
    }

    private BloomFilter initialize() {
        this.seeds = new int[]{3, 5, 7, 11, 13, 31, 37, 61};
        this.bits = new BitSet(this.bit_size);
        return this;
    }


    public static BloomFilter newInstance(int bit_size) {
        return new BloomFilter().setBit_size(bit_size).initialize();
    }


}
