package org.ivy.util.complex.configurator;


import org.ivy.util.common.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> description: 通配符处理器
 * <br>
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/3/20 20:18
 */
public class WildcardHandler {

    public static final String REGEXP_WILDCARD = "[\\S&&[^\\.]]*";
    public static final String DEFAULT_WILDCARD = ConfiguratorConstant.WILDCARD;

    private String wildcard;

    private WildcardHandler(String wildcard) {
        super();
        this.wildcard = wildcard;
    }


    public static WildcardHandler newInstance(String wildcard) {
        return new WildcardHandler(wildcard);
    }

    public static WildcardHandler newInstance() {
        return WildcardHandler.newInstance(DEFAULT_WILDCARD);
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
    public Map<String, String> getMatched(String key, Map<String, String> mapping) {
        Map<String, String> matched = new HashMap<>();

        if (StringUtil.isBlank(key)) {
            return matched;
        }
        if (!key.contains(this.wildcard)) {
            if (mapping.containsKey(key)) {
                matched.put(key, mapping.get(key));
            }
            return matched;
        }

        String regex = this.processKeyRegExp(key);
        for (Map.Entry<String, String> e : mapping.entrySet()) {
            if (e.getKey().matches(regex)) {
                matched.put(e.getKey(), e.getValue());
            }
        }
        return matched;
    }

    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description: process wildcard
     * <br>     # multistageWildcard
     * <br>     # singleStageWildcard
     * <br>
     * <br>---------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param key
     * @return
     */
    private String processKeyRegExp(String key) {
        String regex = "^" + key;
        String multistageWildcard = this.wildcard + this.wildcard;
        if (key.endsWith(multistageWildcard)) {
            regex = key.substring(0, key.length() - 2) + "[\\S]*";
        }
        regex = regex.replace(this.wildcard, REGEXP_WILDCARD) + "$";
        return regex;
    }

}
