package org.ivy.util.complex.configurator;

import org.ivy.util.common.FileUtil;
import org.ivy.util.common.SystemUtil;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> description: configurator util
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/4/12 10:58
 */
public class ConfiguratorUtil {

    public static String[] directories;

    static {
        String classpath = SystemUtil.getClasspath();
        String userDir = SystemUtil.getUserDir();
        String sourceDir = SystemUtil.getSourceDirectory();

//        directories = new String[]{
//                FileUtil.getUnixStyleFilePath(classpath + "/ofd/"),
//                FileUtil.getUnixStyleFilePath(userDir + "/ofd/"),
//                FileUtil.getUnixStyleFilePath(sourceDir + "/ofd/")
//        };
        directories = new String[]{
                classpath,
                userDir,
                sourceDir
        };
    }

    private ConfiguratorUtil() {
    }


    /**
     * get configurator support directory array
     *
     * @return directory array
     */
    public static <T> String[] getConfiguratorSupportedDirectory(Class<T> type) {
        String sourceDir = SystemUtil.getSourceDirectory(type);
        String[] dirs = directories.clone();
        dirs[2] = sourceDir;
        return dirs;
    }

    /**
     * get concrete configuration item
     *
     * @param dir      directory array
     * @param wildcard wildcard
     * @param key      configurator tag array
     * @return concrete configuration array
     */
    public static String[] concreteConfiguration(String[] dir, String wildcard, String... key) {
        String filename = String.format(wildcard, key);
        String[] dest = new String[dir.length];
        int idx = 0;
        for (String e : dir) {
            dest[idx++] = e + filename;
        }
        return dest;
    }

    /**
     * get concrete configuration item
     *
     * @param dir      directory array
     * @param wildcard wildcard array
     * @param key      configurator tag array
     * @return concrete configuration path array
     */
    public static String[] concreteConfiguration(String[] dir, String[] wildcard, String... key) {
        String[] dest = new String[dir.length * wildcard.length];
        String filename;
        int cursor = 0;
        for (String ele : wildcard) {
            filename = String.format(ele, key);
            for (String path : dir) {
                dest[cursor++] = FileUtil.getUnixStyleFilePath(path + "/" + filename);
            }
        }
        return dest;
    }


}
