package org.ivy.util.complex.configurator;

import lombok.extern.slf4j.Slf4j;
import org.ivy.entity.Student;
import org.ivy.util.common.SystemUtil;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> description:
 * <br> Configurator test case
 * <br>
 * <br>
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description Configurator test case
 * @date 2020/3/3 9:41
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class ConfiguratorSimpleTest {

    private static final String CLASSPATH = SystemUtil.getClasspath();

    private static Configurator CONF;

    @BeforeAll
    public static void setUp() {

        ExpressionLanguageHandler handler = ExpressionLanguageHandler.newInstance()
                .set("project", "");


        CONF = Configurator.newInstance("test-configurator")
                .configPath(CLASSPATH + "root.properties")
                .configPath(true, CLASSPATH + "configurator/sub/")
                .configPath(1, CLASSPATH + "configurator/priority/f.properties")
                .configPath(2, CLASSPATH + "configurator/priority/g.properties")
                .configPath(3, CLASSPATH + "configurator/ofd.register.default.properties")
                // ----设置通配符处理器，不设置使用默认的通配符处理器
                .wildcardHandler(null)
                //  ----设置el表达式处理器，不设置使用默认的el表达式处理器
                .elHandler(null)
                .exceptPath(CLASSPATH + "configurator/sub/except/")
                .build(ConfiguratorSimpleTest.class);
    }

    @Test
    public void test_01_listAllConfigurations() {
        this.CONF.action.list();
    }


    @Test
    public void test_02_getForcedIncludeConfiguration() throws Exception {
        String key = "one.key.dont.exists.except.throws.exception";
        String value = this.CONF.action.getForcedIncludeConfiguration(key);
        log.debug("{{}: {}}", key, value);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "configurator.el.test.static.##",
            "configurator.el.test.static.#",
            "configurator.el.test.student.##",
            "configurator.el.test.student.#",
            "configurator.el.test.date.##",
            "configurator.el.test.date.#",
            "configurator.el.test.adv.#"
    })
    public void test_05_explang(String key) throws Exception {
        Student student = new Student();
        student.setName("Rashford");
        student.setAddr("Manchester");
        student.setAge(20);

        Set<String> keys = this.CONF.action.getForcedIncludeKeySet(key);
        String value;
        for (String e : keys) {
            value = this.CONF.action.get(e);
            log.debug("========{{}: {}}", e, value);
            value = this.CONF.action.getExpLang(e, student);
            log.info("~~~~~~~~{{}: {}}", e, value);
        }
        log.info("=======================================");
        Map<String, String> confs = CONF.action.getAllForcedIncludeConfiguration(true, key);
        String eKey, eVal;
        for (Map.Entry<String, String> e : confs.entrySet()) {
            eKey = e.getKey();
            eVal = e.getValue();
            log.debug("========{{}: {}}", eKey, eVal);
            eVal = this.CONF.getElHandler().handle(eVal, student);
            log.info("~~~~~~~~{{}: {}}", eKey, eVal);
        }


//        this.configurator.action.getForcedIncludeConfigurationExpLang("this.is.not.exits");
    }

    @Test
    public void test_03_priority() {
        String key = "user.name";
        String value = this.CONF.action.get(key);
        log.debug("{{}: {}}", key, value);
        Assert.assertEquals(value, "f");
    }

    @Test
    public void test_04_wildcard() throws Exception {
        String key = "ofd.register.material.path.el.#";
        key = "root.el.#.test";
        key = "root.el.test.#";
        key = "configurator.el.test.##";
        Set<String> keys = this.CONF.action.getKeySet(key);
        for (String e : keys) {
            log.info("{{}: {}}", e, this.CONF.action.get(e));
        }
    }


}









