package org.ivy.util.common;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Classname	ZipUtil 
 * @Description TODO(这里用一句话描述这个类的作用) 
 * @author 		 
 * Createdate 	2017年6月26日 下午5:41:53
 */
public class ZipUtil {
	
	public void zip(File file, String targetName) throws FileNotFoundException, IOException {
		try(
				ZipOutputStream outstream = new ZipOutputStream(new FileOutputStream(file));
				FileInputStream instream = new FileInputStream(targetName);
				){
			outstream.putNextEntry(new ZipEntry(targetName));

			byte[] buffer = new byte[1024];
			int len;
			while ((len = instream.read(buffer)) > 0) outstream.write(buffer, 0, len);
			outstream.closeEntry();
		}
	}
	
	public void zip(String filename, String targetName) throws FileNotFoundException, IOException {
		File file = new File(filename);
		zip(file, targetName);
	}
	
	
	public void unzip(File zipFile, String dest) throws IOException {
		try(
				FileInputStream fis = new FileInputStream(zipFile);
				ZipInputStream zis = new ZipInputStream(fis);
				){
			ZipEntry ze;
			while ((ze = zis.getNextEntry()) != null) {
				System.out.println(ze.getName());
				zis.closeEntry();
			}
		}
	}
	
	public void upzip(String zipFilename, String dest) throws IOException {
		File file = new File(zipFilename);
		unzip(file, dest);
	}
	
	
	/**
	 * 直接读取zip压缩包中的文件
	 * 
	 * @param zipFilename
	 * @throws Exception
	 */
	public static String readZipFile(String zipFilename) throws Exception {
		StringBuffer buffer = new StringBuffer();
		ZipFile zipFile = new ZipFile(zipFilename);
		InputStream in = new BufferedInputStream(new FileInputStream(zipFilename));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
				// 目录
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(ze), "GBK"));
				String line;
				while ((line = br.readLine()) != null) buffer.append(line);
				br.close();
			}
		}
		zin.closeEntry();
		return buffer.toString();
	}

}









