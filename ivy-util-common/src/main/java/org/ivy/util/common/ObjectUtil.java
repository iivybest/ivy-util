/**
 * @Package edu.hit.utility.util.utils
 * @author miao.xl
 * @date 2016年3月24日-下午12:30:35
 */
package org.ivy.util.common;

import java.util.ArrayList;
import java.util.List;

/**
 * ObjectUtil
 *
 * @author miao.xl
 * @version 1.0
 * @date 2016年3月24日-下午12:30:35
 */
public class ObjectUtil {
    private final static int SIZE = 5000;
    private static final Runtime RUNTIME = Runtime.getRuntime();

    public static <T> long sizeof(T t) {
        runGC();
        long start = RUNTIME.freeMemory();
        T bean = t;
        runGC();
        long end = RUNTIME.freeMemory();
        return start - end;
    }

    private static void runGC() {
        long usedMem1 = usedMemory();
        long usedMem2 = Long.MAX_VALUE;
        for (int i = 0; (usedMem1 < usedMem2) && (i < SIZE); ++i) {
            RUNTIME.runFinalization();
            RUNTIME.gc();
            Thread.yield();
            usedMem2 = usedMem1;
            usedMem1 = usedMemory();
        }
    }

    private static long usedMemory() {
        return RUNTIME.totalMemory() - RUNTIME.freeMemory();
    }


    public static void main(String[] args) {
        int size = 10000;
        runGC();
        long start = RUNTIME.freeMemory();
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add("item_" + i);
        }
        runGC();
        long end = RUNTIME.freeMemory();
        long si = start - end;
        System.out.printf("%.2f", si / 1024D / 1024D);
    }


}
