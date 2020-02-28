package org.ivy.util.tool;

import org.ivy.util.common.FileUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


/**
 * <p>  classname: LocalFileHandler
 * <br> description:
 * <br>---------------------------------------------------------
 * <br> 本地文件名修改
 * <br> 本地文件处理
 * <br> 本地文件删除
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2019/12/24 0:45
 */
public class LocalFileHandler {
    private static int counter = 0;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String base;
    private String path;
    private String done;
    private String temp;

    private Collection<String> types;

    @Before
    public void setUp() {
        this.types = Arrays.asList("mp4", "mkv", "avi", "flv");


//		this.base = "E:/Ivybest/Tech/Tutorials/01.Suit/06.尚硅谷/[优]-MySQL高级/";
//		this.path = base + "/";
//		this.base = "D:/BaiduNetdiskDownload/";
        // ----文件处理基准路径
//		this.base = "D:\\BaiduNetdiskDownload\\netty高并发-张龙/";
        this.base = "D:\\BaiduNetdiskDownload\\Spring Cloud微服务实战-慕课-廖师兄";
        // ----scan path----format path
        this.path = FileUtil.getUnixStyleFilePath(this.base + "/");
        // ----temp path
        this.done = this.path + "__done/";
        this.temp = this.path + "__temp/";
    }

