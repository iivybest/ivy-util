package org.ivy.util.cfg;


import org.ivy.util.common.PropertiesUtil;
import org.ivy.util.common.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p> classname: IvyUtilConf
 * <p> description: ivy-util 配置参数
 * <br>--------------------------------------------------------
 * <br> 1、配置
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2016/3/22 09:03
 */
public class IvyUtilConf {
    private static String classPath;
    private static String propertiesName;
    private static Map<String, String> propertiesUrlMap;
    private static Map<String, String> propertyMap;

    static {
        initArgs();
        initProp();
    }


    private IvyUtilConf() {
    }

    /**
     * <p> 初始化成员变量
     * <br> ivy-util 资源文件定义位置及优先级顺序
     * <br>	   1、classpath/config/ivy-util.properties
     * <br>    2、classpath/ivy-util.properties
     * <br>    3、. ivy-util.properties	// 当前路径下
     */
    private static void initArgs() {
        propertyMap = new HashMap<>();
        classPath = SystemUtil.getClasspath();
        propertiesName = "ivy-util.properties";
        propertiesUrlMap = new HashMap<>(8);
        propertiesUrlMap.put("1", classPath + File.separator + "config" + File.separator + propertiesName);
        propertiesUrlMap.put("2", classPath + File.separator + propertiesName);
        propertiesUrlMap.put("3", classPath + IvyUtilConf.class.getPackage().getName().replace(".", File.separator) + File.separator + propertiesName);
    }

    private static void initProp() {
        // 将系统classpath位置保存到SystemConf中。
        setProperty("classPath", classPath);
        File file;
        for (int i = propertiesUrlMap.size(); i >= 1; i--) {
            file = new File(propertiesUrlMap.get(String.valueOf(i)));
            if (file.exists()) {
                try {
                    Properties prop = PropertiesUtil.load(file.getAbsolutePath());
                    propertyMap.putAll(PropertiesUtil.convertToMap(prop));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static void setProperty(String key, String val) {
        propertyMap.put(key, val);
    }

    public static String getProperty(String key) {
        String ikey = IvyUtilConstant.PRIFIX + IvyUtilConstant.SEPARATOR + key;
        return (containsKey(key)) ? propertyMap.get(ikey) : "";
    }

    public static boolean containsKey(String key) {
        String ikey = IvyUtilConstant.PRIFIX + IvyUtilConstant.SEPARATOR + key;
        return propertyMap.containsKey(ikey);
    }


}
