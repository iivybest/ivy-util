package org.ivy.xutil;

import java.util.BitSet;

/**
 * @author Ivybest imiaodev@163.com
 * @version 1.0
 * ------------------------------------------
 * @className BloomFilter
 * @date 2018年5月16日 下午7:47:43
 */
/**
 * <p> description: bloom filter
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2018/5/16 19:47
 */
public class BloomFilter {
    private int bitSize;
    private int[] seeds;

    private BitSet bits;

    private BloomFilter() {
    }

    public static BloomFilter newInstance(int bitSize) {
        return new BloomFilter().setBitSize(bitSize).initialize();
    }

    private BloomFilter setBitSize(int bitSize) {
        this.bitSize = bitSize;
        return this;
    }

    private BloomFilter initialize() {
        this.seeds = new int[]{3, 5, 7, 11, 13, 31, 37, 61};
        this.bits = new BitSet(this.bitSize);
        return this;
    }


}
