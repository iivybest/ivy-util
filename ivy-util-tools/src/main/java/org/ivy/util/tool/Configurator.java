package org.ivy.util.tool;

import org.ivy.util.common.FileUtil;
import org.ivy.util.common.PropertiesUtil;
import org.ivy.util.common.StringUtil;
import org.ivy.util.common.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> requirement:
 * <br> 1、构建过于负责--builer模式（变种）
 * <br> 2、单例模式 (去除单例模式，复杂应用使用 spring 管理 configurator 对象)
 * <br> 3、用户自定义扫描位置、优先级
 * <br> 4、用户自定义例外文件或位置
 * <br> 5、多文件类型支持 --二期实现
 * <br> 6、配置文件修改即时感应 --二期实现
 * <br>---------------------------------------------------------
 * <br> description（logic）:
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description 配置文件管理器
 * @date 2020/2/28 10:46
 */
public class Configurator {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(Configurator.class);
    /**
     * 默认扫描路径，用户不指定扫描路径则扫描默认路径
     */
    private static List<ConfPathEntry> defaultConfContainer;
    /**
     * 配置管理器标识
     */
    private String id;
    /**
     * 扫描路径集合
     */
    private List<ConfPathEntry> scanContainer;
    /**
     * 例外路径集合、用户配置例外路径
     */
    private List<ConfPathEntry> exceptContainer;
    /**
     * 有效例外路径集合。不扫描该路径集合
     */
    private List<String> effectiveExceptContainer;
    /**
     * 配置文件管理器、内部工作者
     */
    private ConfMgr confMgr;
    /**
     * 配置文件计数器
     */
    private AtomicInteger counter;


    static {
        // 初始化默认扫描路径--项目路径和 classpath--非递归扫描
        defaultConfContainer = new ArrayList<>();
        defaultConfContainer.add(ConfPathEntry.newInstance(0, Boolean.FALSE, SystemUtil.getClasspath()));
        defaultConfContainer.add(ConfPathEntry.newInstance(1, Boolean.FALSE, SystemUtil.getUserDir()));
    }


    public Configurator() {
        initializeInstanceArgsWithLiteral();
    }

    /**
     * 实例化一个 Configurator
     *
     * @param id configurator id
     * @return Configurator object
     */
    public static Configurator newInstance(String id) {
        return new Configurator().setId(id);
    }


    public Configurator addConfPath(int priority, boolean recursion, String... path) {
        for (String e : path) {
            this.scanContainer.add(ConfPathEntry.newInstance(priority, recursion, e));
        }
        return this;
    }

    /**
     * add one or multi scanning path(file or directory)
     *
     * @param recursion weather recursion read directory
     * @param path      scanning file paht
     * @return Configurator this object
     */
    public Configurator addConfPath(boolean recursion, String... path) {
        return addConfPath(this.counter.getAndIncrement(), recursion, path);
    }

    /**
     * add one or multi scanning path(file or directory)
     * and recursion is false
     *
     * @param path scanning file paht
     * @return Configurator this object
     */
    public Configurator addConfPath(String... path) {
        return this.addConfPath(false, path);
    }


    /**
     * add one or multi except scanning path(file or directory)
     * and recursion is false
     *
     * @param path scanning file paht
     * @return Configurator this object
     */
    public Configurator addExpectPath(String... path) {
        for (String e : path) {
            this.exceptContainer.add(ConfPathEntry
                    .newInstance(-1, Boolean.TRUE, FileUtil.getUnixStyleFilePath(e)));
        }
        return this;
    }


    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> build configurator object.
     * <br> If user does not specify a configuration,
     * <br> use the default configuration file
     * <br>---------------------------------------------------------
     * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @return configuration object
     */
    public Configurator build() {
        this
                // ----检查扫描路径
                .initializeScanContainerBeforeBuild()
                // ----检查设置例外路径，并设置有效例外路径
                .initializeEffectiveExceptContainerBeforeBuild()
                // ----初始化 ConfMgr -- # confMgr.init() 返回对象为 confMgr
                .getConfMgr().init();
        return this;
    }

    /**
     * initialize scanContainer Before operation Build
     *
     * @return Configurator
     */
    public Configurator initializeScanContainerBeforeBuild() {
        // ----检查设置扫描路径集合
        if (this.getScanContainer().size() == 0) {
            this.setScanContainer(Configurator.defaultConfContainer);
        }
        return this;
    }

