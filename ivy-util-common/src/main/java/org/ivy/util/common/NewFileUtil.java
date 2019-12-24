package org.ivy.util.common;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NewileUtil
 *
 * @author miao.xl
 * @date 2014-6-5 上午09:01:55
 */
public class NewFileUtil {
    // 文件缓存大小 24K
    protected static final int FILE_BUF_SIZE = 1024 * 24;

    public static void copy(File file, String dest) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        File tempFile = null; // 文件复制的临时文件
        File targetFile = null; // 文件复制的最终文件

        // 检查目标路径
        FileUtil.checkDir(dest, true);

        if (file.exists()) {
            if (file.isDirectory()) {
                dest += File.separator + file.getName();
                File dir = new File(dest);
                if (!(dir.exists() && dir.isDirectory())) dir.mkdirs();

                File[] files = file.listFiles();

                for (int i = 0; i < files.length; i++) copy(files[i], dest);
            } else if (file.isFile()) {
                // 文件复制过程中，临时文件全名
                String tempFileUrl = dest + File.separator + file.getName() + FileUtil.TEMP_SUFFIX;
                // 复制文件目标路径
                String targetFileUrl = dest + File.separator + file.getName();
                tempFile = new File(tempFileUrl);
                targetFile = new File(targetFileUrl);
                try {
                    fis = new FileInputStream(file);
                    fos = new FileOutputStream(tempFile);
                    // 利用 NIO 实现文件复制
                    inChannel = fis.getChannel();
                    outChannel = fos.getChannel();
                    ByteBuffer buf = ByteBuffer.allocate(FILE_BUF_SIZE);
                    while (true) {
                        buf.clear();
                        if (inChannel.read(buf) <= 0) break;
                        buf.flip();
                        outChannel.write(buf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inChannel != null) inChannel.close();
                        inChannel = null;
                        if (outChannel != null) outChannel.close();
                        outChannel = null;
                        if (fos != null) fos.close();
                        fos = null;
                        if (fis != null) fis.close();
                        fis = null;
                        // 文件改名只用关闭流后操作才行， 文件复制完后，文件改名，去掉后缀
                        if (tempFile != null && targetFile != null) tempFile.renameTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 以字节流读文件 --NIO实现
     */
    public static byte[] read(File file) {
        byte[] original = null;
        FileInputStream fis = null;
        FileChannel channel = null;
        ByteArrayOutputStream baos = null;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                channel = fis.getChannel();
                baos = new ByteArrayOutputStream();
                ByteBuffer buffer = ByteBuffer.allocate(FILE_BUF_SIZE);
                int len;
                while ((len = channel.read(buffer)) > 0) {
                    buffer.flip();
                    baos.write(buffer.array(), 0, len);
                    buffer.clear();
                }
                original = baos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null)
                        baos.close();
                    if (channel != null)
                        channel.close();
                    if (fis != null)
                        fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return original;
    }

    /**
     * <p>copy 多线程实现 </p>
     *
     * @param file  源文件
     * @param dest  目标路径
     * @param async 是否异步
     * @author miao.xl
     * @date 2016年4月1日-下午1:19:05
     */
    public void copy(File file, String dest, boolean async) {
        if (async) {
            new Thread(new CopyRunnable(file, dest)).start();
        } else {
            this.copy(file, dest);
        }
    }

    /**
     * <p>copy</p>
     *
     * @param fileUrl 源文件路径
     * @param dest    目标路径
     * @param async   是否异步
     * @author miao.xl
     * @date 2016年4月1日-下午1:17:54
     */
    public void copy(String fileUrl, String dest, boolean async) {
        File file = new File(fileUrl);
        this.copy(file, dest, async);
    }

    /**
     * CopyRunnable 文件复制线程
     *
     * @author miao.xl
     * @version 1.0
     * @date 2016年3月31日-下午7:22:01
     */
    private class CopyRunnable implements Runnable {
        private File file;
        private String dest;

        public CopyRunnable(File file, String dest) {
            this.file = file;
            this.dest = dest;
        }

        @Override
        public void run() {
            // 调用外部类中copy() 实现文件的复制
            copy(file, dest);
        }
    }

}
