package org.ivy.util.common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> 文件操作工具
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className FileUtil
 * @date 2014/6/5 09:01:55
 */
public class FileUtil {
    // 文件复制中间临时文件后缀
    protected static final String TEMP_SUFFIX;
    // Unix file Separator
    protected static final String UNIX_SEPARATOR;
    // 读写缓冲大小
    protected static final int BUF_SIZE;

    static {
        TEMP_SUFFIX = ".temp";
        UNIX_SEPARATOR = "/";
        BUF_SIZE = 1024 * 4;
    }

    /**
     * 获取 UNIX 风格的文件路径
     *
     * @param file
     * @return String
     */
    public static String getUnixStyleFilePath(File file) {
        if (null == file) return null;

        String path;
        if (FileUtil.UNIX_SEPARATOR.equals(File.separator)) path = file.getAbsolutePath();
        else path = file.getAbsolutePath().replace(File.separator, FileUtil.UNIX_SEPARATOR);

        // if file is Dir, make sure file's path end with unix separator
        if (file.isDirectory() && !path.endsWith(FileUtil.UNIX_SEPARATOR)) path += FileUtil.UNIX_SEPARATOR;

        return path;
    }

    /**
     * @param file
     * @return String
     * @Title getUnixStyleFilePath
     * @Description 获取Unix风格的文件路径
     */
    public static String getUnixStyleFilePath(String file) {
        if (null == file || file.length() <= 0) return null;

        // 替换windows文件分隔符
        String path = file.replaceAll("\\\\+", "/");
        path = path.replaceAll(FileUtil.UNIX_SEPARATOR + "{2,}", FileUtil.UNIX_SEPARATOR);

        return path;
    }

    /**
     * @return String
     * @Title getFilenameWithoutFileType
     * @Description 获取不含有文件类型的文件名
     */
    public static String getFilenameWithoutFileType(File file) {
        if (file == null) return null;
        int idx = file.getName().lastIndexOf(".");
        if (idx == -1) return file.getName();
        return file.getName().substring(0, idx);
    }

    /**
     * @return String
     * @Title getFilenameWithoutFileType
     * @Description 获取不含有文件类型的文件名
     */
    public static String getFilenameWithoutFileType(String file) {
        if (file == null || file.length() == 0) return null;
        return getFilenameWithoutFileType(new File(file));
    }

