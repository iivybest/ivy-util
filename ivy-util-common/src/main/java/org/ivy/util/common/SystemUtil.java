package org.ivy.util.common;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> TODO
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className SystemUtil
 * @date 2015/10/22 15:18
 */
public class SystemUtil {
    public static String classpath;
    public static final String UNIX_SEPARATOR = "/";

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
            case "\\":    // 若是 win 系统，去掉第一个字符
                classpath = root.substring(1);
                break;
            case "/":
                classpath = root;
                break;
            default:
                classpath = root;
        }

        // 将win 分隔符，装换为 linux分隔符
        if (fileSeparator.equals("\\")) {
            root = root.replace("\\", UNIX_SEPARATOR);
        }

        // root 路径补全
        if (!classpath.endsWith(UNIX_SEPARATOR)) {
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








