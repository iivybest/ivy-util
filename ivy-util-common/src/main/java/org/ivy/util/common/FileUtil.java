package org.ivy.util.common;

import org.ivy.util.common.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> file operation util class
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/6/5 09:01:55
 */
public class FileUtil {

    /**
     * 文件复制时，重复文件处理方式
     */
    public static final int OPT_COPY_ABANDON = 0;
    public static final int OPT_COPY_COVER = 1;
    public static final int OPT_COPY_BOTH = 2;


    /**
     * 文件复制中间临时文件后缀
     */
    protected static final String TEMP_SUFFIX;
    /**
     * Unix file Separator
     */
    protected static final String UNIX_SEPARATOR;
    /**
     * Win file Separator
     */
    protected static final String WIN_SEPARATOR;
    /**
     * the buffer for read or write
     */
    protected static final int BUF_SIZE;

    static {
        TEMP_SUFFIX = ".temp";
        UNIX_SEPARATOR = "/";
        WIN_SEPARATOR = "\\";
        BUF_SIZE = 1024 * 4;
    }


    private FileUtil() {

    }


    /**
     * get file path in unix style
     *
     * @param file file
     * @return String
     */
    public static String getUnixStyleFilePath(File file) {
        if (null == file) {
            return null;
        }
        String path = FileUtil.UNIX_SEPARATOR.equals(File.separator)
                ? file.getAbsolutePath()
                : file.getAbsolutePath().replace(File.separator, FileUtil.UNIX_SEPARATOR);

        // if file is Dir, make sure file's path end with unix separator
        if (file.isDirectory() && !path.endsWith(FileUtil.UNIX_SEPARATOR)) {
            path += FileUtil.UNIX_SEPARATOR;
        }
        return path;
    }

    /**
     * get file path in unix style
     *
     * @param file file
     * @return String
     */
    public static String getUnixStyleFilePath(String file) {
        if (null == file || file.trim().length() == 0) {
            return null;
        }
        // Replace Windows file separator
        String path = file.replaceAll("\\\\+", UNIX_SEPARATOR);
        // remove repetitive file separator
        path = path.replaceAll(FileUtil.UNIX_SEPARATOR + "{2,}", FileUtil.UNIX_SEPARATOR);
        if (path.startsWith(FileUtil.UNIX_SEPARATOR)) {
            path = path.substring(1, path.length());
        }
        return path;
    }

    /**
     * <p>
     * <br>--------------------------------------------
     * <br> description: 确保文件复制时，目标文件可操作性
     * <br> 1、目录，确保其存在
     * <br> 2、文件，确保目标路径是否含有同名文件
     * <br>     2.1 检查目标文件是否存在存在同名文件
     * <br>     2.2 若存在，文件名后缀 + "自增阿拉伯数字"
     * <br>         2.2.1 重复 2.1操作
     * <br> 2.3 若不存在，返回该文件
     * <br>--------------------------------------------
     * </p>
     *
     * @param dest destination
     * @return File
     */
    private static File getFinalDestByOptForCopy(int opt, File dest) throws IOException {
        if (dest.isDirectory()) {
            return dest;
        }
        // ----覆盖模式进行文件复制
        if (OPT_COPY_COVER == opt) {
            if (dest.exists()) {
                dest.delete();
            }
            return dest;
        }
        // ----放弃模式进行文件复制
        if (OPT_COPY_ABANDON == opt) {
            return dest.exists() ? null : dest;
        }
        // ----保留两个文件模式进行复制，目标文件格式 schema: filename-(count).type
        if (OPT_COPY_BOTH == opt) {
            int idx = 0;
            String prefix = dest.getParent() + FileUtil.UNIX_SEPARATOR + getFilenameWithoutFileType(dest);
            String suffix = (getFileType(dest).length() > 0)
                    ? ("." + getFileType(dest))
                    : "";
            for (; dest.exists(); dest = new File(prefix + "-(" + idx++ + ")" + suffix)) {
            }
            return dest;
        }
        throw new IOException("==== opt[" + opt + "] is illegal, valid opt set[0, 1, 2]");
    }

