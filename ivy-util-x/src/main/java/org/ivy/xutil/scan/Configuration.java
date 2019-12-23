package org.ivy.xutil.scan;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>Configuration</p>
 * <p>Description : </p>
 *
 * @author miao.xl
 * @version 1.0
 * @date 2015年7月1日 - 上午9:27:19
 */
public class Configuration {
    private Map<String, String> args;
    private ReadWriteLock lock;
    private PropertiesScanner scanner;

    public void initialize() {
        lock = new ReentrantReadWriteLock();
        this.args = this.scanner.scan();
    }

    public void refresh() {
        lock.writeLock().lock();
        try {
            this.args = this.scanner.scan();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void update() {
        Map<String, String> increments = this.scanner.update();
        if (increments == null) return;

        lock.writeLock().lock();
        try {
            this.args.putAll(increments);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void destroy() {
        lock.writeLock().lock();
        try {
            this.args = null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getProperty(String key) {
        lock.readLock().lock();
        try {
            return this.args.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(String key) {
        return this.args.containsKey(key);
    }


}

//定时检测资源文件更新情况
class PropertiesScanThread implements Runnable {
    private int internal;
    private boolean stop;
    private PropertiesScanner scanner;

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!this.stop) this.scan();
    }

    private void scan() {

    }

    ;

}










