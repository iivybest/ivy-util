package org.ivy.xutil.log;

import org.apache.commons.lang.StringUtils;
import org.ivy.util.cfg.IvyUtilConf;
import org.ivy.util.cfg.IvyUtilConstant;
import org.ivy.util.common.DateTimeUtil;
import org.ivy.util.common.FileUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p> description:
 * <br>--------------------------------------------------------
 * <br> custom log util
 * <br>--------------------------------------------------------
 * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2014/5/19 09:24
 */
public class LogUtil {
    /* LogUtil 工具内部参数 start **********************************/
    /**
     * 路径分隔符
     */
    private String separator;
    /**
     * 默认日志名
     */
    private String defaultLogName;
    /* LogUtil 工具内部参数 end ************************************/

    /**
     * 记录日志者的class类型
     */
    private Class<?> logClass;
    /**
     * 日志路径
     */
    private String logUrl;
    /**
     * 以线程 ID 分割日志
     */
    private boolean splitLogByThreadId;
    /**
     * 日志文件流
     */
    private PrintWriter writer;

    /**
     * constructor
     *
     * @param logClass           logClass
     * @param logUrl             logUrl
     * @param splitLogByThreadId whether split log by thread id
     * @param <T>                logClass type
     */
    public <T> LogUtil(Class<T> logClass, String logUrl, boolean splitLogByThreadId) {
        this.logClass = logClass;
        this.logUrl = logUrl;
        this.splitLogByThreadId = splitLogByThreadId;
        this.initialize();
    }

    public <T> LogUtil(Class<T> logClass, String logUrl) {
        this(logClass, logUrl, false);
    }

    public LogUtil(String logUrl, boolean splitLogByThreadId) {
        this(LogUtil.class, logUrl, splitLogByThreadId);
    }

    public <T> LogUtil(Class<T> logClass) {
        this(logClass, IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
    }

    public LogUtil(String logUrl) {
        this(LogUtil.class, logUrl);
    }

    public LogUtil() {
        this(IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
    }

    private void initialize() {
        this.separator = "/";
        this.defaultLogName = "log.log";
        this.checkLogDir();
        try {
            this.writer = new PrintWriter(new FileWriter(this.getCurrentLogUrl(), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * finalize operation: close stream before destruction
     *
     * @throws Throwable Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        if (null != this.writer) {
            this.writer.flush();
            this.writer.close();
            this.writer = null;
        }
        super.finalize();
    }

    /**
     * set log directory
     *
     * @param logDir log directory
     */
    private void setLogUrl(String logDir) {
        this.logUrl = logDir.replace("\\", this.separator);
    }

    /**
     * get current log directory path
     *
     * @return String
     */
    private String getCurrentLogUrl() {
        if (this.splitLogByThreadId) {
            return this.logUrl + "_" + Thread.currentThread().getId();
        } else {
            return this.logUrl;
        }
    }

    /**
     * check log dir whether exists
     *
     * <br>-------------------------------------------------------------------------
     * <br> 规则：指定路径文件不存在时
     * <br>     若目标路径不含 “.” 认定为其为路径, 日志记录在该路径下，日志文件以 log.log 命名
     * <br>     若包含 “.”，则认定其为文件，日志记录此文件中。
     * <br>
     * <br>    1、检查路径是否存在，
     * <br>    2、若存在，检查其是目录还是文件，
     * <br>        2.1、若为文件，无动作
     * <br>        2.2、若为目录，则在该目录新建log.log，并将log.log指定为日志路径
     * <br>    3 、若不存在，检查其是目录还是文件（规则如上面定义）
     * <br>        3.1、若为文件
     * <br>            3.1.1、检查其父目录
     * <br>        3.2、若为目录
     * <br>            3.2.2、检查该目录，指定log.log为日志路径
     * <br>-------------------------------------------------------------------------
     */
    private void checkLogDir() {
        /* 若无指定日志目录，设定为系统默认路径  默认路径配置在资源文件中  */
        if (StringUtils.isBlank(this.logUrl)) {
            this.setLogUrl(IvyUtilConf.getProperty(IvyUtilConstant.LOG_DIR));
        }

        File file = new File(this.logUrl);
        if (file.exists()) {
            if (file.isDirectory()) {
                this.setLogUrl(file.getAbsolutePath() + this.separator + this.defaultLogName);
            }
        } else if (!file.exists()) {
            // 含有“.”按文件操作
            if (this.logUrl.contains(".")) {
                FileUtil.createNewFile(file);
            } else {
                // 按目录操作
                String path = file.getAbsolutePath() + this.separator + this.defaultLogName;
                FileUtil.createNewFile(path);
                this.setLogUrl(path);
            }
        }
    }


    private String prefix() {
        return "["
                + DateTimeUtil.currentDateTime("yyyy-MM-dd HH:mm:ss:SSS")
                + "]-["
                + Thread.currentThread().getId()
                + "]-["
                + this.logClass.getName()
                + "]-";
    }


    private String format(String data) {
        return this.prefix() + data;
    }

    /**
     * print on console
     *
     * @param data data
     */
    private void print(Object data) {
        System.out.println(data);
    }

    /**
     * log data
     *
     * @param data data
     */
    public void log(Object data) {
        this.log(data, true);
    }

    /**
     * log data and print in console
     *
     * @param data      data
     * @param printable printable
     */
    public void log(Object data, boolean printable) {
        if (null == data) {
            data = "null";
        }
        data = this.format(data.toString());

        if (printable) {
            this.print(data);
        }
        this.writer.println(data);
        this.writer.flush();
    }

    /**
     * log by bytes
     *
     * @param data data
     */
    public void logByBytes(String data) {
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        try (FileOutputStream fos = new FileOutputStream(this.getCurrentLogUrl(), true);
             FileChannel channel = fos.getChannel()) {
            buffer.put(data.getBytes());
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}



