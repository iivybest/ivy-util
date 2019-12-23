package org.ivy.util.common;

import static org.ivy.util.common.IvyConstant.*;

/**
 * <p>  classname：SystemUtil
 * <br> description：系统工具类
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2015/10/22 15:18
 */
public class SystemUtil {
    private static String classpath;

    static {
        initClasspath();
    }

    /**
     * 初始化classpath
     */
    public static void initClasspath() {
        String fileSeparator = System.getProperty("file.separator");

//		String root = Class.class.getClass().getResource("/").getPath().replace("%20", " ");
        /* 打成 ruuable jar，执行，返回为空字符串 */
        String root = Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ");

        switch (fileSeparator) {
            // 若是 win 系统，去掉第一个字符
            case WIN_SEPARATOR:
                classpath = root.substring(1);
                break;
            case UNIX_SEPARATOR:
                classpath = root;
                break;
            default:
                classpath = root;
        }

        // 将 win 分隔符，装换为 linux分隔符
        if (fileSeparator.equals(WIN_SEPARATOR)) {
            root = root.replace(WIN_SEPARATOR, UNIX_SEPARATOR);
        }
        // root 路径补全
        if (! classpath.endsWith(UNIX_SEPARATOR)) {
            classpath += UNIX_SEPARATOR;
        }
    }


    public static String getClasspath() {
        return classpath;
    }

    public static String getOsname() {
        String osname = System.getProperty("os.name").toUpperCase();
        String os = "";
        if (osname.contains("LINUX")) {
            os = "LINUX";
        } else if (osname.contains("WINDOWS")) {
            os = "WINDOWS";
        }
        return os;
    }

}








