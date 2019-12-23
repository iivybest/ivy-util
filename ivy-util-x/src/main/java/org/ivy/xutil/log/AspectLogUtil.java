/**
 * @Package edu.hit.utility.xutil.log 
 * @author miao.xl
 * @date 2016年3月23日-上午10:15:47
 */
package org.ivy.xutil.log;

/**
 * <p>AspectLogUtil</p>
 * <p>横切日志工具</p>
 *
 * @author miao.xl
 * @date 2016年3月23日-上午10:15:47
 * @version 1.0
 */
public class AspectLogUtil {
	private LogUtil _log;
	
	public AspectLogUtil() {
		this._log = new LogUtil();
	}
	public AspectLogUtil(String logUrl) {
		this._log = new LogUtil(logUrl);
	}
	public <T> AspectLogUtil(Class<T> clz) {
		this._log = new LogUtil(clz);
	}
	public <T> AspectLogUtil(Class<T> clz, String logUrl) {
		this._log = new LogUtil(clz, logUrl);
	}
	
	/**
	 * <p>log</p>
	 * @Description 记录日志
	 * @param t	当前被横切处理的类
	 * @param msg 
	 *
	 * @author miao.xl
	 * @date 2016年3月23日-上午10:21:18
	 */
	public <T> void log(T t, String msg) {
		this._log.log("[" + t.getClass().getSimpleName() + "]====>" + msg);
	}
	
	/**
	 * <p>log</p>
	 * @Description TODO
	 * @param t 当前被横切处理的类
	 * @param signature	当前被横切处理方法
	 * @param msg 日志消息
	 *
	 * @author miao.xl
	 * @date 2016年3月23日-上午10:48:23
	 */
	public <T> void log(T t, String signature, String msg){
		this._log.log("[" + t.getClass().getSimpleName() + "].["+ signature +"]====>" + msg);
	}
	
	
	public void log(String msg) {
		this._log.log(msg);
	}
	
	
}
