/**
 * 
 */
package org.ivy.xutil.scan;

import java.util.Map;

/**
 * <p>PropertiesScanner</p>
 * <p>Description : </p>
 *
 * @author miao.xl
 * @date 2015年7月1日 - 上午9:29:39
 * @version 1.0
 */
public interface PropertiesScanner {
	
	/**
	 * 扫描全部文件
	 * @return
	 * 
	 * @author miao.xl
	 * @date 2015年7月1日 上午9:40:59
	 * @version 1.0
	 */
	public Map<String, String> scan();
	
	/**
	 * Incrementally update,增量更新
	 * @return
	 * 
	 * @author miao.xl
	 * @date 2015年7月1日 上午9:41:26
	 * @version 1.0
	 */
	public Map<String, String> update();
	
}
