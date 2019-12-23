package org.ivy.util.common;

import java.io.*;
import java.util.*;

/**
 * <p>  classname: PropertiesUtil
 * <br> description: properties 工具类
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/12/3 17:47
 */
public class PropertiesUtil {
    /**
     * load properties
     *
     * @param fileUrl
     * @return Properties
     * @throws IOException
     * @throws FileNotFoundException
     * @author miao.xl
     * @date 2015年1月15日 上午9:05:35
     * @version 1.0
     */
    public static Properties load(String fileUrl) throws FileNotFoundException, IOException {
        if (StringUtil.isBlank(fileUrl)) return null;
        Properties prop = null;
        try (InputStream is = new BufferedInputStream(new FileInputStream(fileUrl))) {
            // Class.class.getResourceAsStream，只支持classpath的相对路径
            if (fileUrl.matches(IvyConstant.REGEXP_XML)) prop = loadFromXML(is);
            else prop = loadFromProperties(is);
        }
        return prop;
    }

    /**
     * load properties from xml file
     *
     * @param is
     * @return
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     * @author miao.xl
     * @date 2014年12月9日 下午5:09:19
     * @version 1.0
     */
    public static Properties loadFromXML(InputStream is) throws InvalidPropertiesFormatException, IOException {
        Properties prop = new Properties();
        prop.loadFromXML(is);
        return prop;
    }

    /**
     * load properties from properties file
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static Properties loadFromProperties(InputStream is) throws IOException {
        Properties prop = new Properties();
        prop.load(is);
        return prop;
    }

    /**
     * store properties
     *
     * @param prop
     * @param fileUrl
     * @param comment
     * @author miao.xl
     * @date 2014年12月9日 下午4:20:18
     * @version 1.0
     */
    public static void storeProperties(Properties prop, String fileUrl, String comment) {
        // 文件不存在，创建文件
        if (!new File(fileUrl).exists()) FileUtil.createNewFile(fileUrl);

        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileUrl))) {
            if (fileUrl.matches(IvyConstant.REGEXP_XML))
                storeProperties2XML(prop, os, comment);
            else
                storeProperties(prop, os, comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store properties file
     *
     * @param prop
     * @param os
     * @param comment
     * @author miao.xl
     * @date 2014年12月9日 下午4:24:24
     * @version 1.0
     */
    public static void storeProperties(Properties prop, OutputStream os, String comment) {
        try {
            prop.store(os, comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store properties to xml file
     *
     * @param prop
     * @param os
     * @param comment
     * @author miao.xl
     * @date 2014年12月9日 下午4:26:18
     * @version 1.0
     */
    public static void storeProperties2XML(Properties prop, OutputStream os, String comment) {
        try {
            prop.storeToXML(os, comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert properties to map
     *
     * @param prop
     * @return map
     * @author miao.xl
     * @date 2015年1月15日 上午9:42:46
     * @version 1.0
     */
    public static Map<String, String> convertToMap(Properties prop) {
        if (prop == null) return null;

        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> keys = (Enumeration<?>) prop.propertyNames();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            map.put(key, prop.getProperty(key));
        }
        return map;
    }

}