    /**
     * initialize EffectiveExceptContainer Before operation Build
     *
     * @return Configurator
     */
    public Configurator initializeEffectiveExceptContainerBeforeBuild() {
        if (this.exceptContainer.size() > 0) {
            File file;
            String unixStyleFilePath;
            for (ConfPathEntry e : this.exceptContainer) {
                file = new File(e.getValue());
                if (!file.exists()) {
                    continue;
                }
                unixStyleFilePath = FileUtil.getUnixStyleFilePath(file);
                this.effectiveExceptContainer.add(unixStyleFilePath);
            }
        }
        return this;
    }


    /**
     * initialize the configuration instance args with literal
     *
     * @return Configurator this object
     */
    private Configurator initializeInstanceArgsWithLiteral() {
        this.counter = new AtomicInteger(1_000);
        return this
                .setScanContainer(new ArrayList<>())
                .setExceptContainer(new ArrayList<>())
                .setEffectiveExceptContainer(new ArrayList<>())
                .setConfMgr(new ConfMgr(this));
    }


    /**
     * <p>
     * <br>---------------------------------------------------------------
     * <br> description:
     * <br>     * get a series of configuration items with the same prefix
     * <br>     * user can specify multiple keys TODO
     * <br>     * user can specify whether to remove the prefix
     * <br>---------------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param removePrefix whether remove prefix of key
     * @param keyPrefix    prefix of key
     * @return configuration map
     */
    public Map<String, String> get(boolean removePrefix, String keyPrefix) {
       return this.confMgr.get(removePrefix, keyPrefix);
    }

    public String get(String key) {
        return this.confMgr.get(key);
    }


    public void set(String key, String val) {
        this.confMgr.set(key, val);
    }

    /**
     * 获取必须包含项配置----若配置信息为空--抛出异常
     *
     * @param key key
     * @return value
     * @throws Exception
     */
    public String getForcedIncludeConfiguration(String key) throws Exception {
        return this.confMgr.getForcedIncludeConfiguration(key);
    }

    /**
     * return the value of this key，if the value is blank，return the defaultValue
     *
     * @param key        key
     * @param defaultVal default Value
     * @return String
     */
    public String get(String key, String defaultVal) {
        return this.confMgr.get(key, defaultVal);
    }

    /**
     * 从几个 key 中获取其中一个配置项并返回
     *
     * @param keys keys
     * @return String
     */
    public String get(String... keys) {
        return this.confMgr.get(keys);
    }

    public boolean containsKey(String key) {
        return this.confMgr.containsKey(key);
    }

    /**
     * 列出所有资源属性
     */
    public void list() {
        this.confMgr.list();
    }


    public String getId() {
        return id;
    }

    public Configurator setId(String id) {
        this.id = id;
        return this;
    }

    public List<ConfPathEntry> getScanContainer() {
        return scanContainer;
    }

    private Configurator setScanContainer(List<ConfPathEntry> scanContainer) {
        this.scanContainer = scanContainer;
        return this;
    }

    public List<ConfPathEntry> getExceptContainer() {
        return exceptContainer;
    }

    private Configurator setExceptContainer(List<ConfPathEntry> exceptContainer) {
        this.exceptContainer = exceptContainer;
        return this;
    }

    public List<String> getEffectiveExceptContainer() {
        return effectiveExceptContainer;
    }

    private Configurator setEffectiveExceptContainer(List<String> effectiveExceptContainer) {
        this.effectiveExceptContainer = effectiveExceptContainer;
        return this;
    }

    public ConfMgr getConfMgr() {
        return confMgr;
    }

    public Configurator setConfMgr(ConfMgr confMgr) {
        this.confMgr = confMgr;
        return this;
    }

    /**
     * <p>
     * <p> description: 系统配置参数内部管理器
     * <br>--------------------------------------------------------
     * <br>	project 初始化时，加载系统配置项
     * <br>--------------------------------------------------------
     * <br>Copyright@2019 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @author Ivybest (ivybestdev@163.com)
     * @version 1.0
     * @date 2017/6/20 12:24
     */
    private static class ConfMgr {
        private Configurator configurator;
        /**
         * lock
         */
        private ReentrantReadWriteLock lock;
        private ReentrantReadWriteLock.ReadLock readLock;
        private ReentrantReadWriteLock.WriteLock writeLock;
        /**
         * 全部常量
         */
        private Map<String, String> allConfigurationsMap;
        /**
         * 特定前置常量前缀缓存
         * 用户查询过一次后，计入缓存，方便下一次调用
         */
        private Map<String, Map<String, String>> specificPrefixConfigurationsCache;

        public ConfMgr(Configurator configurator) {
            this.configurator = configurator;
        }

        /**
         * init
         *
         * @return ConfMgr
         */
        private ConfMgr init() {
            return this.initReadWriteLock().initArgs().loadConf();
        }

        private ConfMgr initReadWriteLock() {
            this.lock = new ReentrantReadWriteLock();
            this.readLock = lock.readLock();
            this.writeLock = lock.writeLock();
            return this;
        }

