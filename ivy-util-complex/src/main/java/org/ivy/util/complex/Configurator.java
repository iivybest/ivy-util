package org.ivy.util.complex;

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
    private ConfActionMgr confActionMgr;
    /**
     * 配置文件计数器,用于优先级计数，数字越小，优先级越高
     * 用户未指定优先级时，使用该计数器为path设置优先级
     */
    private AtomicInteger priorityCounter;


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


    /**
     * setting config path
     *
     * @param priority  config path priority, the number smaller priority higher
     * @param recursion whether recursive scanning the path
     * @param path      config path
     * @return Configurator this object
     */
    public Configurator configPath(int priority, boolean recursion, String... path) {
        for (String e : path) {
            this.scanContainer.add(ConfPathEntry.newInstance(priority, recursion, e));
        }
        return this;
    }

    /**
     * setting one or multi scanning path(file or directory)
     *
     * @param recursion weather recursion read directory
     * @param path      scanning file paht
     * @return Configurator this object
     */
    public Configurator configPath(boolean recursion, String... path) {
        return configPath(this.priorityCounter.getAndIncrement(), recursion, path);
    }


    public Configurator configPath(int priority, String... path) {
        return this.configPath(priority, Boolean.FALSE, path);
    }

    /**
     * add one or multi scanning path(file or directory)
     * and recursion is false
     *
     * @param path scanning file paht
     * @return Configurator this object
     */
    public Configurator configPath(String... path) {
        return this.configPath(Boolean.FALSE, path);
    }


    /**
     * add one or multi except scanning path(file or directory)
     * and recursion is false
     *
     * @param path scanning file paht
     * @return Configurator this object
     */
    public Configurator exceptPath(String... path) {
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
                // ----初始化 ConfActionMgr -- # ConfActionMgr.init() 返回对象为 ConfActionMgr
                .getConfActionMgr().init();
        return this;
    }

    /**
     * <p>
     * <br>---------------------------------------------------------------
     * <br> description:
     * <br> initialize scanContainer Before operation Build
     * <br>     1、if ScanContainer size is zero，use defaultConfContainer
     * <br>     2、sort all items in ScanContainer
     * <br>---------------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @return Configurator
     */
    public Configurator initializeScanContainerBeforeBuild() {
        if (this.getScanContainer().size() == 0) {
            this.setScanContainer(Configurator.defaultConfContainer);
            return this;
        }

        Collections.sort(this.scanContainer, this.ConfPathEntryPriorityComparator);

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
        this.priorityCounter = new AtomicInteger(10_000_000);
        return this
                .setScanContainer(new ArrayList<>())
                .setExceptContainer(new ArrayList<>())
                .setEffectiveExceptContainer(new ArrayList<>())
                .setConfActionMgr(new ConfActionMgr(this));
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
        return this.confActionMgr.get(removePrefix, keyPrefix);
    }

    public String get(String key) {
        return this.confActionMgr.get(key);
    }


    public void set(String key, String val) {
        this.confActionMgr.set(key, val);
    }

    /**
     * 获取必须包含项配置----若配置信息为空--抛出异常
     *
     * @param key key
     * @return value
     * @throws Exception
     */
    public String getForcedIncludeConfiguration(String key) throws Exception {
        return this.confActionMgr.getForcedIncludeConfiguration(key);
    }

    /**
     * return the value of this key，if the value is blank，return the defaultValue
     *
     * @param key        key
     * @param defaultVal default Value
     * @return String
     */
    public String get(String key, String defaultVal) {
        return this.confActionMgr.get(key, defaultVal);
    }

    /**
     * 从几个 key 中获取其中一个配置项并返回
     *
     * @param keys keys
     * @return String
     */
    public String get(String... keys) {
        return this.confActionMgr.get(keys);
    }

    public boolean containsKey(String key) {
        return this.confActionMgr.containsKey(key);
    }

    /**
     * 列出所有资源属性
     */
    public void list() {
        this.confActionMgr.list();
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

    public ConfActionMgr getConfActionMgr() {
        return confActionMgr;
    }

    public Configurator setConfActionMgr(ConfActionMgr confActionMgr) {
        this.confActionMgr = confActionMgr;
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
    private static class ConfActionMgr {
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

        public ConfActionMgr(Configurator configurator) {
            this.configurator = configurator;
        }

        /**
         * init
         *
         * @return ConfActionMgr
         */
        private ConfActionMgr init() {
            return this.initReadWriteLock().initArgs().loadConfiguration();
        }

        private ConfActionMgr initReadWriteLock() {
            this.lock = new ReentrantReadWriteLock();
            this.readLock = lock.readLock();
            this.writeLock = lock.writeLock();
            return this;
        }

        /**
         * 初始化成员变量
         */
        private ConfActionMgr initArgs() {
            this.allConfigurationsMap = new HashMap<String, String>();
            this.specificPrefixConfigurationsCache = new HashMap<>();
            return this;
        }


        /**
         * 加载 project 配置项
         *
         * <p>
         * <br>---------------------------------------------------------
         * <br> description:
         * <br> load all configurations
         * <br>    1、add system default configuration items.
         * <br>    2、Reverse load configuration items.
         * <br>---------------------------------------------------------
         * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @return ConfActionMgr
         */
        private ConfActionMgr loadConfiguration() {
            // ----将系统参数 classpath 保存到 ConfActionMgr 中。
            this.allConfigurationsMap.put("class.path", SystemUtil.getClasspath());
            this.allConfigurationsMap.put("user.dir", SystemUtil.getUserDir());
            this.allConfigurationsMap.put("os.arch", SystemUtil.getOSArch());
            this.allConfigurationsMap.put("os.name", SystemUtil.getOsName());

            List<ConfPathEntry> scanContainer = this.configurator.scanContainer;

            File file;
            for (int i = scanContainer.size() - 1; i >= 0; i--) {
                file = new File(scanContainer.get(i).getValue());
                if (!file.exists()) {
                    continue;
                }
                // --- 加载配置文件
                this.loadConfAtomic(file, scanContainer.get(i).isRecursion());
            }
            return this;
        }

        /**
         * <p>
         * <br>---------------------------------------------------------
         * <br> description: 加载配置文件原子逻辑
         * <br>    1、
         * <br>
         * <br>---------------------------------------------------------
         * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @param file      configuration file
         * @param recursion whether recursive scanning this path,
         * @return ConfActionMgr this objects
         */
        private ConfActionMgr loadConfAtomic(File file, boolean recursion) {
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

        private ConfActionMgr loadConfAtomic(String file, boolean recursion) {
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
            String prefix = keyPrefix.endsWith(".") ? keyPrefix.substring(0, keyPrefix.length() - 1) : keyPrefix;
            if (!this.specificPrefixConfigurationsCache.containsKey(prefix)) {
                synchronized (this) {
                    if (!this.specificPrefixConfigurationsCache.containsKey(prefix)) {
                        Map<String, String> specificMap = new HashMap<>();
                        String key;
                        for (Map.Entry<String, String> e : this.allConfigurationsMap.entrySet()) {
                            key = e.getKey();
                            if (key.startsWith(prefix)) {
                                key = removePrefix ? key.replace(prefix + ".", "") : key;
                                specificMap.put(key, e.getValue());
                            }
                        }
                        this.specificPrefixConfigurationsCache.put(prefix, specificMap);
                    }
                }
            }
            return this.specificPrefixConfigurationsCache.get(prefix);
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
                if (StringUtil.isNonBlank(value = this.get(e))) {
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
         * @return ConfActionMgr
         */
        public ConfActionMgr reload() {
            try {
                this.writeLock.lock();
                // 初始化所有参数，重新读取配置文件
                this.clear().initArgs().loadConfiguration();
                return this;
            } finally {
                if (this.writeLock.isHeldByCurrentThread()) {
                    this.writeLock.unlock();
                }
            }
        }

        public ConfActionMgr clear() {
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
     * @description 配置文件描述项封装对象
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
    }

    /**
     * @description ConfPathEntry Priority Comparator
     * @author ivybest (ivybestdev@163.com)
     * @version 1.0
     * @date 2020/3/4 0:28
     */
    private Comparator<ConfPathEntry> ConfPathEntryPriorityComparator = new Comparator<ConfPathEntry>() {
        @Override
        public int compare(ConfPathEntry arg0, ConfPathEntry arg1) {
            // ----升序比较设置
            int result = arg0.getPriority() - arg1.getPriority();
            if (result == 0) {
                result = arg0.getValue().compareTo(arg1.getValue());
            }
            return result;
        }
    };

}
