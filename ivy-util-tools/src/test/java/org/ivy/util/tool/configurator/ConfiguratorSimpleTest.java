package org.ivy.util.tool.configurator;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.StringUtil;
import org.ivy.util.common.SystemUtil;
import org.ivy.util.tool.Configurator;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
                .addConfPath(classpath + "root.properties")
                .addConfPath(true, classpath + "configurator/")
                .addExpectPath(classpath + "configurator/except/")
                .build();
    }

    @Test
    public void test_01_listAllConfigurations() {
        this.configurator.list();
    }

    @Test
    public void test_02_getForcedIncludeConfiguration() {
        String key = "h.name";
        try {
            String value = this.configurator.getForcedIncludeConfiguration(key);
            log.debug("{{}: {}}", key,value);
        } catch (Exception e) {
            log.error(StringUtil.getFullStackTrace(e));
        }
    }








}