    @Test
    public void cutFile() {
//		String[] originFileTypes = {"avi", "flv"};
        // ----源文件类型
        String originFileType = "mp4";

        FileUtil.checkDir(true, done, temp);
        File[] sub = FileUtil.getAllNonDirFileList(path);
        Stream.of(sub).forEach(e -> {
            String type = FileUtil.getFileType(e);
            if (!types.contains(type)) {
                return;
            }

            // ----原文件非MP4文件，处理方式
            if (!"mp4".equals(originFileType)) {
                if ("mp4".equals(type)) {
                    String originFilename = e.getParentFile().getAbsolutePath()
                            + "/" + FileUtil.getFilenameWithoutFileType(e) + "." + originFileType;
//					if (logger.isDebugEnabled()) logger.debug("---->scanning- " + e.getName());
                    try {
                        if (new File(originFilename).exists()) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("---->scanning- " + e.getName());
                            }
                            if (logger.isDebugEnabled()) {
                                logger.debug("====>processed-" + ++counter + "-" + originFilename);
                            }
                            FileUtil.cut(originFilename, temp);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            // ----处理MP4文件
            if ("mp4".equals(originFileType)) {
                if ("mp4".equals(type) && e.getName().endsWith("~1.mp4")) {
                    String originFilename = e.getAbsolutePath().replace("~1.mp4", ".mp4");
                    logger.debug("---->scanning-{}", e.getName());
                    File originFile = new File(originFilename);
                    try {
                        if (originFile.exists()) {
                            logger.debug("====>processed-{}-{}", ++counter, originFilename);
                            FileUtil.cut(originFile, temp);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
        logger.debug("---->processed-count-" + counter);
        // ----函数式语言，打印所有文件名称
//		Arrays.asList(sub).stream().map(e -> e.getName()).forEach(System.out::println);
    }

    @Test
    public void renameFile() {
        String path = "C:/Users/Ivybest/Videos/Java读源码之Netty深入剖析--慕课/";
        File[] sub = FileUtil.getAllNonDirFileList(path);

        Stream.of(sub).forEach(e -> {
            String type = FileUtil.getFileType(e);
            if (!types.contains(type)) {
                return;
            }

            logger.debug("----processing-" + ++counter + "-" + e.getName() + ", type:" + type);
//			String newName = e.getParentFile().getAbsolutePath() + "/" + e.getParentFile().getName() + "-" + e.getName();
            String newName = e.getParentFile().getAbsolutePath() + "/" + e.getName().replaceAll("\\s+", "_");
            e.renameTo(new File(newName));
        });
        logger.debug("---------------------> " + counter);
    }

    @Test
    public void fixFilename() {
        String path = "C:/Users/Ivybest/Videos/Java读源码之Netty深入剖析--慕课/";
        String regex = "\\d+-\\d{3}";
        Pattern pattern = Pattern.compile(regex);

        File[] sub = FileUtil.getAllNonDirFileList(path);
        Arrays.asList(sub).forEach(e -> {
            String type = FileUtil.getFileType(e);
            if (!types.contains(type)) {
                return;
            }
            String filename = e.getName();
//			logger.debug("----processing-" + ++counter + "- " + filename + ", type:" + type);
            Matcher matcher = pattern.matcher(filename);

            int left, right = -1;
            String prifix, suffix = null;
            while (matcher.find()) {
                left = matcher.start();
                right = matcher.end() - 2;
//				prifix = new StringBuffer(filename.substring(left, right)).insert(3, "0").toString();
//				prifix = new StringBuffer(filename.substring(left, right)).deleteCharAt(3).toString();
                prifix = filename.substring(left, right - 1);
                suffix = filename.substring(right);//.replaceAll("\\s+", "");
//				if (left != 0) filename = filename.substring(0, left);
                String newname = e.getParentFile().getAbsolutePath() + "/" + prifix + suffix;
                logger.debug(filename);
                logger.debug(newname + "\n");
                e.renameTo(new File(newname));
            }
        });
    }

    @Test
    public void fixFilename2() {
        String path = "D:\\BaiduNetdiskDownload\\黑马57期\\09 微服务电商【黑马乐优商城】·";
        String regex = "\\d+[_][A-Za-z0-9]+(.mp4)";

        File[] sub = FileUtil.getAllNonDirFileList(path);
        Arrays.asList(sub).forEach(e -> {
            String type = FileUtil.getFileType(e);
            if (!types.contains(type)) {
                return;
            }
            String filename = e.getName();
            String filenameTo;
            if (filename.matches(regex)) {
                filenameTo = e.getParentFile().getAbsolutePath()
                        + "/"
                        + filename.replaceAll("(_)[A-Za-z0-9]+", "");
                logger.debug("\r\n{ \r\n\"filename\": \"{}\", \r\n\"filenameTo\": \"{}\"\r\n }", filename, filenameTo);
                e.renameTo(new File(filenameTo));
            }
        });
    }

    @Test
    public void testRecursiveLoadSubFile() {
        this.path = "D:/BaiduNetdiskDownload/done/";
        File[] sub = FileUtil.getAllNonDirFileList(this.path);
        Arrays.asList(sub).forEach(System.out::println);
    }

    /**
     * origin: musicName_author.type
     * target: author_musicName.type
     */
    @Test
    public void test_05_fixMusicName() {
        String separator = " - ";
        String path = "J:\\ivybest\\My Files\\Audio\\Music\\Singles\\320k/";
        File[] files = FileUtil.getNonDirFileList(path);
        Arrays.stream(files).forEach(e -> {
            String dest = e.getParentFile().getAbsolutePath();
            String type = FileUtil.getFileType(e);

            String nameWithoutType = FileUtil.getFilenameWithoutFileType(e);
            String fixed = e.getName();
            if (nameWithoutType.contains("_")) {
                String [] items = nameWithoutType.split("_");
                fixed = items[1] + separator + items[0] + "." + type;
                e.renameTo(new File(dest + "/" + fixed));
            }
            logger.debug("{dest: {}, type: {}, name: {}, fixed: {}}", dest, type, e.getName(), fixed);
        });
    }

    /**
     * add sequence '(Live)' 4 music name
     */
    @Test
    public void test_05_fixMusicName4Live() {
        String separator = " (Live)";
        String path = "J:\\ivybest\\My Files\\Audio\\Music\\song\\done/";
        File[] files = FileUtil.getNonDirFileList(path);
        Arrays.stream(files).forEach(e -> {
            String dest = e.getParentFile().getAbsolutePath();
            String type = FileUtil.getFileType(e);
            String name = e.getName();
            String nameWithoutType = FileUtil.getFilenameWithoutFileType(e);
            String fixed = nameWithoutType + separator + "." + type;
            if (! nameWithoutType.contains(separator)) {
                e.renameTo(new File(dest + "/" + fixed));
            }
            logger.debug("{dest: {}, type: {}, name: {}, fixed: {}}", dest, type, name, fixed);
        });
    }


}