        /**
         * 初始化成员变量
         */
        private ConfMgr initArgs() {
            this.allConfigurationsMap = new HashMap<String, String>();
            this.specificPrefixConfigurationsCache = new HashMap<>();
            return this;
        }


        /**
         * 加载 project 配置项
         *
         * @return ConfMgr
         */
        private ConfMgr loadConf() {
            // ----将系统参数 classpath 保存到 ConfMgr 中。
            this.allConfigurationsMap.put("class.path", SystemUtil.getClasspath());
            this.allConfigurationsMap.put("user.dir", SystemUtil.getUserDir());
            this.allConfigurationsMap.put("os.arch", SystemUtil.getOSArch());
            this.allConfigurationsMap.put("os.name", SystemUtil.getOsName());

            List<ConfPathEntry> scanContainer = this.configurator.scanContainer;

            File file;
            for (int i = scanContainer.size() - 1; i >= 0; i--) {
                file = new File(scanContainer.get(i).getValue());
                // ----文件不存在，则退出本次循环
                if (!file.exists()) {
                    continue;
                }
                // --- 加载配置文件
                this.loadConfAtomic(file, scanContainer.get(i).isRecursion());
            }
            return this;
        }

        private ConfMgr loadConfAtomic(File file, boolean recursion) {
            if (!file.exists()) {
                return this;
            }
            // ---- 检查改文件是否需要被扫描-检查文件是否配置在了用户的例外集合中
            if (!this.checkScanPath(file)) {
                return this;
            }

            if (file.isDirectory()) {
                File[] subFiles = recursion ? FileUtil.getAllNonDirFileList(file) : FileUtil.getNonDirFileList(file);
                for (File e : subFiles) {
                    loadConfAtomic(e, recursion);
                }
            } else {
                if (file.getName().endsWith(".properties")) {
                    try {
                        Properties prop = PropertiesUtil.load(file.getAbsolutePath());
                        this.allConfigurationsMap.putAll(PropertiesUtil.convertToMap(prop));
                    } catch (IOException ex) {
                        Configurator.log.error("===={} load file error [{}]", Configurator.class.getName(), file.getAbsolutePath());
                        Configurator.log.error(StringUtil.getFullStackTrace(ex));
                    }
                }
            }

            return this;
        }

        private ConfMgr loadConfAtomic(String file, boolean recursion) {
            return this.loadConfAtomic(new File(file), recursion);
        }

