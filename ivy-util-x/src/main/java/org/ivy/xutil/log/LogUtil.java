package org.ivy.xutil.log;

import org.apache.commons.lang.StringUtils;
import org.ivy.util.cfg.IvyUtilConf;
import org.ivy.util.cfg.IvyUtilConstant;
import org.ivy.util.common.DateTimeUtil;
import org.ivy.util.common.FileUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> 自定义日志工具
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @className LogUtil
 * @author Ivybest (ivybestdev@163.com)
 * @date 2014/5/19 09:24
 * @version 1.0
 */
public class LogUtil {
	/* LogUtil工具内部参数 start **********************************/
	/* 路径分隔符*/
	private String separator;
	/* 默认日志名 */
	private String defaultLogName;
	/* LogUtil工具内部参数 end ************************************/
	
	/* 记录日志者的class类型*/
	private Class<?> logClass;
	/* 日志路径 */
	private String logUrl;
	/* 以线程ID分割日志 */
	private boolean splitLogByThreadId;
	/* 日志文件流 */
	private PrintWriter writer;
	
	/* constructor*/
	public <T> LogUtil(Class<T> logClass, String logUrl, boolean splitLogByThreadId) {
		this.logClass = logClass;
		this.logUrl = logUrl;
		this.splitLogByThreadId = splitLogByThreadId;
		this.initialize();
	}
	/* constructor*/
	public <T> LogUtil(Class<T> logClass, String logUrl) {
		this(logClass, logUrl, false);
	}
	/* constructor*/
	public <T> LogUtil(String logUrl, boolean splitLogByThreadId) {
		this(LogUtil.class, logUrl, splitLogByThreadId);
	}
	/* constructor*/
	public <T> LogUtil(Class<T> logClass) {
		this(logClass, IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
	}
	/* constructor*/
	public LogUtil(String logUrl){
		this(LogUtil.class, logUrl);
	}
	/* constructor */
	public LogUtil() {
		this(IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
	}
	
	/* 初始化 */
	private void initialize(){
		this.separator = "/";
		this.defaultLogName = "log.log";
		this.checkLogDir();
		try {
			this.writer = new PrintWriter(new FileWriter(this.getCurrentLogUrl(), true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* 
	 * <p>Title: finalize</p> 
	 * <p>Description: 销毁前，关闭流</p> 
	 * @see java.lang.Object#finalize() 
	 */
	@Override
	protected void finalize() throws Throwable {
		if(null != this.writer) {
			this.writer.flush();
			this.writer.close();
			this.writer = null;
		}
		super.finalize();
	}
	
	
	/* 设置日志文件路径*/
	private void setLogUrl(String logDir){
		this.logUrl = logDir.replace("\\", this.separator);
	}
	
	/* 获取当前需要记录日志的路径 */
	private String getCurrentLogUrl() {
		if(this.splitLogByThreadId) return this.logUrl + "_" + Thread.currentThread().getId();
		else return this.logUrl;
	}
	
	/* 检查日志路径*/
	private void checkLogDir(){
		/* 若无指定日志目录，设定为系统默认路径  默认路径配置在资源文件中  */
		if(StringUtils.isBlank(this.logUrl)) 
			this.setLogUrl(IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
		
		/* 规则：指定路径文件不存在时
		 * 		若目标路径不含“.”认定为其为路径, 日志记录在该路径下的文件中，日志文件以log.log命名
		 * 		若包含“.”，则认定其为文件，日志记录此文件中。
		 * 
		 * 	1、检查路径是否存在，
		 * 	2、若存在，检查其是目录还是文件，
		 * 		2.1、若为文件，无动作
		 * 		2.2、若为目录，则在该目录新建log.log，并将log.log指定为日志路径
		 * 
		 * 	3 、若不存在，检查其是目录还是文件（规则如上面定义）
		 * 		3.1、若为文件
		 * 			3.1.1、检查其父目录
		 * 		3.2、若为目录
		 * 			3.2.2、检查该目录，指定log.log为日志路径
		 */
		File file = new File(this.logUrl);
		if(file.exists()){
			if(file.isDirectory()) this.setLogUrl(file.getAbsolutePath() + this.separator + this.defaultLogName);
		}else if(!file.exists()){
			// 含有“.”按文件操作
			if(this.logUrl.contains(".")) FileUtil.createNewFile(file);
			else {
				// 按目录操作
				String path = file.getAbsolutePath() + this.separator + this.defaultLogName;
				FileUtil.createNewFile(path);
				this.setLogUrl(path);
			}
		}
	}
	
	/* 日志前缀*/
	private String prifix(){
		return "[" + DateTimeUtil.currentDateTime("yyyy-MM-dd HH:mm:ss:SSS")
				+ "]-[" + Thread.currentThread().getId() 
				+ "]-[" + this.logClass.getName() + "]-";
	}
	
	/* 格式化打印文本*/
	private String format(String data){
		return this.prifix() + data;
	}
	
	/**
	 * <p>控制台打印</p>
	 * 
	 * @param data
	 */
	private void print(Object data){
		System.out.println(data);
	}
	
	/**
	 * <p>以字符流记日志</p>
	 * 
	 * @param data
	 */
	public void log(Object data){
		this.log(data, true);
	}
	
	/**
	 * 记录日志并打印
	 * @param data
	 * @param printable
	 * 
	 * @author miao.xl
	 * @date 2014年6月20日 上午9:49:32
	 * @version 1.0
	 */
	public void log(Object data, boolean printable){
		if(null == data) data = "null";
		data = this.format(data.toString());
		
		if(printable) this.print(data);
		this.writer.println(data);
		this.writer.flush();
	}
	
	/**
	 * <p>以字节流记日志</p>
	 * 
	 * @param data
	 */
	public void logByBytes(String data){
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		try (FileOutputStream fos = new FileOutputStream(this.getCurrentLogUrl(), true);
				FileChannel channel = fos.getChannel()){
			buffer.put(data.getBytes());
			buffer.flip();
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}



