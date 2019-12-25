package org.ivy.xutil.scan;

import java.util.Map;

/**
 * <p>  classname: PropertiesScanner
 * <br> description: Properties Scanner
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2019 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @date 2015/7/10 8:44
 * @version 1.0
 */
public interface PropertiesScanner {

    /**
     *  scan all properties
     *
     * @return Map
     */
    public Map<String, String> scan();

    /**
     * Incrementally update
     *
     * @return Map
     */
    public Map<String, String> update();

}