    /**
     * @param file
     * @return String
     * @Title getFileType
     * @Description 获取文件类型
     */
    public static String getFileType(File file) {
        if (null == file) return null;

        String filename = file.getName();
        if (!filename.contains(".")) return "";

        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    /**
     * @param file   拷问文件
     * @param target 目标文件
     * @return void
     * @throws IOException
     * @throws FileNotFoundException ***************************************************
     *                               思路：
     *                               1、文件不存在，退出
     *                               2、目标文键为null，退出
     *                               3、目标路径下是否有同名文件处理，
     *                               3.1 若有同名文件，使用数字累加处理
     *                               3.2 考虑目标文件名没有类型的情况处理
     *                               4、采用缓冲、以字节流来读写文件
     *                               5、复制过程中采用临时文件
     *                               6、复制完成，去掉临时文件后缀
     *                               ***************************************************
     * @Title copyNonDirFile
     * @Description 非目录文件拷贝
     */
    private static void copyNonDirFile(File file, File target) throws IOException {
        // 文件不存在，直接退出
//		if (null == file || ! file.exists()) return;
        // 目标路径不存在，直接退出
//		if (null == target) return;

        // 目标路径下存在同名文件处理, 需要考虑目标文件名中没有文件类型的情况
        target = makeSureTargetFile4Copy(target);

        // 文件拷贝 - 文件复制过程中，使用临时文件
        File tempFile = new File(target.getAbsolutePath() + TEMP_SUFFIX);
        int successFlag = 0;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             FileOutputStream fos = new FileOutputStream(tempFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            int len;                            // 每次读取字节长度
            byte[] buf = new byte[BUF_SIZE];    // 缓冲区
            while ((len = bis.read(buf)) > 0) bos.write(buf, 0, len);
            bos.flush();                        // 释放缓存
            successFlag++;        // 文件复制成功后，标识自增1
        } finally {
            // 复制失败，删除临时文件
            if (successFlag == 0) tempFile.delete();
            // 文件改名只用关闭流后操作才行
            if (tempFile != null && target != null) tempFile.renameTo(target);
        }

    }

    /**
     * @param dest
     * @return File
     * @Title makeSureTargetFile4Copy
     * @Description ----------------------------
     * 确保文件复制时，目标文件可操作性；
     * 1、目录，确保其存在
     * 2、文件，确保目标路径是否含有同名文件
     * 2.1 检查目标文件是否存在存在同名文件
     * 2.2 若存在，文件名后缀 + "自增阿拉伯数字"
     * 2.2.1 重复 2.1操作
     * 2.3 若不存在，返回该文件
     */
    private static File makeSureTargetFile4Copy(File dest) {
        if (dest == null) return null;
        if (dest.isDirectory()) return dest;

        int idx = 0;
        String prefix = dest.getParent() + FileUtil.UNIX_SEPARATOR + getFilenameWithoutFileType(dest);
        String suffix = getFileType(dest).length() > 0 ? ("." + getFileType(dest)) : "";
        for (; dest.exists(); dest = new File(prefix + "-(" + idx++ + ")" + suffix)) ;
        return dest;
    }

    /**
     * @param file
     * @param dest 目标文件目录
     * @return void
     * *****************************************
     * 1、文件不存在，退出；目标路径为空，退出
     * 2、检查目标路径是否存在，不存在新建
     * 3、判断要复制文件是目录还是文件
     * 4、若为文件直接复制到目标路径, 先判断是否目标路径是否存在同名文件
     * 5、若为目录，则目标路径下新建该目录，此时目标路径改为新建目录
     * 并列举目录下所以子文件，对每个文件重复4、5操作(递归实现)
     * 6、文件处理过程中，正在进行复制的文件使用TEMP_SUFFIX标识，
     * 7、复制完成后，去掉后缀
     * ***************************************************
     * @throws Exception
     * @Title copy
     * @Description 文件复制
     */
    public static void copy(File file, String dest) throws Exception {
        // 拷贝文件夹不存在，直接退出。
        if (null == file || !file.exists()) return;
        // 目标路径为空，退出
        if (null == dest || dest.length() <= 0) return;
        // 检查目标路径不通过，退出
        if (!checkDir(dest, true)) return;

        if (file.isDirectory()) {
            if (dest.contains(file.getAbsolutePath())) throw new Exception("不能将父目录拷贝到子目录");

            dest = getUnixStyleFilePath(dest) + FileUtil.UNIX_SEPARATOR + file.getName();
            // 确认目标路径存在，采用递归调用，进行子文件进行复制
            if (checkDir(dest, true)) for (File f : file.listFiles()) copy(f, dest);
        } else if (file.isFile()) {
            // 目标文件不包含复制文件名的，进行名称补全
            if (!dest.endsWith(file.getName()))
                dest = getUnixStyleFilePath(dest + FileUtil.UNIX_SEPARATOR + file.getName());
            copyNonDirFile(file, new File(dest));
        }
    }


    /**
     * <p>文件复制</p>
     *
     * @param fileUrl
     * @param dest
     * @throws Exception
     */
    public static void copy(String fileUrl, String dest) throws Exception {
        if (null == fileUrl || fileUrl.length() <= 0) return;
        copy(new File(fileUrl), dest);
    }

    /**
     * @param file
     * @return void
     * @Title delete
     * @Description 文件删除
     */
    public static void delete(File file) {
        if (null == file || !file.exists()) return;
        // 如果文件是目录，遍历所有子文件，递归删除
        if (file.isDirectory()) for (File f : file.listFiles()) delete(f);
        file.delete();
    }

    /**
     * <p>文件删除</p>
     *
     * @param fileUrl
     */
    public static void delete(String fileUrl) {
        if (null == fileUrl || fileUrl.length() <= 0) return;
        delete(new File(fileUrl));
    }

    /**
     * <p>文件剪切</p>
     *
     * @param file
     * @param dest
     * @throws IOException
     * @description 将文件或目录剪切到目标路径下
     */
    public static void cut(File file, String dest) throws IOException {
        if (null == file || !file.exists()) return;
        if (null == dest || dest.length() <= 0) return;

        // 格式化目标路径 , 为路径结尾加上分隔符
        dest = getUnixStyleFilePath(dest + File.separator);

        boolean flag = checkDir(dest, true);
        if (!flag) throw new IOException("dest目标路径创建失败");

        if (file.isDirectory()) {
            String _src = getUnixStyleFilePath(file.getAbsolutePath() + File.separator);
            // 目标路径与操作路径相同，不做处理，直接退出
            if (_src.equals(dest)) return;
            // 目标路径是操作路径的子路径，抛出异常
            if (dest.startsWith(_src)) throw new IOException("不能将父目录剪切到子目录");
        } else {
            // 目标路径为原文件所在路径，不做处理，直接退出
            if (getUnixStyleFilePath(file.getParentFile().getAbsoluteFile() + File.separator).equals(dest)) return;
        }

        if (file.isDirectory()) {
            dest += file.getName();
            checkDir(dest, true);
            for (File f : file.listFiles()) cut(f, dest);
            file.delete();
        } else {
            File target = new File(dest + file.getName());
            file.renameTo(target);
        }
    }

    /**
     * <p>文件剪切</p>
     *
     * @param fileUrl
     * @param dest
     * @throws IOException
     */
    public static void cut(String fileUrl, String dest) throws IOException {
        if (null == fileUrl || fileUrl.length() <= 0) return;
        cut(new File(fileUrl), dest);
    }

    /**
     * 创建新文件
     *
     * @param file
     * @return
     * @author miao.xl
     * @date 2014年12月19日 下午5:52:10
     * @version 1.0
     */
    public static boolean createNewFile(File file) {
        if (null == file) return false;
        if (file.exists()) return file.isFile();

        boolean flag = false;
        if (checkDir(file.getParent(), true))
            try {
                flag = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return flag;
    }

    /**
     * 创建新文件
     *
     * @param fileUrl
     * @return
     * @author miao.xl
     * @date 2014年12月19日 下午5:52:29
     * @version 1.0
     */
    public static boolean createNewFile(String fileUrl) {
        if (null == fileUrl || fileUrl.length() <= 0) return false;
        return createNewFile(new File(fileUrl));
    }

    /**
     * 以字节流读文件
     *
     * @param in
     * @return byte[]
     */
    public static byte[] read(InputStream in) {
        if (null == in) return null;
        byte[] original = null;
        try (BufferedInputStream bis = new BufferedInputStream(in);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf)) > 0) baos.write(buf, 0, len);
            original = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return original;
    }