        /**
         * <p>
         * <br>---------------------------------------------------------
         * <br> description:
         * <br> 检查文件是否需要被扫描：
         * <br>     - 若文件配置在例外集合中，则不需要被扫描
         * <br>         -- 将文件路径转换为 Unix Style。
         * <br>         -- 遍历 except 集合，比对得出例外结果
         * <br>---------------------------------------------------------
         * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @param file check file
         * @return check result [boolean]
         */
        private boolean checkScanPath(File file) {
            String unixStyleFilePath = FileUtil.getUnixStyleFilePath(file);
            for (String e : this.configurator.effectiveExceptContainer) {
                if (unixStyleFilePath.contains(e)) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkScanPath(String file) {
            return this.checkScanPath(new File(file));
        }


        /**
         * <p>
         * <br>---------------------------------------------------------------
         * <br> description:
         * <br>     * get a series of configuration items with the same prefix
         * <br>     * user can specify multiple keys TODO
         * <br>     * user can specify whether to remove the prefix
         * <br>---------------------------------------------------------------
         * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @param removePrefix whether remove prefix of key
         * @param keyPrefix    prefix of key
         * @return configuration map
         */
        public Map<String, String> get(boolean removePrefix, String keyPrefix) {
            if (!this.specificPrefixConfigurationsCache.containsKey(keyPrefix)) {
                synchronized (this) {
                    if (!this.specificPrefixConfigurationsCache.containsKey(keyPrefix)) {
                        Map<String, String> specificMap = new HashMap<>();
                        String key;
                        for (Map.Entry<String, String> e : this.allConfigurationsMap.entrySet()) {
                            key = e.getKey();
                            if (key.startsWith(keyPrefix)) {
                                key = removePrefix ? key.replace(keyPrefix, "") : key;
                                specificMap.put(key, e.getValue());
                            }
                        }
                    }
                }
            }
            return this.specificPrefixConfigurationsCache.get(keyPrefix);
        }


        public String get(String key) {
            if (!this.containsKey(key)) {
                return "";
            }
            try {
                this.readLock.lock();
                return allConfigurationsMap.get(key);
            } finally {
                this.readLock.unlock();
            }
        }


        public void set(String key, String val) {
            try {
                if (containsKey(key)) {
                    this.writeLock.lock();
                }
                this.allConfigurationsMap.put(key, val);
            } finally {
                if (this.writeLock.isHeldByCurrentThread()) {
                    this.writeLock.unlock();
                }
            }
        }

        /**
         * 获取必须包含项配置----若配置信息为空--抛出异常
         *
         * @param key key
         * @return value
         * @throws Exception
         */
        public String getForcedIncludeConfiguration(String key) throws Exception {
            if (!this.containsKey(key)) {
                throw new Exception("====not find [" + key + "] in configuration");
            }
            String val = this.get(key);
            if (StringUtil.isBlank(val)) {
                throw new Exception("====key [" + key + "] in configuration, but value is blank");
            }
            return val;
        }

        /**
         * return the value of this key，if the value is blank，return the defaultValue
         *
         * @param key        key
         * @param defaultVal default Value
         * @return String
         */
        public String get(String key, String defaultVal) {
            if (!this.containsKey(key)) {
                return defaultVal;
            }
            String val = this.get(key);
            if (StringUtil.isBlank(val)) {
                val = defaultVal;
            }
            return val;
        }

        /**
         * 从几个 key 中获取其中一个配置项并返回
         *
         * @param keys keys
         * @return String
         */
        public String get(String... keys) {
            String value = "";
            for (String e : keys) {
                value = this.get(e);
                if (StringUtil.isNonBlank(value)) {
                    break;
                }
            }
            return value;
        }

        public boolean containsKey(String key) {
            return this.allConfigurationsMap.containsKey(key);
        }

        /**
         * 列出所有资源属性
         */
        public void list() {
            StringBuffer buffer = new StringBuffer();
            try {
                this.readLock.lock();
                for (Map.Entry<String, String> entry : this.allConfigurationsMap.entrySet()) {
                    buffer.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\r\n");
                }
                System.out.println(buffer.toString());
            } finally {
                this.readLock.unlock();
            }
        }

        /**
         * reload
         *
         * @return ConfMgr
         */
        public ConfMgr reload() {
            try {
                this.writeLock.lock();
                // 初始化所有参数，重新读取配置文件
                this.clear().initArgs().loadConf();
                return this;
            } finally {
                if (this.writeLock.isHeldByCurrentThread()) {
                    this.writeLock.unlock();
                }
            }
        }

        public ConfMgr clear() {
            try {
                this.writeLock.lock();
                this.allConfigurationsMap.clear();
                return this;
            } finally {
                if (this.writeLock.isHeldByCurrentThread()) {
                    this.writeLock.unlock();
                }
            }
        }

    }

    /**
     * @author ivybest (ivybestdev@163.com)
     * @version 1.0
     * @description 配置文件描述项
     * @date 2020/2/28 17:20
     */
    private static class ConfPathEntry {
        /**
         * 配置文件路径、支持 directory、File
         */
        private String value;
        /**
         * 是否递归获取配置项，对 directory 有效
         * 若为 true, 则读取 directory 的所有子目录
         */
        private boolean recursion;
        /**
         * 扫描路径的优先级
         * 取值范围 [0-n)，--数字越小优先级越高
         */
        private int priority;
        /**
         * 扫描路径默认优先级
         * 用户未指定 priority 时，按照 defaultPriority 排序
         */
        private int defaultPriority;


        public static ConfPathEntry newInstance(int priority, boolean recursion, String value) {
            return new ConfPathEntry().setValue(value).setRecursion(recursion).setPriority(priority);
        }

        public static ConfPathEntry newInstance() {
            return new ConfPathEntry();
        }


        public String getValue() {
            return value;
        }

        public ConfPathEntry setValue(String value) {
            this.value = value;
            return this;
        }

        public boolean isRecursion() {
            return recursion;
        }

        public ConfPathEntry setRecursion(boolean recursion) {
            this.recursion = recursion;
            return this;
        }

        public int getPriority() {
            return priority;
        }

        public ConfPathEntry setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public int getDefaultPriority() {
            return defaultPriority;
        }

        public ConfPathEntry setDefaultPriority(int defaultPriority) {
            this.defaultPriority = defaultPriority;
            return this;
        }
    }

    /**
     * @description ConfPathEntry Priority Comparator
     * @author ivybest (ivybestdev@163.com)
     * @version 1.0
     * @date 2020/3/4 0:28
     */
    Comparator<ConfPathEntry> confPathEntryPriorityComparator = new Comparator<ConfPathEntry>() {
        @Override
        public int compare(ConfPathEntry arg0, ConfPathEntry arg1) {
            return arg0.getPriority() - arg1.getPriority();
        }
    };

}
