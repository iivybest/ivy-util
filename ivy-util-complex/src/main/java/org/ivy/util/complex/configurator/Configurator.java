package org.ivy.util.complex.configurator;

import org.ivy.util.common.FileUtil;
import org.ivy.util.common.PropertiesUtil;
import org.ivy.util.common.StringUtil;
import org.ivy.util.common.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> requirement:
 * <br> 1、构建过于负责--builer模式（变种）
 * <br> 2、单例模式 (去除单例模式，复杂应用使用 spring 管理 configurator 对象)
 * <br> 3、可定义扫描位置、优先级
 * <br> 4、用户自定义例外文件或位置
 * <br> 5、配置项 key 支持通配符 #，
 * <br>         ofd.register.#.name
 * <br>         ofd.register.activation.#
 * <br> 6、配置项 value 支持 el 表达式, eg: ${classpath} / ${user.name}
 * <br>         eg: ${classpath} / ${user.name}
 * <br> 7、多文件类型支持 --二期实现
 * <br> 8、配置文件修改即时感应 --二期实现
 * <br> 9、用户设置记录 --二期实现
 * <br>---------------------------------------------------------
 * <br> description（logic）:
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
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
     * 有效路径集合。
     */
    private List<String> effectiveConfigContainer;
    /**
     * 配置文件管理器、动作管理器
     */
    public ConfActionMgr action;
    /**
     * wildcard handler
     * handle key
     */
    private WildcardHandler wildcardHandler;
    /**
     * Expression Language Handler
     * handle val
     */
    private ExpressionLanguageHandler elHandler;
    /**
     * 配置文件计数器,用于优先级计数，数字越小，优先级越高
     * 用户未指定优先级时，使用该计数器为path设置优先级
     */
    private AtomicInteger priorityCounter;
    /**
     * 用户设置记录，记录用户做过哪些设置
     * 用户未设置的必须项，使用你默认策略
     * 用户设置值为 1，不设置为 0
     */
    private int customSettingRecord;


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
        // ----检查扫描路径
        this.initializeScanContainerBeforeBuild()
                // ----检查设置例外路径，并设置有效例外路径
                .initializeEffectiveExceptContainerBeforeBuild()
                // ----初始化 ConfActionMgr -- # ConfActionMgr.init() 返回对象为 ConfActionMgr
                .getAction().init();

        // ----若用户未指定通配符处理器，使用默认通配符处理器
        if (this.getWildcardHandler() == null) {
            this.wildcardHandler = WildcardHandler.newInstance(ConfiguratorConstant.WILDCARD);
        }
        // ----若用户未指定el表达式处理器，使用默认 el 表达式处理器
        if(this.getElHandler() == null) {
            // ----使用通配符获取全部的类型别名集合
            Map<String, String> aliasMap = this.action.getAll(Boolean.TRUE, ConfiguratorConstant.WILDCARDS_ALIAS_DYNAMIC);
            // ----静态文本别名
            Map<String, String> staticTextMap = this.action.getAll(Boolean.TRUE, ConfiguratorConstant.WILDCARD_ALIAS_STATIC);
            // ----build expression language handler
            ExpressionLanguageHandler defaultHandler = ExpressionLanguageHandler.newInstance()
                    .set("project", this.action.get("user.dir"))
                    .set("classpath", this.action.get("class.path"))
                    .set(staticTextMap)
                    .setAlias(aliasMap)
                    ;
            this.elHandler = defaultHandler;
        }
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

        this.scanContainer.sort(this.confPathEntryPriorityComparator);

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
        this.customSettingRecord = 0b00_000_000_000_000_000_000_000_000_000_000;
        return this
                .setScanContainer(new ArrayList<>())
                .setExceptContainer(new ArrayList<>())
                .setEffectiveExceptContainer(new ArrayList<>())
                .setEffectiveConfigContainer(new ArrayList<>())
                .setAction(new ConfActionMgr(this));
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

    public List<String> getEffectiveConfigContainer() {
        return effectiveConfigContainer;
    }

    public Configurator setEffectiveConfigContainer(List<String> effectiveConfigContainer) {
        this.effectiveConfigContainer = effectiveConfigContainer;
        return this;
    }

    public ConfActionMgr getAction() {
        return action;
    }

    public Configurator setAction(ConfActionMgr action) {
        this.action = action;
        return this;
    }

    public WildcardHandler getWildcardHandler() {
        return wildcardHandler;
    }

    public Configurator wildcardHandler(WildcardHandler wildcardHandler) {
        this.wildcardHandler = wildcardHandler;
        return this;
    }

    public ExpressionLanguageHandler getElHandler() {
        return elHandler;
    }

    public Configurator elHandler(ExpressionLanguageHandler elHandler) {
        this.elHandler = elHandler;
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
    public static class ConfActionMgr {
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
         * 通配符配置项缓存
         * 用户查询过一次后，计入缓存，方便下一次调用
         */
        private Map<String, Map<String, String>> wildcardConfigCache;


        /**
         * constructor
         *
         * @param configurator configurator
         */
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
            this.wildcardConfigCache = new HashMap<>();
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

            String path;
            File file;
            for (int i = scanContainer.size() - 1; i >= 0; i--) {
                path = scanContainer.get(i).getValue();
                if (path.startsWith("classpath:") || path.startsWith("file:")) {
                    this.loadConfigurationFromClasspath(path);
                    continue;
                }
                file = new File(path);
                if (!file.exists()) {
                    continue;
                }
                // --- 加载配置文件
                this.loadConfAtomic(file, scanContainer.get(i).isRecursion());
            }
            return this;
        }

        /**
         * 基于classpath读取配置文件，不支持目录读取，支持jar内配置文件读取
         *
         * @param path 文件路径
         * @return ConActionMgr this inst
         */
        private ConfActionMgr loadConfigurationFromClasspath(String path) {
            log.debug("{path: {}}", path);
            String dest = FileUtil.getUnixStyleFilePath(path.replace("classpath:", ""));
            InputStream in = Configurator.class.getClassLoader().getResourceAsStream(dest);
            if (null == in) {
                return this;
            }
            String absolutePath = FileUtil.getUnixStyleFilePath(Configurator.class.getClassLoader().getResource(dest).getPath());
            log.debug("{absolutePath: {}}", absolutePath);
            if (null == absolutePath) {
                return this;
            }
            try {
                Properties prop = PropertiesUtil.loadProperties(in);
                this.allConfigurationsMap.putAll(PropertiesUtil.convertToMap(prop));
                // ---- 将有效的配置文件按次序添加到 effectiveConfigContainer 中
                this.configurator.getEffectiveConfigContainer().add(absolutePath);
            } catch (IOException ex) {
                Configurator.log.error("===={} load file error [{}]", Configurator.class.getName(), path);
                Configurator.log.error(StringUtil.getFullStackTrace(ex));
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
                        // ---- 将有效的配置文件按次序添加到 effectiveConfigContainer 中
                        this.configurator.getEffectiveConfigContainer().add(FileUtil.getUnixStyleFilePath(file));
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
         * 根据通配符获取指定 key 值
         *
         * @param wildcard
         * @param items
         * @return
         */
        public String getKey(String wildcard, String ... items) {
            if (StringUtil.isBlank(wildcard)) {
                return wildcard;
            }
            if (! wildcard.contains(ConfiguratorConstant.WILDCARD)) {
                return wildcard;
            }
            String result = wildcard;
            for (String e : items) {
                if (! wildcard.contains(ConfiguratorConstant.WILDCARD)) {
                    break;
                }
                result = result.replaceFirst(ConfiguratorConstant.WILDCARD, e);
            }

            return result;
        }

        /**
         * 获取配置项，配置项使用el表达式处理器进行处理
         *
         * @param key key
         * @return result
         */
        public String getExpLang(String key) throws Exception{
            String value = this.get(key);
            if (StringUtil.isBlank(value)) {
                return value;
            }
            value = this.configurator.getElHandler().handle(value);
            return value;
        }
        public String getForcedIncludeConfigurationExpLang(String key) throws Exception {
            String value = this.getForcedIncludeConfiguration(key);
            value = this.configurator.getElHandler().handle(value);
            return value;
        }

        /**
         * 获取配置项，配置项使用el表达式处理器进行处理，使用pojo进行动态赋值
         *
         * @param key  key
         * @param bean pojo
         * @param <T>  the type of pojo
         * @return result
         * @throws Exception
         */
        public <T> String getExpLang(String key, T bean) throws Exception {
            String value = this.get(key);
            if (StringUtil.isBlank(value)) {
                return value;
            }
            value = this.configurator.getElHandler().handle(value, bean);
            return value;
        }
        public <T> String getForcedIncludeConfigurationExpLang(String key, T bean) throws Exception {
            String value = this.getForcedIncludeConfiguration(key);
            value = this.configurator.getElHandler().handle(value, bean);
            return value;
        }


        /**
         * <p>
         * <br>---------------------------------------------------------------
         * <br> description:
         * <br>     * get a series of configuration items with the same prefix
         * <br>     * wildcard support, [#]
         * <br>     * user can specify multiple keys TODO
         * <br>     * user can specify whether to remove the prefix
         * <br>---------------------------------------------------------------
         * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @param key prefix of key
         * @return configuration map
         */
        public Map<String, String> getAll(String key) {
            // ----double check, make sure this values in cache.
            if (! wildcardConfigCache.containsKey(key)) {
                synchronized (this) {
                    Map<String, String> specificMap;
                    if (! wildcardConfigCache.containsKey(key)) {
                        specificMap = this.configurator.getWildcardHandler().getMatched(key, this.allConfigurationsMap);
                        wildcardConfigCache.put(key, specificMap);
                    }
                }
            }
            return wildcardConfigCache.get(key);
        }

        private Map<String, String> getAll(boolean removePrefix, String key) {
            Map<String, String> result = this.getAll(key);
            if (removePrefix) {
                Map<String, String> iResult = new HashMap<>(result.size());
                String prefix = key.replace(ConfiguratorConstant.WILDCARD, "");
//                prefix = (prefix.endsWith(".")) ? prefix.substring(0, prefix.length() - 1) : prefix;
                String iKey;
                for(Map.Entry<String,String> e : result.entrySet()) {
                    iKey = e.getKey().replace(prefix, "");
                    iResult.put(iKey, e.getValue());
                }
                result.clear();
                result.putAll(iResult);
            }
            return result;
        }


        public Map<String, String> getAllForcedIncludeConfiguration(String key) throws Exception {
            Map<String, String> result = this.getAll(key);
            if (result == null || result.entrySet().size() == 0) {
                throw new Exception("====not find any key matches [" + key + "] in configuration, these keys are required");
            }
            return result;
        }

        public Set<String> getKeySet(String keyPrefix) {
            Map<String, String> kv = this.getAll(keyPrefix);
            return kv.keySet();
        }

        /**
         * <p>
         * <br>---------------------------------------------------------
         * <br> description:
         * <br> 获取配置必须包含项的key值集合
         * <br>---------------------------------------------------------
         * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
         * </p>
         *
         * @param key key
         * @return a set of key
         * @throws Exception
         */
        public Set<String> getForcedIncludeKeySet(String key) throws Exception {
            Map<String, String> kv = this.getAllForcedIncludeConfiguration(key);
            return kv.keySet();
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
                throw new Exception("====not find [" + key + "] in configuration, this key is required");
            }
            String val = this.get(key);
            if (StringUtil.isBlank(val)) {
                throw new Exception("====key [" + key + "] in configuration, but value is blank, this key is required");
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
            StringBuilder buffer = new StringBuilder();
            try {
                this.readLock.lock();
                for (Map.Entry<String, String> entry : this.allConfigurationsMap.entrySet()) {
                    buffer.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\r\n");
                }
                log.debug("\r\n{}", buffer.toString());
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
    private Comparator<ConfPathEntry> confPathEntryPriorityComparator = new Comparator<ConfPathEntry>() {
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
