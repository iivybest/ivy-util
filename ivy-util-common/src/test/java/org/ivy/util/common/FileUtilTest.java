package org.ivy.util.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

/**
 * <p> description:
 * <br>---------------------------------------------------------
 * <br> the testcase of FileUtil
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2017/12/22 10:25
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class FileUtilTest {

    private static final String CLASS_PATH = SystemUtil.getClasspath();

    @Before
    public void setUp() {
    }

    @Test
    public void test_00_base() throws Exception{
        File file = new File(CLASS_PATH + "material/HelloWorld.txt");
        String path = FileUtil.getUnixStyleFilePath(new File("")) + "/";
        log.debug("{path: {}}", path);
        log.debug("{path: {}}", FileUtil.getUnixStyleFilePath(System.getProperty("user.dir") + IvyConstant.UNIX_SEPARATOR));
        log.debug("{CLASS_PATH: {}}", CLASS_PATH);

        log.debug("{file.exists(): {}}", file.exists());
        log.debug("file.isDirectory(): {}}", file.isDirectory());
        log.debug("{file.isFile(): {}}", file.isFile());
        log.debug("{file.getAbsolutePath(): {}}", file.getAbsolutePath());
        log.debug("{file.getParent(): {}}", file.getParent());
        log.debug("{file.getName(): {}}", file.getName());
    }

    @Test
    public void test_01_getUnixSeparatorFilePath() {
        File file = new File(CLASS_PATH + "\\\\material///////HelloWorld.txt");
        log.debug(file.getAbsolutePath());
        log.debug(FileUtil.getUnixStyleFilePath(file));
        log.debug(FileUtil.getUnixStyleFilePath("D:\\Users//ivybest\\\\照片查看器.reg"));

        log.debug(FileUtil.getUnixStyleFilePath("D:\\Users//ivybest\\\\"));
        log.debug(FileUtil.getUnixStyleFilePath(new File("D:\\Users//ivybest\\\\")));
    }

    @Test
    public void test_02_reader() {
        String content = FileUtil.reader(CLASS_PATH + "material/HelloWorld.txt");
        log.debug(content);
    }

    @Test
    public void test_03_copy() throws Exception {
        FileUtil.copy("D:/app/logs", "D:/logs/ivybest/");
    }

    @Test
    public void test_04_cut() throws IOException {
        FileUtil.cut("D:/app/logs/test", "D:/logs/ivybest");
        //FileUtil.cut("D:\\ImiaoDev\\logs", "D:\\");
    }
}
