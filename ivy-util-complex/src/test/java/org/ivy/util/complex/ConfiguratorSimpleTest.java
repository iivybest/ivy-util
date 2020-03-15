package org.ivy.util.complex;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.SystemUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Map;

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
        this.configurator = Configurator.newInstance("test-configurator")
                .configPath(classpath + "root.properties")
                .configPath(true, classpath + "configurator/sub/")
                .configPath(2, classpath +"configurator/priority/f.properties")
                .configPath(1, classpath +"configurator/priority/g.properties")
                .exceptPath(classpath + "configurator/sub/except/")
                .build();
    }

    @Test
    public void test_01_listAllConfigurations() {
        this.configurator.list();
    }


    @Test(expected = Exception.class)
    public void test_02_getForcedIncludeConfiguration() throws Exception {
        String key = "one.key.dont.exists.except.throws.exception";
        String value = this.configurator.getForcedIncludeConfiguration(key);
        log.debug("{{}: {}}", key, value);
    }


    @Test
    public void test_03_getASeriasCconfig() {
        String prefix = "page.style";
        Map<String, String> styleCconfig = this.configurator.get(false, prefix);
        for(Map.Entry<String, String> e: styleCconfig.entrySet()) {
            log.info("{{}: {}}", e.getKey(), e.getValue());
        }
    }


    @Test
    public void test_04_priority() {
        String key = "user.name";
        String value = this.configurator.get(key);
        log.debug("{{}: {}}", key, value);
    }




}
