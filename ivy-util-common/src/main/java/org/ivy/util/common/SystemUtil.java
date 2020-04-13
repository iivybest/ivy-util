package org.ivy.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.ivy.util.common.IvyConstant.UNIX_SEPARATOR;
import static org.ivy.util.common.IvyConstant.WIN_SEPARATOR;


/**
 * <p>
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
    private static Logger log = LoggerFactory.getLogger(SystemUtil.class);

    private static String classpath;
    private static String userDir;

    static {
        initClasspath();
        initUserDir();
    }

    /**
     * 初始化classpath
     */
    private static void initClasspath() {
        String fileSeparator = System.getProperty("file.separator");

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
        if (!classpath.endsWith(UNIX_SEPARATOR)) {
            classpath += UNIX_SEPARATOR;
        }
    }

    private static void initUserDir() {
        userDir = FileUtil.getUnixStyleFilePath(System.getProperty("user.dir") + IvyConstant.UNIX_SEPARATOR);
        if ("linux".equals(getOsName())) {
            if (!userDir.startsWith("/")) {
                userDir = "/" + userDir;
            }
        }
    }

    /**
     * 获取当前项目项目 classpath
     *
     * @return classpath string
     */
    public static String getClasspath() {
        return classpath;
    }

    /**
     * 获取操作系统架构 64 / 32
     *
     * @return os arch
     */
    public static String getOSArch() {
        String arch = System.getProperty("os.arch").contains("64")
                ? "64"
                : "32";
        return arch;
    }

    /**
     * 获取系统名称
     *
     * @return os name
     */
    public static String getOsName() {
        String name = System.getProperty("os.name").toUpperCase();
        String os = "";

        if (name.contains("LINUX")) {
            os = "linux";
        } else if (name.contains("WINDOWS")) {
            os = "windows";
        }
        return os;
    }

    /**
     * get the directory of the source code
     *
     * @return directory of the source code
     */
    public static String getSource() {
        String path = FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        log.info("====source: {}", path);
        try {
            path = URLDecoder.decode(path, "UTF-8");
            log.info("====source: {}", path);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return FileUtil.getUnixStyleFilePath(path);
    }

    /**
     * get the directory of the source code
     *
     * @return directory of the source code
     */
    public static String getSourceDirectory() {
        return FileUtil.getUnixStyleFilePath(new File(SystemUtil.getSource()).getParentFile());
    }

    /**
     * 获取当前项目路径
     *
     * @return user dir string
     */
    public static String getUserDir() {
        return userDir;
    }

}








