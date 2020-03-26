package org.ivy.util.complex.configurator;

/**
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description configurator constant
 * @date 2020/3/23 10:07
 */
public interface ConfiguratorConstant {
    /**
     * wildcard expression
     */
    String WILDCARD = "#";
    /**
     * static text alias wildcard
     */
    String WILDCARD_ALIAS_STATIC = "configurator.el.alias.static.##";
    /**
     * pojo [object / class] alias wildcard
     */
    String WILDCARDS_ALIAS_DYNAMIC = "configurator.el.alias.pojo.#";

}
