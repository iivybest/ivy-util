package org.ivy.util.complex.configurator;

import lombok.extern.slf4j.Slf4j;
import org.ivy.entity.Student;
import org.ivy.util.common.SystemUtil;
import org.ivy.util.complex.configurator.Configurator;
import org.ivy.util.complex.configurator.ExpressionLanguageHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfiguratorSimpleTest {

    private Configurator configurator;

    @Before
    public void setUp() {
        String classpath = SystemUtil.getClasspath();

        ExpressionLanguageHandler handler = ExpressionLanguageHandler.newInstance()
                .set("project", "");


        this.configurator = Configurator.newInstance("test-configurator")
                .configPath(classpath + "root.properties")
                .configPath(true, classpath + "configurator/sub/")
                .configPath(1, classpath + "configurator/priority/f.properties")
                .configPath(2, classpath + "configurator/priority/g.properties")
                .configPath(3, classpath + "configurator/ofd.register.default.properties")
                // ----设置通配符处理器，不设置使用默认的通配符处理器
                .wildcardHandler(null)
                //  ----设置el表达式处理器，不设置使用默认的el表达式处理器
                .elHandler(null)
                .exceptPath(classpath + "configurator/sub/except/")
                .build();
    }

    @Test
    public void test_01_listAllConfigurations() {
        this.configurator.action.list();
    }


    @Test(expected = Exception.class)
    public void test_02_getForcedIncludeConfiguration() throws Exception {
        String key = "one.key.dont.exists.except.throws.exception";
        String value = this.configurator.action.getForcedIncludeConfiguration(key);
        log.debug("{{}: {}}", key, value);
    }

    @Test
    public void test_03_priority() {
        String key = "user.name";
        String value = this.configurator.action.get(key);
        log.debug("{{}: {}}", key, value);
        Assert.assertEquals(value, "f");
    }

    @Test
    public void test_04_wildcard() throws Exception {
        String key = "ofd.register.material.path.el.#";
        key = "root.el.#.test";
        key = "root.el.test.#";
        key = "configurator.el.test.##";
        Set<String> keys = this.configurator.action.getKeySet(key);
        for (String e : keys) {
            log.info("{{}: {}}", e, this.configurator.action.get(e));
        }
    }

    @Test
    public void test_05_explang() throws Exception {
        Student student = new Student();
        student.setName("rashford");
        student.setAddr("London");
        student.setAge(22);

        String key = "configurator.el.test.student.##";
        String value;
        Set<String> keys = this.configurator.action.getForcedIncludeKeySet(key);
        for (String e : keys) {
            value = this.configurator.action.get(e);
            log.debug("{{}: {}}", e, value);
            value = this.configurator.action.getExpLang(e, student);
            log.info("{{}: {}}", e, value);
        }
        log.info("=======================================");
        key = "configurator.el.test.date.##";
        keys = this.configurator.action.getForcedIncludeKeySet(key);
        for (String e : keys) {
            value = this.configurator.action.get(e);
            log.debug("{{}: {}}", e, value);
            value = this.configurator.action.getExpLang(e);
            log.info("{{}: {}}", e, value);
        }
        log.info("=======================================");
        key = "configurator.el.test.static.##";
        keys = this.configurator.action.getForcedIncludeKeySet(key);
        for (String e : keys) {
            value = this.configurator.action.get(e);
            log.debug("{{}: {}}", e, value);
            value = this.configurator.action.getExpLang(e);
            log.info("{{}: {}}", e, value);
        }
        log.info("=======================================");
        key = "configurator.el.test.adv.##";
        keys = this.configurator.action.getForcedIncludeKeySet(key);
        for (String e : keys) {
            value = this.configurator.action.get(e);
            log.debug("{{}: {}}", e, value);
            value = this.configurator.action.getExpLang(e, student);
            log.info("{{}: {}}", e, value);
        }
//        this.configurator.action.getForcedIncludeConfigurationExpLang("this.is.not.exits");
    }


}