    /**
     * <p>以字节流读文件</p>
     *
     * @return
     */
    public static byte[] read(File file) {
        if ((null == file || !file.exists()) || file.isDirectory()) return null;

        byte[] original = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            original = read(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return original;
    }

    /**
     * @param fileUrl
     * @return byte[]
     * @Title read
     * @Description 以字节形式读文件
     */
    public static byte[] read(String fileUrl) {
        if (null == fileUrl || fileUrl.length() <= 0) return null;
        return read(new File(fileUrl));
    }

    /**
     * 以字节形式读取文件
     *
     * @param fileInputStream
     * @param encoding
     * @return String
     */
    public static String reader(FileInputStream fileInputStream, String encoding) {
        // 文件不存在，或者文件为目录，返回null
        if (null == fileInputStream) return null;

        StringBuffer sb = new StringBuffer();
        try (
                BufferedReader br = new BufferedReader(
                        null == encoding ? new InputStreamReader(fileInputStream) : new InputStreamReader(fileInputStream, encoding),
                        1 * 1024 * 1024);
        ) {
            char[] cbuf = new char[1024 * 2];
            int len;
            while ((len = br.read(cbuf)) > 0) sb.append(cbuf, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 以字节形式读取文件
     *
     * @param file
     * @param encoding
     * @return String
     */
    public static String reader(File file, String encoding) {
        // 文件不存在，或者文件为目录，返回null
        if ((null == file || (!file.exists())) || file.isDirectory()) return null;
        String result = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            result = reader(fis, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 以字节形式读取文件
     *
     * @param fileUrl
     * @param encoding
     * @return String
     */
    public static String reader(String fileUrl, String encoding) {
        return reader(new File(fileUrl), encoding);
    }

    /**
     * 以字节形式读取文件
     *
     * @param fileInputStream
     * @return String
     */
    public static String reader(FileInputStream fileInputStream) {
        return reader(fileInputStream, null);
    }

    /**
     * 以字节形式读取文件
     *
     * @param file
     * @return String
     */
    public static String reader(File file) {
        return reader(file, null);
    }

    public static String reader(String fileUrl) {
        return reader(fileUrl, null);
    }

    /**
     * @param file
     * @param data
     * @param isAppend
     * @return void
     * @Title write
     * @Description 以字节形式写入文件
     */
    public static void write(File file, byte[] data, boolean isAppend) {
        if (null == file || (null == data || data.length <= 0)) return;

        // make sure file exists
        if (!file.exists()) createNewFile(file);

        try (FileOutputStream fos = new FileOutputStream(file, isAppend);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             //使用 bytes 构造成 ByteArrayInputStream，设置缓冲区读写，推荐
             ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            byte[] buf = new byte[BUF_SIZE];
            int len = 0;
            while ((len = bais.read(buf)) >= 0) bos.write(buf, 0, len);

            /* 一次性全部写出，第二推荐 */
//			bos.write(data);

            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileUrl
     * @param data
     * @param isAppend
     * @return void
     * @Title write
     * @Description 以字节形式写入文件
     */
    public static void write(String fileUrl, byte[] data, boolean isAppend) {
        File file = new File(fileUrl);
        write(file, data, isAppend);
    }

    /**
     * @param file
     * @param data
     * @return void
     * @Title write
     * @Description 以字节流形式写文件
     */
    public static void write(File file, byte[] data) {
        write(file, data, false);
    }

    /**
     * @param fileUrl
     * @param data
     * @return void
     * @Title write
     * @Description 以字节流形式写文件
     */
    public static void write(String fileUrl, byte[] data) {
        File file = new File(fileUrl);
        write(file, data);
    }


    /**
     * <p>文件写入 </p>
     *
     * @param file     写入文件
     * @param data     写入内容
     * @param isAppend 是否在原文件上追加写入
     * @author miao.xl
     * @date 2015年1月13日 下午12:13:09
     * @version 1.0
     */
    public static void writer(File file, String data, boolean isAppend) {
        if (null == file || null == data) return;
        if (!file.exists()) createNewFile(file);
        try (    // new FileWriter(file, true)  true 实现文件追加写入
                 BufferedWriter bw = new BufferedWriter(new FileWriter(file, isAppend))) {
            bw.write(data);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>文件写入 </p>
     *
     * @param fileUrl  文件路径
     * @param data     写入内容
     * @param isAppend 是否在原文件上追加写入
     * @author miao.xl
     * @date 2015年1月13日 下午12:15:18
     * @version 1.0
     */
    public static void writer(String fileUrl, String data, boolean isAppend) {
        File file = new File(fileUrl);
        writer(file, data, isAppend);
    }

    /**
     * <p>文件写入</p>
     */
    public static void writer(File file, String original) {
        writer(file, original, false);
    }

    /**
     * <p>文件写入</p>
     */
    public static void writer(String fileUrl, String original) {
        File file = new File(fileUrl);
        writer(file, original);
    }

    /**
     * 文件尾部追加内容 - 文件若不存在，新建文件
     *
     * @param file
     * @param data
     * @author miao.xl
     * @date 2014年12月19日 下午5:47:43
     * @version 1.0
     */
    public static void append(File file, String data) {
        if (null == data || data.length() <= 0) return;
        if (null == file) return;

        if (!file.exists()) createNewFile(file);
        writer(file, data, true);
    }

    /**
     * 文件尾部追加内容
     *
     * @param fileUrl
     * @param data
     * @author miao.xl
     * @date 2014年12月19日 下午5:49:23
     * @version 1.0
     */
    public static void append(String fileUrl, String data) {
        if (null == fileUrl || fileUrl.length() <= 0) return;
        if (null == data || data.length() <= 0) return;
        append(new File(fileUrl), data);
    }

    /**
     * 检查dir
     */
    public static boolean checkDir(File file) {
        return null != file && file.exists() && file.isDirectory();
    }

    /**
     * 检查dir
     */
    public static boolean checkDir(String fileUrl) {
        if (null == fileUrl || fileUrl.length() <= 0) return false;
        return checkDir(new File(fileUrl));
    }

    /**
     * 检查dir，并决定是否新建
     *
     * @param file
     * @param isNew
     * @return
     * @author miao.xl
     * @date 2014年7月8日 下午3:05:14
     * @version 1.0
     */
    public static boolean checkDir(File file, boolean isNew) {
        if (!checkDir(file) && isNew) file.mkdirs();
        return file.exists();
    }

    /**
     * 检查dir，并决定是否新建
     *
     * @param fileUrl
     * @param isNew
     * @return
     * @author miao.xl
     * @date 2014年7月8日 下午3:05:14
     * @version 1.0
     */
    public static boolean checkDir(String fileUrl, boolean isNew) {
        if (null == fileUrl || fileUrl.length() <= 0) return false;
        return checkDir(new File(fileUrl), isNew);
    }

    /**
     * @param isNew
     * @param fileUrl
     * @return boolean
     * @Title: checkDir
     * @Description: 检查任意个目录是否存在，不存在根据isNew进行新建
     */
    public static boolean checkDir(boolean isNew, String... fileUrl) {
        if (null == fileUrl) return false;
        boolean flag = true;
        for (String path : fileUrl) {
            flag = checkDir(path, isNew);
            if (!isNew && !flag) break;
        }
        return flag;
    }


    /**
     * 获取目录下文件列表
     *
     * @param dir
     * @return
     * @author miao.xl
     * @date 2015年1月15日 上午10:24:26
     * @version 1.0
     */
    public static File[] getFileList(File dir) {
        if (null == dir || !dir.exists() || !dir.isDirectory()) return null;
        return dir.listFiles();
    }

    /**
     * 获取目录下文件列表
     *
     * @param dirUrl
     * @return
     * @author miao.xl
     * @date 2015年1月15日 上午10:24:26
     * @version 1.0
     */
    public static File[] getFileList(String dirUrl) {
        if (null == dirUrl || dirUrl.length() <= 0) return null;
        return getFileList(new File(dirUrl));
    }


    /**
     * @param dir
     * @return File[]
     * @Title getDirFileList
     * @Description 获取路径下所有文件夹
     */
    public static File[] getDirFileList(File dir) {
        if (null == dir || !dir.exists()) return null;

        List<File> list = new ArrayList<File>();
        for (File f : getFileList(dir)) if (f.isDirectory()) list.add(f);

        return list.toArray(new File[list.size()]);
    }

    /**
     * @param dirUrl
     * @return File[]
     * @Title getDirFileList
     * @Description 获取路径下所有文件夹
     */
    public static File[] getDirFileList(String dirUrl) {
        if (dirUrl == null || dirUrl.length() <= 0) return null;
        return getDirFileList(new File(dirUrl));
    }


    /**
     * 获取目录下非目录文件列表
     *
     * @param dir
     * @return
     * @author miao.xl
     * @date 2015年1月15日 上午10:35:19
     * @version 1.0
     */
    public static File[] getNonDirFileList(File dir) {
        if (null == dir || !dir.exists()) return null;

        List<File> list = new ArrayList<File>();
        File[] files = getFileList(dir);
        for (int i = 0; i < files.length; i++) if (files[i].isFile()) list.add(files[i]);

        return list.toArray(new File[list.size()]);
    }

    /**
     * 获取目录下非目录文件列表
     *
     * @param dir
     * @return File[]
     */
    public static File[] getNonDirFileList(String dir) {
        if (null == dir || dir.length() <= 0) return null;
        return getNonDirFileList(new File(dir));
    }

    /**
     * 获取目录下非目录文件列表-包括子目录文件
     *
     * @param dir
     * @return File[]
     */
    public static File[] getAllNonDirFileList(File dir) {
        if (null == dir || (!dir.exists())) return null;
        if (dir.isFile()) return new File[0];

        List<File> list = new ArrayList<File>();
        for (File f : getFileList(dir)) {
            if (f.isFile()) list.add(f);
//			else list.addAll(Arrays.asList(getAllNonDirFileList(f)));
            else for (File sf : getAllNonDirFileList(f)) list.add(sf);
        }
        return list.toArray(new File[list.size()]);
    }

    /**
     * 获取目录下非目录文件列表-包括子目录文件
     *
     * @param dirUrl
     * @return
     * @author miao.xl
     * @date 2015年1月15日 上午10:35:19
     * @version 1.0
     */
    public static File[] getAllNonDirFileList(String dirUrl) {
        if (null == dirUrl || dirUrl.length() <= 0) return null;
        return getAllNonDirFileList(new File(dirUrl));
    }

}





