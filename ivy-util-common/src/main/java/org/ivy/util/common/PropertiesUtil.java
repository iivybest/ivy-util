package org.ivy.util.common;

import java.io.*;
import java.util.*;

/**
 * <p> description: properties 工具类
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
     * @param fileUrl file path
     * @return Properties
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static Properties load(String fileUrl) throws FileNotFoundException, IOException {
        if (StringUtil.isBlank(fileUrl)) {
            return null;
        }
        Properties prop = null;
        try (InputStream is = new BufferedInputStream(new FileInputStream(fileUrl))) {
            // Class.class.getResourceAsStream，只支持classpath的相对路径
            prop = (fileUrl.matches(IvyConstant.REGEXP_XML)) ? loadFromXML(is) : loadProperties(is);
        }
        return prop;
    }

    /**
     * load properties from xml file
     *
     * @param is InputStream
     * @return Properties
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    public static Properties loadFromXML(InputStream is) throws InvalidPropertiesFormatException, IOException {
        Properties prop = new Properties();
        prop.loadFromXML(is);
        return prop;
    }

    /**
     * load properties from properties file
     *
     * @param is InputStream
     * @return Properties
     * @throws IOException
     */
    public static Properties loadProperties(InputStream is) throws IOException {
        Properties prop = new Properties();
        prop.load(is);
        return prop;
    }

    /**
     * store Properties to file system
     *
     * @param prop    Properties
     * @param fileUrl file path
     * @param comment comment
     */
    public static void storeProperties(Properties prop, String fileUrl, String comment) {
        if (!new File(fileUrl).exists()) {
            FileUtil.createNewFile(fileUrl);
        }
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileUrl))) {
            if (fileUrl.matches(IvyConstant.REGEXP_XML)) {
                storePropertiesToXML(prop, os, comment);
            } else {
                storeProperties(prop, os, comment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * store properties file
     *
     * @param prop    Properties
     * @param os      OutputStream
     * @param comment comment
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
     * @param prop    Properties
     * @param os      OutputStream
     * @param comment comment
     */
    public static void storePropertiesToXML(Properties prop, OutputStream os, String comment) {
        try {
            prop.storeToXML(os, comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * convert properties to map
     *
     * @param prop Properties
     * @return Map
     */
    public static Map<String, String> convertToMap(Properties prop) {
        if (prop == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<?> keys = prop.propertyNames();
        while (keys.hasMoreElements()) {
            String key = String.valueOf(keys.nextElement());
            map.put(key, prop.getProperty(key));
        }
        return map;
    }

}