    /**
     * get file name without file type
     *
     * @param file file
     * @return String
     */
    public static String getFilenameWithoutFileType(File file) {
        if (file == null) {
            return null;
        }
        int idx = file.getName().lastIndexOf(".");
        if (idx == -1) {
            return file.getName();
        }
        return file.getName().substring(0, idx);
    }

    /**
     * get file name without file type
     *
     * @param file file
     * @return String
     */
    public static String getFilenameWithoutFileType(String file) {
        if (file == null || file.length() == 0) {
            return null;
        }
        return getFilenameWithoutFileType(new File(file));
    }

    /**
     * get file type
     *
     * @param file file
     * @return String
     */
    public static String getFileType(File file) {
        if (null == file) {
            return null;
        }

        String filename = file.getName();
        if (!filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf(".") + 1, filename.length());
    }

    /**
     * file copy
     * <br> ----------------------------------------
     * <br> 1、文件不存在，退出
     * <br> 2、目标文键为null，退出
     * <br> 3、目标路径下是否有同名文件处理，
     * <br>     3.1 若有同名文件，使用数字累加处理
     * <br>     3.2 考虑目标文件名没有类型的情况处理
     * <br> 4、采用缓冲、以字节流来读写文件
     * <br> 5、复制过程中采用临时文件
     * <br> 6、复制完成，去掉临时文件后缀
     * <br> ----------------------------------------
     *
     * @param opt  operation type
     * @param in   file
     * @param dest destination
     * @throws IOException
     */
    private static void copyNonDirFile(int opt, InputStream in, File dest) throws IOException {
        // 输入流不存在，直接退出
        if (null == in) {
            return;
        }
        // 目标路径不存在，直接退出
        if (null == dest) {
            throw new IOException("====arg dest can not be null");
        }
        // 源文件为输入流，dest 须为文件，不能是目录
        if (dest.isDirectory()) {
            throw new IOException("====arg dest can not be a directory");
        }
        // 检查 dest 所在文件夹，若不存在穿件该目录
        checkDir(dest.getParentFile(), true);
        // ----目标路径下存在同名文件处理, 需要考虑目标文件名中没有文件类型的情况
        dest = getFinalDestByOptForCopy(opt, dest);
        if (dest == null) {
            // ----dest == null, 说明采取了放弃模式进行文件复制
            return;
        }
        // ----临时文件----文件拷贝过程中，使用临时文件，拷贝完成，将临时文件改名为目标文件
        File temp = new File(dest.getAbsolutePath() + TEMP_SUFFIX);
        int successFlag = 0;
        try (
                FileOutputStream fos = new FileOutputStream(temp);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            int len;
            byte[] buf = new byte[BUF_SIZE];
            while ((len = in.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            // 释放缓存
            bos.flush();
            // 文件复制成功后，标识自增1
            successFlag++;
        } finally {
            // 复制失败，删除临时文件
            if (successFlag == 0) {
                temp.delete();
            }
            // 临时文件改名为目标文件名，需文件流关闭后执行
            if (temp != null && dest != null) {
                temp.renameTo(dest);
            }
        }
    }


    /**
     * file copy
     * <br> ----------------------------------------
     * <br> 1、文件不存在，退出
     * <br> 2、目标文键为null，退出
     * <br> 3、目标路径下是否有同名文件处理，
     * <br>     3.1 若有同名文件，使用数字累加处理
     * <br>     3.2 考虑目标文件名没有类型的情况处理
     * <br> 4、采用缓冲、以字节流来读写文件
     * <br> 5、复制过程中采用临时文件
     * <br> 6、复制完成，去掉临时文件后缀
     * <br> ----------------------------------------
     *
     * @param opt  operation type
     * @param file file
     * @param dest destination
     * @throws IOException
     */
    private static void copyNonDirFile(int opt, File file, File dest) throws IOException {
        // 文件不存在，直接退出
        if (null == file || !file.exists()) {
            return;
        }
        // 目标路径不存在，直接退出
        if (null == dest) {
            throw new IOException("====arg dest can not be null");
        }
        // ----目标路径下存在同名文件处理, 需要考虑目标文件名中没有文件类型的情况
        dest = getFinalDestByOptForCopy(opt, dest);
        if (dest == null) {
            // ----dest == null, 说明采取了放弃模式进行文件复制
            return;
        }
        // ----临时文件----文件拷贝过程中，使用临时文件，拷贝完成，将临时文件改名为目标文件
        File tempFile = new File(dest.getAbsolutePath() + TEMP_SUFFIX);
        int successFlag = 0;
        try (
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                FileOutputStream fos = new FileOutputStream(tempFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos)
        ) {
            int len;
            byte[] buf = new byte[BUF_SIZE];
            while ((len = bis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
            // 释放缓存
            bos.flush();
            // 文件复制成功后，标识自增1
            successFlag++;
        } finally {
            // 复制失败，删除临时文件
            if (successFlag == 0) {
                tempFile.delete();
            }
            // 临时文件改名为目标文件名，需文件流关闭后执行
            if (tempFile != null && dest != null) {
                tempFile.renameTo(dest);
            }
        }
    }

    /**
     * copy file
     * <br>---------------------------------------------------
     * <br> 1、文件不存在，退出；目标路径为空，退出
     * <br> 2、检查目标路径是否存在，不存在新建
     * <br> 3、判断要复制文件是目录还是文件
     * <br> 4、若为文件直接复制到目标路径, 先判断是否目标路径是否存在同名文件
     * <br> 5、若为目录，则目标路径下新建该目录，此时目标路径改为新建目录
     * <br>    并列举目录下所以子文件，对每个文件重复4、5操作(递归实现)
     * <br> 6、文件处理过程中，正在进行复制的文件使用TEMP_SUFFIX标识，
     * <br> 7、复制完成后，去掉后缀
     * <br>---------------------------------------------------
     *
     * @param file file
     * @param dest destination
     * @throws IOException
     */
    public static void copy(int opt, File file, String dest) throws IOException {
        // 拷贝文件夹不存在，直接退出。
        if (null == file || !file.exists()) {
            throw new IOException("====arg file [" + file + "] not exists");
        }
        // 目标路径为空，退出
        if (null == dest || dest.trim().length() == 0) {
            throw new IOException("====arg dest is blank");
        }
        // 检查目标路径不通过，退出
        if (!checkDir(dest, true)) {
            return;
        }

        if (file.isDirectory()) {
            if (dest.contains(file.getAbsolutePath())) {
                throw new IOException("====can not copy parent directory to children directory");
            }
            dest = getUnixStyleFilePath(dest) + FileUtil.UNIX_SEPARATOR + file.getName();
            // 确认目标路径存在，采用递归调用，进行子文件进行复制
            if (checkDir(dest, true)) {
                for (File e : file.listFiles()) {
                    copy(opt, e, dest);
                }
            }
        } else if (file.isFile()) {
            // 目标文件不包含复制文件名的，进行名称补全
            if (!dest.endsWith(file.getName())) {
                dest = getUnixStyleFilePath(dest + FileUtil.UNIX_SEPARATOR + file.getName());
            }
            copyNonDirFile(opt, file, new File(dest));
        }
    }


    /**
     * copy file
     *
     * @param file file
     * @param dest destination
     * @throws IOException
     */
    public static void copy(int opt, String file, String dest) throws IOException {
        if (null == file || file.trim().length() == 0) {
            throw new IOException("====arg file [" + file + "] not exists");
        }

        if (file.startsWith("classpath:")) {
            copyFromClasspath(opt, file.replace("classpath:", ""), dest);
        } else {
            copy(opt, new File(file), dest);
        }

    }

    public static void copy(File file, String dest) throws IOException {
        copy(OPT_COPY_BOTH, file, dest);
    }

    public static void copy(String file, String dest) throws IOException {
        copy(OPT_COPY_BOTH, file, dest);
    }

    /**
     * 按照 classpath 路径进行文件复制
     *
     * @param path classpath 相对路径
     * @param dest 目标路径，文件系统陆行
     * @throws Exception
     */
    public static void copyFromClasspath(int opt, String path, String dest) throws IOException {
        if (StringUtil.containsBlank(path, dest)) {
            throw new IOException("==== path [" + path + "], dest [" + dest + "] can not be blank");
        }
        try (InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
            File destFile = new File(dest);
            copyNonDirFile(opt, in, destFile);
        }
    }

    /**
     * 按照 classpath 路径进行文件复制
     *
     * @param path classpath 相对路径
     * @param dest 目标路径，文件系统陆行
     * @throws Exception
     */
    public static void copyFromClasspath(String path, String dest) throws IOException {
        if (StringUtil.containsBlank(path, dest)) {
            throw new IOException("==== path [" + path + "], dest [" + dest + "] can not be blank");
        }
        try (InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path)) {
            File destFile = new File(dest);
            copyNonDirFile(OPT_COPY_COVER, in, destFile);
        }
    }


    /**
     * delete file
     *
     * @param file files
     */
    public static void delete(File... file) {
        if (file.length == 0) {
            return;
        }
        for (File e : file) {
            if (!e.exists()) {
                continue;
            }
            if (e.isDirectory()) {
                for (File f : e.listFiles()) {
                    delete(f);
                }
            }
            e.delete();
        }
    }

    /**
     * delete file
     *
     * @param file files
     */
    public static void delete(String... file) {
        if (file.length == 0) {
            return;
        }
        for (String e : file) {
            if (null == e || e.length() <= 0) {
                continue;
            }
            delete(new File(e));
        }
    }

    /**
     * file cut
     *
     * @param file file
     * @param dest destination
     * @throws IOException
     */
    public static void cut(File file, String dest) throws IOException {
        if (null == file || !file.exists()) {
            return;
        }
        if (null == dest || dest.trim().length() == 0) {
            throw new IOException("====arg dest can not be blank");
        }
        // 格式化目标路径 , 为路径结尾加上分隔符
        dest = getUnixStyleFilePath(dest + File.separator);
        if (!checkDir(dest, true)) {
            throw new IOException("====create directory dest failed");
        }

        if (file.isDirectory()) {
            String src = getUnixStyleFilePath(file.getAbsolutePath() + File.separator);
            // 目标路径与操作路径相同，不做处理，直接退出
            if (src.equals(dest)) {
                return;
            }
            // 目标路径是操作路径的子路径，抛出异常
            if (dest.startsWith(src)) {
                throw new IOException("====can not cut parent directory to childern directory");
            }
        } else {
            // 目标路径为原文件所在路径，不做处理，直接退出
            if (getUnixStyleFilePath(file.getParentFile().getAbsoluteFile() + File.separator).equals(dest)) {
                return;
            }
        }

        if (file.isDirectory()) {
            dest += file.getName();
            checkDir(dest, true);
            for (File f : file.listFiles()) {
                cut(f, dest);
            }
            file.delete();
        } else {
            File target = new File(dest + file.getName());
            file.renameTo(target);
        }
    }

    /**
     * file cut
     *
     * @param file file
     * @param dest destination
     * @throws IOException
     */
    public static void cut(String file, String dest) throws IOException {
        if (null == file || file.trim().length() == 0) {
            return;
        }
        cut(new File(file), dest);
    }

    /**
     * create new file
     *
     * @param file file
     * @return boolen
     */
    public static boolean createNewFile(File file) {
        if (null == file) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        boolean flag = false;
        if (checkDir(file.getParent(), true)) {
            try {
                flag = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * create new file
     *
     * @param file file
     * @return boolen
     */
    public static boolean createNewFile(String file) {
        if (null == file || file.trim().length() == 0) {
            return false;
        }
        return createNewFile(new File(file));
    }

    /**
     * Read files in bytes
     *
     * @param inputStream inputStream
     * @return byte[]
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        if (null == inputStream) {
            return null;
        }
        byte[] data = null;
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            data = baos.toByteArray();
        }
        return data;
    }


    /**
     * Read files in bytes
     *
     * @param file file
     * @return byte[]
     */
    public static byte[] read(File file) throws IOException {
        if ((null == file || !file.exists())) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        byte[] data = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            data = read(fis);
        }
        return data;
    }

    /**
     * Read files in bytes
     *
     * @param file file
     * @return byte[]
     */
    public static byte[] read(String file) throws IOException {
        if (null == file || file.trim().length() == 0) {
            return null;
        }
        return read(new File(file));
    }

    /**
     * Read files as characters
     *
     * @param fileInputStream fileInputStream
     * @param encoding        encoding
     * @return String
     */
    public static String reader(FileInputStream fileInputStream, String encoding) {
        // 文件不存在，或者文件为目录，返回null
        if (null == fileInputStream) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(null == encoding
                ? new InputStreamReader(fileInputStream)
                : new InputStreamReader(fileInputStream, encoding),
                1 * 1024 * 1024)
        ) {
            char[] cbuf = new char[1024 * 2];
            int len;
            while ((len = br.read(cbuf)) > 0) {
                sb.append(cbuf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Read files as characters
     *
     * @param file     file
     * @param encoding encoding
     * @return String
     */
    public static String reader(File file, String encoding) {
        // 文件不存在，或者文件为目录，返回null
        if ((null == file || !file.exists())) {
            return null;
        }
        if (file.isDirectory()) {
            return null;
        }
        String data = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            data = reader(fis, encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Read files as characters
     *
     * @param file     file
     * @param encoding encoding
     * @return String
     */
    public static String reader(String file, String encoding) {
        return reader(new File(file), encoding);
    }

    /**
     * Read files as characters
     *
     * @param fileInputStream fileInputStream
     * @return String
     */
    public static String reader(FileInputStream fileInputStream) {
        return reader(fileInputStream, null);
    }

    /**
     * Read files as characters
     *
     * @param file file
     * @return String
     */
    public static String reader(File file) {
        return reader(file, null);
    }

    /**
     * Read file in bytes
     *
     * @param file file
     * @return String
     */
    public static String reader(String file) {
        return reader(file, null);
    }

    /**
     * Write files in bytes
     *
     * @param file     file
     * @param data     data
     * @param isAppend whether append
     */
    public static void write(File file, byte[] data, boolean isAppend) {
        if (null == file) {
            return;
        }
        if (null == data || data.length == 0) {
            return;
        }
        // make sure file exists
        if (!file.exists()) {
            createNewFile(file);
        }

        try (FileOutputStream fos = new FileOutputStream(file, isAppend);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             //使用 bytes 构造成 ByteArrayInputStream，设置缓冲区读写，推荐
             ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            byte[] buf = new byte[BUF_SIZE];
            int len = 0;
            while ((len = bais.read(buf)) >= 0) {
                bos.write(buf, 0, len);
            }

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
     * Write files in bytes
     *
     * @param file     file
     * @param data     data
     * @param isAppend whether append
     */
    public static void write(String file, byte[] data, boolean isAppend) {
        write(new File(file), data, isAppend);
    }

    /**
     * Write files in bytes
     *
     * @param file file
     * @param data data
     */
    public static void write(File file, byte[] data) {
        write(file, data, false);
    }

    /**
     * Write files in bytes
     *
     * @param file file
     * @param data data
     */
    public static void write(String file, byte[] data) {
        write(new File(file), data);
    }


    /**
     * writing file
     *
     * @param file     file
     * @param data     data
     * @param isAppend whether append
     */
    public static void writer(File file, String data, boolean isAppend) {
        if (null == file || null == data) {
            return;
        }
        if (!file.exists()) {
            createNewFile(file);
        }
        try (    // new FileWriter(file, true)  true 实现文件追加写入
                 BufferedWriter bw = new BufferedWriter(new FileWriter(file, isAppend))) {
            bw.write(data);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writing file
     *
     * @param file     file
     * @param data     data
     * @param isAppend whether append
     */
    public static void writer(String file, String data, boolean isAppend) {
        writer(new File(file), data, isAppend);
    }

    /**
     * writing file
     *
     * @param file file
     * @param data data
     */
    public static void writer(File file, String data) {
        writer(file, data, false);
    }

    /**
     * writing file
     *
     * @param file file
     * @param data data
     */
    public static void writer(String file, String data) {
        writer(new File(file), data);
    }

    /**
     * Append content at the end of the file.
     * If the file does not exist, create a new file
     *
     * @param file file
     * @param data data
     */
    public static void append(File file, String data) {
        if (null == data || data.trim().length() == 0) {
            return;
        }
        if (null == file) {
            return;
        }
        if (!file.exists()) {
            createNewFile(file);
        }
        writer(file, data, true);
    }

    /**
     * Append content at the end of the file.
     * If the file does not exist, create a new file
     *
     * @param file file
     * @param data data
     */
    public static void append(String file, String data) {
        if (null == file || file.trim().length() == 0) {
            return;
        }
        if (null == data || data.length() == 0) {
            return;
        }
        append(new File(file), data);
    }

    /**
     * Check if the directory exists
     *
     * @param file file
     * @return boolean
     */
    public static boolean checkDir(File file) {
        return null != file && file.exists() && file.isDirectory();
    }

    /**
     * Check if the directory exists
     *
     * @param file file
     * @return boolean
     */
    public static boolean checkDir(String file) {
        if (null == file || file.trim().length() == 0) {
            return false;
        }
        return checkDir(new File(file));
    }

    /**
     * Check if the directory exists
     *
     * @param isNew New folder or not
     * @param file  file
     * @return boolean
     */
    public static boolean checkDir(File file, boolean isNew) {
        if (!checkDir(file) && isNew) {
            file.mkdirs();
        }
        return file.exists();
    }

    /**
     * Check if the directory exists
     *
     * @param isNew New folder or not
     * @param file  file
     * @return boolean
     */
    public static boolean checkDir(String file, boolean isNew) {
        if (null == file || file.trim().length() == 0) {
            return false;
        }
        return checkDir(new File(file), isNew);
    }

    /**
     * Check if the directory exists
     *
     * @param isNew New folder or not
     * @param file  file
     * @return boolean
     */
    public static boolean checkDir(boolean isNew, String... file) {
        if (null == file) {
            return false;
        }
        boolean flag = true;
        for (String dir : file) {
            flag = checkDir(dir, isNew);
            if (isNew && !flag) {
                break;
            }
        }
        return flag;
    }


    /**
     * get all files in the directory
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getFileList(File dir) {
        if (null == dir || !dir.exists() || !dir.isDirectory()) {
            return null;
        }
        return dir.listFiles();
    }

    /**
     * get all files in the directory
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getFileList(String dir) {
        if (null == dir || dir.trim().length() == 0) {
            return null;
        }
        return getFileList(new File(dir));
    }


    /**
     * get directory files under this directory
     * excluding sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getDirFileList(File dir) {
        if (null == dir || !dir.exists()) {
            return null;
        }

        List<File> list = new ArrayList<File>();
        for (File f : getFileList(dir)) {
            if (f.isDirectory()) {
                list.add(f);
            }
        }
        return list.toArray(new File[list.size()]);
    }

    /**
     * get directory files under this directory
     * excluding sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getDirFileList(String dir) {
        if (dir == null || dir.trim().length() == 0) {
            return null;
        }
        return getDirFileList(new File(dir));
    }

    /**
     * get non directory files under this directory
     * excluding sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getNonDirFileList(File dir) {
        if (null == dir || !dir.exists()) {
            return null;
        }

        List<File> list = new ArrayList<File>();
        File[] files = getFileList(dir);
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                list.add(files[i]);
            }
        }

        return list.toArray(new File[list.size()]);
    }

    /**
     * get non directory files under this directory
     * excluding sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getNonDirFileList(String dir) {
        if (null == dir || dir.trim().length() == 0) {
            return null;
        }
        return getNonDirFileList(new File(dir));
    }

    /**
     * get non directory files under this directory
     * including sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getAllNonDirFileList(File dir) {
        if (null == dir || (!dir.exists())) {
            return null;
        }
        if (dir.isFile()) {
            return new File[0];
        }

        List<File> list = new ArrayList<File>();
        for (File e : getFileList(dir)) {
            if (e.isFile()) {
                list.add(e);
            } else {
                for (File sf : getAllNonDirFileList(e)) {
                    list.add(sf);
                }
            }
//			else list.addAll(Arrays.asList(getAllNonDirFileList(e)));
        }
        return list.toArray(new File[list.size()]);
    }

    /**
     * get non directory files under this directory
     * including sub directory files
     *
     * @param dir directory
     * @return File[]
     */
    public static File[] getAllNonDirFileList(String dir) {
        if (null == dir || dir.trim().length() == 0) {
            return null;
        }
        return getAllNonDirFileList(new File(dir));
    }
}









