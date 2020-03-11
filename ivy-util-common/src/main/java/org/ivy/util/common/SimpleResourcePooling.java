package org.ivy.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author: ivybest imiaodev@163.com
 * @date: 2019年5月6日 下午5:02:49
 * @Description: 带缓存的资源池
 * <p>-------------------------------------------------
 * <br> 1、使用内存池，避免多次new对象的资源开销
 * <br> 2、使用AtomicInteger、BlockingQueue，保证多线程并发性
 * <p>-------------------------------------------------
 */
public class SimpleResourcePooling<T> {


    private int capcity_max_default = 100;
    private int capcity_max;
//	private int capcity_min_default = 1;
//	private int capcity_min;

    private AtomicInteger capcity;
    private AtomicInteger capcity_free;
	/**
	 * ----资源池--维护所有资源对象索引
	 */
    private Collection<T> resPool;
	/**
	 * ----空闲资源池--从该池中获取资源对象--并且使用后归还池中
	 */
    private BlockingQueue<T> freeResPool;

    private SimpleResourcePooling() {
    }

    ;

    public static <T> SimpleResourcePooling<T> getInstance(int capcity, Supplier<T> supplier) {
        return new SimpleResourcePooling<T>().build(capcity, supplier);
    }

    public SimpleResourcePooling<T> build(int capcity, Supplier<T> supplier) {
        this.capcity_max = capcity <= 0 ? this.capcity_max_default : capcity;
        this.capcity = new AtomicInteger(this.capcity_max);
        this.capcity_free = new AtomicInteger(this.capcity.get());
        this.resPool = new ArrayList<T>(this.capcity.get());
        this.freeResPool = new LinkedBlockingQueue<T>(this.capcity.get());

        for (int i = 0; i < this.capcity.get(); i++) {
            this.generateResource(supplier);
        }

        return this;
    }


    public int getCapcity() {
        return capcity.get();
    }

    public int getFreeCapcity() {
        return capcity_free.get();
    }

    public T get() {
        T e = null;
        try {
            e = this.freeResPool.take();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        this.capcity_free.decrementAndGet();
        return e;
    }

    private T generateResource(Supplier<T> supplier) {
        T e = supplier.get();
        this.resPool.add(e);
        this.freeResPool.offer(e);
        return e;
    }


    public boolean release(T t) {
        if (!this.resPool.contains(t)) {
            return false;
        }
        if (this.freeResPool.contains(t)) {
            return true;
        }
        boolean realse = false;

        this.capcity_free.incrementAndGet();
        try {
            this.freeResPool.put(t);
            realse = true;
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return realse;
    }

    /**
     * 内部静态类
     *
     * @param <T>
     */
    public static interface Supplier<T> {
        T get();
    }
}

	
	
	
	
	
	
	
	
	
	
	
