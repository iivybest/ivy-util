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
 * @className GZipUtil
 * @author Ivybest (ivybestdev@163.com)
 * @date 2019/12/18 17:18
 * @version 1.0
 */
public class GZipUtil {
	public static Logger log = LoggerFactory.getLogger(GZipUtil.class);
	private static final int BUF_SIZE;
	
	static {
		BUF_SIZE = 1024 * 4;
	}
	
	/**
	 * <p>gZip压缩</p>
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException{
		if (data == null || data.length == 0) return null;
		byte[] gZipData = null;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				// 此类为使用 GZIP 文件格式写入压缩数据实现流过滤器
				GZIPOutputStream gzipos = new GZIPOutputStream(baos)) {
			int len;
			byte[] buf = new byte[BUF_SIZE];
			while((len = bais.read(buf)) > 0) gzipos.write(buf, 0, len);
			
			gzipos.finish();
			gzipos.flush();
			gZipData = baos.toByteArray();
		}
		return gZipData;
	}
	/**
	 * <p>gZip压缩</p>
	 * 
	 * @param original
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(String original) throws IOException{
		if(original == null || original.length() == 0) return null;
		byte[] data = original.getBytes();
		return compress(data);
	}
	
	/**
	 * <p>gZip解压缩</p>
	 * 
	 * @param data
	 * @return
	 * @throws IOException 
	 */
	public static  byte[] decompress(byte[] data) throws IOException{
		if(data == null || data.length == 0) return null;
		byte[] unGZipData = null;

		try(
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				GZIPInputStream gzipis = new GZIPInputStream(bais);
				ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			// 缓冲区，每次读取的最大长度
			byte[] buf = new byte[BUF_SIZE];
			// 读取长度
			int len;
			while((len = gzipis.read(buf)) > 0) {
				baos.write(buf, 0, len);
			}
			unGZipData = baos.toByteArray();
			baos.flush();
			
			// 利用conmmons-io.jar替代baos的输出工作
			// unGZipData = IOUtils.toByteArray(gzipis);
		}catch(IOException e){
			log.error("==== decompress error-[]", e.getMessage());
			throw new IOException("==== decompress error", e);
		}
		return unGZipData;
	}
	
	public static String decompress2String(byte[] data) throws IOException{
		if(data == null || data.length == 0) return null;
		String unGZipData = null;
		/* strategy 1*/
		unGZipData = new String(decompress(data));
		/* strategy 2 */
//		unGZipData = IOUtils.toString(gzipis, encoding);
		return unGZipData;
	}
	
	public static String decompress2String(byte[] data, String encoding) throws IOException{
		if(data == null || data.length == 0) return null;
		if(null == encoding || encoding.length() == 0) return null;
		return new String(decompress(data), encoding);
	}
	
	
}

