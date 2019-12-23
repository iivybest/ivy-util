/**   
 * @Filename 	FileUtilTest 
 * @Description TODO 
 * @author 		ivybest   
 * @version 	V1.0   
 * @CreateDate 	2017年12月22日 上午10:25:20
 * @Company 	IB.
 * @Copyright 	Copyright(C) 2010-
 * 				All rights Reserved, Designed By ivybest
 *
 * Modification History:
 * Date				Author		Version		Discription
 * --------------------------------------------------------
 * 2017年12月22日	ivybest		1.0			new create
 */
package org.ivy.util.common;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/** 
 * @Classname	FileUtilTest
 * @author 		ivybest imiaodev@163.com
 * @Createdate 	2017年12月22日 上午10:25:20
 * @Version		1.0
 * ------------------------------------------
 * @Description TODO(这里用一句话描述这个类的作用)
 */
public class FileUtilTest {
	
	private FileUtil util;
	
	@Before
	public void setUp() {
		util = new FileUtil();
	}
	
	@Test
	public void fileTest() {
		File file = new File("D:\\Users\\ivybest\\ok");
		System.out.println(file.exists());
		System.out.println(file.isDirectory());
		System.out.println(file.isFile());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getParent());
		System.out.println(file.getName());
	}
	
	@Test
	public void testGetUnixSeparatorFilePath() {
		File file = new File("D:\\Users\\ivybest\\\\照片查看器.reg");
		System.out.println(file.getAbsolutePath());
		System.out.println(this.util.getUnixStyleFilePath(file));
		System.out.println(this.util.getUnixStyleFilePath("D:\\Users//ivybest\\\\照片查看器.reg"));
		
		System.out.println(this.util.getUnixStyleFilePath("D:\\Users//ivybest\\\\"));
		System.out.println(this.util.getUnixStyleFilePath(new File("D:\\Users//ivybest\\\\")));
	}
	
	@Test
	public void readerTest() {
		String content = this.util.reader("E:/Ivybest/test/guide/aspectlog.log");
		System.out.println(content);
	}
	
	@Test
	public void testCopy() throws Exception {
		this.util.copy("F:/MyFiles/Vedio/Movie/Youth.2017.4K.AVC.mp4", "F:/MyFiles/Vedio/Movie/");
	}
	
	@Test 
	public void testCut() throws IOException {
		this.util.cut("D:\\logs", "D:\\logs\\ImiaoDev");
		//this.util.cut("D:\\ImiaoDev\\logs", "D:\\");
	}
	
	
	
	
}
