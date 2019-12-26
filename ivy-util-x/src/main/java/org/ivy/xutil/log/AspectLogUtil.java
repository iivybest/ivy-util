package org.ivy.xutil.log;

/**
 * AspectLogUtil
 *
 * @author miao.xl
 * @version 1.0
 * @date 2016年3月23日-上午10:15:47
 */
public class AspectLogUtil {
    private LogUtil logger;

    public AspectLogUtil() {
        this.logger = new LogUtil();
    }

    public AspectLogUtil(String logUrl) {
        this.logger = new LogUtil(logUrl);
    }

    public <T> AspectLogUtil(Class<T> clz) {
        this.logger = new LogUtil(clz);
    }

    public <T> AspectLogUtil(Class<T> clz, String logUrl) {
        this.logger = new LogUtil(clz, logUrl);
    }

    /**
     * log
     *
     * @param t   class
     * @param msg message
     * @param <T> t's type
     */
    public <T> void log(T t, String msg) {
        this.logger.log("[" + t.getClass().getSimpleName() + "]====>" + msg);
    }

    /**
     * <p>log</p>
     *
     * @param t         当前被横切处理的类
     * @param signature 当前被横切处理方法
     * @param msg       日志消息
     * @param <T>       t's type
     */
    public <T> void log(T t, String signature, String msg) {
        this.logger.log("[" + t.getClass().getSimpleName() + "].[" + signature + "]====>" + msg);
    }


    public void log(String msg) {
        this.logger.log(msg);
    }


}
