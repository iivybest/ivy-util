package org.ivy.util.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p> description: gzip工具
 * <br>--------------------------------------------------------
 * <br>
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @className GZipUtil
 * @date 2019/12/18 17:18
 */
public class GzipUtil {
    private static final int BUF_SIZE;
    public static Logger log = LoggerFactory.getLogger(GzipUtil.class);

    static {
        BUF_SIZE = 1024 * 4;
    }

    /**
     * <p>gZip压缩</p>
     *
     * @param data data
     * @return byte array
     * @throws IOException
     */
    public static byte[] compress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        byte[] gZipData = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ByteArrayInputStream bais = new ByteArrayInputStream(data);
             // 此类为使用 GZIP 文件格式写入压缩数据实现流过滤器
             GZIPOutputStream gzipos = new GZIPOutputStream(baos)) {
            int len;
            byte[] buf = new byte[BUF_SIZE];
            while ((len = bais.read(buf)) > 0) {
                gzipos.write(buf, 0, len);
            }

            gzipos.finish();
            gzipos.flush();
            gZipData = baos.toByteArray();
        }
        return gZipData;
    }

    /**
     * <p>gZip压缩</p>
     *
     * @param data data
     * @return byte array
     * @throws IOException
     */
    public static byte[] compress(String data) throws IOException {
        if (data == null || data.length() == 0) {
            return null;
        }
        return compress(data.getBytes());
    }

    /**
     * <p>gZip解压缩</p>
     *
     * @param data data
     * @return byte array
     * @throws IOException
     */
    public static byte[] decompress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        byte[] unGzipData;

        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                GZIPInputStream gzipis = new GZIPInputStream(bais);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // 缓冲区，每次读取的最大长度
            byte[] buf = new byte[BUF_SIZE];
            // 读取长度
            int len;
            while ((len = gzipis.read(buf)) > 0) {
                baos.write(buf, 0, len);
            }
            unGzipData = baos.toByteArray();
            baos.flush();

            // 利用conmmons-io.jar替代baos的输出工作
            // unGzipData = IOUtils.toByteArray(gzipis);
        } catch (IOException e) {
            log.error("==== decompress error-[]", e.getMessage());
            throw new IOException("==== decompress error", e);
        }
        return unGzipData;
    }

    public static String decompress2String(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        String unGzipData;
        /* strategy 1*/
        unGzipData = new String(decompress(data));
        /* strategy 2 */
//		unGzipData = IOUtils.toString(gzipis, encoding);
        return unGzipData;
    }

    public static String decompress2String(byte[] data, String encoding) throws IOException {
        if (data == null || data.length == 0) {
            return null;
        }
        if (null == encoding || encoding.length() == 0) {
            return null;
        }
        return new String(decompress(data), encoding);
    }


}

