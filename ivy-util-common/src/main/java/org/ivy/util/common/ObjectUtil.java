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
 * @date 2016年3月24日-下午12:30:35
 * @version 1.0
 * 
 */
public class ObjectUtil {
	private final static int _SIZE = 5000;
	private static final Runtime rt = Runtime.getRuntime();
	
	public static <T> long sizeof(T t) {
		runGC();
		long start = rt.freeMemory();
		T _t = t;
		runGC();
		long end = rt.freeMemory();
		return start - end;
	}

	private static void runGC() {
		long usedMem1 = usedMemory(); 
		long usedMem2 = Long.MAX_VALUE;
		for (int i = 0; (usedMem1 < usedMem2) && (i < _SIZE); ++i) {
			rt.runFinalization();
			rt.gc();
			Thread.yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory();
		}
	}
	
	private static long usedMemory() {
		return rt.totalMemory() - rt.freeMemory();
	}
	
	
	
	public static void main(String[] args) {
		int size = 10000;
		runGC();
		long start = rt.freeMemory();
		List<String> list = new ArrayList<>(size);
		for(int i = 0; i < size; i++) list.add("item_" + i);
		runGC();
		long end = rt.freeMemory();
		long si = (long)start - (long)end;
		System.out.printf("%.2f", (long)si / 1024D / 1024D );
	}
	
	
}
