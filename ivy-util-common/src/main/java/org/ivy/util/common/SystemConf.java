package org.ivy.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;


/**
 * <p> description: 系统配置参数
 * <br>--------------------------------------------------------
 * <br>	project 初始化时，加载系统配置项
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @className SystemConf
 * @author Ivybest (ivybestdev@163.com)
 * @date 2017/6/20 12:24
 * @version 1.0
 */
public enum SystemConf {
	instance;
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemConf.class);
	
	private ReentrantReadWriteLock lock; 
	private ReadLock readLock;
	private WriteLock writeLock;
	
	// SystemConf 读取 properties的位置
	private  Map<String, String> propertiesPathMap;
	// SystemConf 所有常量
	private  Map<String, String> keyValueMap;
	
	private SystemConf() {
		this.init();
	};
	
	private SystemConf init() {
		return this.initReadWriteLock()		// 初始化读写锁
				.initArgs()					// 初始化参数
				.loadProp()					// 读取所有配置文件
				;
	}
	
	private SystemConf initReadWriteLock() {
		this.lock = new ReentrantReadWriteLock();
		this.readLock = lock.readLock();
		this.writeLock = lock.writeLock();
		return this;
	}
	
	/**
	 * 初始化成员变量
	 */
	private SystemConf initArgs() {
		this.keyValueMap = new HashMap<String, String>();
		this.initPropertiesPathMap();
		return this;
	}
	
	/**
	 * <p> description:  初始化配置文件加载路径
	 * <br>---------------------------------------------------------
	 * <br>	thinking:
	 * <br>	1、组件将配置文件放到 defaults 目录下，引用工程不设置配置时，使用默认属性
	 * <br>	2、引用工程需要使用个性化配置时，重写所需属性，放到 properties 目录下
	 * <br>	3、properties 权限大于 defaults
	 * <br>	4、properties、defaults 中有相同配置项，则 properties 中配置有效
	 * <br>---------------------------------------------------------
	 * <br>	properties 资源文件定义位置及优先级顺序
	 * <br>	1、classpath/config/properties/../*.properties
	 * <br>	2、classpath/config/*.properties
	 * <br>	3、classpath/*.properties
	 * <br>	4、classpath/config/defaults/../*.properties
	 * <br> 5、classpath/defaults/*.properties
	 * <br>---------------------------------------------------------
	 * <p/>
	 * @return
	 */
	private SystemConf initPropertiesPathMap() {
		String classpath = SystemUtil.getClasspath();
		this.propertiesPathMap = new HashMap<String, String>();
		this.propertiesPathMap.put("1", classpath + File.separator + "config" + File.separator + "properties");
		this.propertiesPathMap.put("2", classpath + File.separator + "config");
		this.propertiesPathMap.put("3", classpath);
		this.propertiesPathMap.put("4", classpath + File.separator + "config" + File.separator + "defaults");
		this.propertiesPathMap.put("5", classpath + File.separator + "defaults");
		
		return this;
	}
	
	/**
	 * 加载 project 配置项
	 *
	 * @return SystemConf
	 */
	private SystemConf loadProp() {
		// ----将系统参数 classpath 保存到 SystemConf 中。
		this.keyValueMap.put("class.path", SystemUtil.getClasspath());
		// ----需递归扫描子路径的路径ID集合
		Collection<Integer> needScanningSubpathIdx = Arrays.asList(1, 4);
		
		for(int i = this.propertiesPathMap.size(); i >= 1; i--) {
			File file = new File(propertiesPathMap.get(String.valueOf(i)));
			// ----目录不存在，则退出本次循环
			if(! file.exists()) continue;
			// ----获取该目录下子文件列表
			File[] subFileList = (needScanningSubpathIdx.contains(i)) 
					? FileUtil.getAllNonDirFileList(file)
					: FileUtil.getNonDirFileList(file);
			// ----遍历每个文件，加载各个配置项
			Properties prop = null;
			for (File f : subFileList) 
				if(f.getName().endsWith(".properties")) {
					try {
						prop = PropertiesUtil.load(f.getAbsolutePath());
						this.keyValueMap.putAll(PropertiesUtil.convertToMap(prop));
					} catch (IOException ex) {
						LOGGER.error("====SystemConf load file error [" + f.getAbsolutePath() + "]");
						ex.printStackTrace();
					}
				}
		}
		return this;
	}
	
	public String get(String key) {
		if (! this.containsKey(key)) return "";
		try{
			this.readLock.lock();
			return keyValueMap.get(key);
		} finally {
			this.readLock.unlock();
		}
	}
	
	
	public void set(String key, String val) {
		try {
			if (containsKey(key)) this.writeLock.lock();
			this.keyValueMap.put(key, val);
		} finally {
			if(this.writeLock.isHeldByCurrentThread()) 
				this.writeLock.unlock();
		}
	}

	/**
	 * 获取必须包含项配置----若配置信息为空--抛出异常
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getForcedIncludeConfiguration(String key) throws Exception{
		if (! this.containsKey(key)) throw new Exception("====not find [" + key + "] in configuration");
		String val = this.get(key);
		if (StringUtil.isBlank(val)) throw new Exception("====key [" + key + "] in configuration, but value is blank");
		return val;
	}

	/**
	 * 获取配置项，如果为空，返回传入的默认值
	 *
	 * @param key
	 * @param defaultVal
	 * @return String
	 */
	public String get(String key, String defaultVal) {
		if (! this.containsKey(key)) return defaultVal;
		String val = this.get(key);
		if (StringUtil.isBlank(val)) val = defaultVal;
		return val;
	}

	/**
	 * 从几个 key 中获取其中一个配置项并返回
	 *
	 * @param keys
	 * @return String
	 */
	public String get(String ... keys) {
		String value = "";
		for (String e : keys) {
			value = this.get(e);
			if (StringUtil.isNonBlank(value)) break;
		}
		return value;
	}
	
	public boolean containsKey(String key) {
		return this.keyValueMap.containsKey(key);
	}
	
	/**
	 * 列出所有资源属性
	 */
	public void list() {
		StringBuffer sb = new StringBuffer();
		try{
			this.readLock.lock();
			for (Map.Entry<String, String> entry : this.keyValueMap.entrySet()) 
				sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
			System.out.println(sb.toString());
		} finally {
			this.readLock.unlock();
		}
	}
	
	/**
	 * @Title: reload
	 * @Description: 重新加载
	 * @return SystemConf
	 * @throws
	 */
	public SystemConf reload() {
		try{
			this.writeLock.lock();
			this.clear().initArgs().loadProp();	// 初始化所有参数，重新读取配置文件
			return this;
		} finally {
			if (this.writeLock.isHeldByCurrentThread()) this.writeLock.unlock();
		}
	}
	
	public SystemConf clear() {
		try{
			this.writeLock.lock();
			this.keyValueMap.clear();
			return this;
		} finally {
			if (this.writeLock.isHeldByCurrentThread()) this.writeLock.unlock();
		}
	}
	
}





