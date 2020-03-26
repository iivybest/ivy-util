package org.ivy.util.complex.configurator;

import org.ivy.util.common.RegExpUtil;
import org.ivy.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br> expression language protocol
 * <br>
 * <br> ${author}
 * <br> ${classpath}
 * <br> ${project}
 * <br> ${user.name}
 * <br> ${user.getName()}
 * <br> ${date.currentDatetime()}
 * <br> ${date.currentDatetime(yyyy-MM-dd HH:mm:ss)}
 * <br>
 * <br> schema: ${type.signature(params)}
 * <br>---------------------------------------------------------
 * <br> description:
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br>
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/3/19 15:29
 */
public class ExpressionLanguageHandler {
    private static Logger log = LoggerFactory.getLogger(ExpressionLanguageHandler.class);


    private static String prefix;

    private static Pattern pattern;
    /**
     * static text obj mapping set
     */
    private Map<String, String> staticTextMapping;
    /**
     * java pojo type alias set
     */
    private Map<String, String> aliasMapping;


    static {
        pattern = Pattern.compile("\\$\\{[\\S|( )&&[^\\{\\}]]*\\}");
        prefix = "configurator.el.alias.";
        prefix = "";
    }

    {
        this.staticTextMapping = new HashMap<>();
        this.aliasMapping = new HashMap<>();
    }

    /**
     * get ExpressionLanguageHandler instance
     *
     * @return ExpressionLanguageHandler instance
     */
    public static ExpressionLanguageHandler newInstance() {
        return new ExpressionLanguageHandler();
    }


    public ExpressionLanguageHandler set(String key, String val) {
        this.staticTextMapping.put(key, val);
        return this;
    }

    public ExpressionLanguageHandler set(Map<String, String> set) {
        this.staticTextMapping.putAll(set);
        return this;
    }

    public ExpressionLanguageHandler setAlias(String alias, String type) {
        this.aliasMapping.put(alias, type);
        return this;
    }

    public ExpressionLanguageHandler setAlias(Map<String, String> set) {
        this.aliasMapping.putAll(set);
        return this;
    }


    /**
     * el 表达式处理
     *
     * @param message
     * @return result
     */
    public String handle(String message) throws Exception {
        if (StringUtil.isBlank(message)) {
            return message;
        }
        String result = message;
        List<String> expressions = RegExpUtil.match(message, pattern);
        String[] protocol;
        outter:
        for (String expression : expressions) {
            protocol = this.explainExpression(expression);
            // ----blank expression ${}
            if (StringUtil.isBlank(protocol[1])) {
                throw new Exception("====expression [" + message + "] pattern illegal");
            }
            // ----static text expression----${classpath}
            if (StringUtil.isBlank(protocol[2]) && StringUtil.isBlank(protocol[3])) {
                result = this.handleStaticText(result, protocol);
                continue outter;
            }
            // ----field expression----${user.name}
            if (StringUtil.isNonBlank(protocol[2]) && StringUtil.isBlank(protocol[3])) {
                throw new Exception("====expression [" + message + "] explain need pojo object");
            }
            // ----signature expression----${data.currentDate(yyyy-MM-dd)}
            // ----static function
            if (StringUtil.isBlank(protocol[2]) && StringUtil.isNonBlank(protocol[3])) {
                result = this.handleDynamicTextBySignature(result, protocol, null);
                continue outter;
            }
        }
        return result;
    }

    /**
     * el 表达式处理 动态语义处理
     *
     * @param message message
     * @param bean    bean
     * @param <T>     the type of bean
     * @return result
     */
    public <T> String handle(String message, T bean) throws Exception {
        if (StringUtil.isBlank(message)) {
            return message;
        }
        if (null == bean) {
            return handle(message);
        }
        String result = message;
        List<String> expressions = RegExpUtil.match(message, pattern);
        String[] protocol;
        position_loop:
        for (String expression : expressions) {
            protocol = this.explainExpression(expression);
            // ----blank expression ${}
            if (StringUtil.isBlank(protocol[1])) {
                throw new Exception("====expression [" + message + "] pattern illegal");
            }
            // ----static text expression----${classpath}
            if (StringUtil.isBlank(protocol[2]) && StringUtil.isBlank(protocol[3])) {
                result = this.handleStaticText(result, protocol);
                continue position_loop;
            }
            // ----field expression----${user.name}
            if (StringUtil.isNonBlank(protocol[2]) && StringUtil.isBlank(protocol[3])) {
                result = this.handleDynamicTextByPojoField(result, protocol, bean);
                continue position_loop;
            }
            // ----signature expression----${data.currentdate(yyyy-MM-dd)}
            // ----instance function
            if (StringUtil.isBlank(protocol[2]) && StringUtil.isNonBlank(protocol[3])) {
                result = this.handleDynamicTextBySignature(result, protocol, bean);
                continue position_loop;
            }
        }
        return result;
    }


    /**
     * handle expression language in configuration
     *
     * @param arg0 message
     * @return result
     */
    private String handleStaticText(String arg0, String[] protocol) {
        String expression = protocol[1];
        String result = arg0;
        if (!staticTextMapping.containsKey(expression)) {
            log.info("===={Missing [{}] mapping configuration}", expression);
            return result;
        }
        result = result.replaceFirst("\\$\\{" + expression + "\\}", staticTextMapping.get(expression));
        return result;
    }


    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description: handle expression language
     * <br>     1、需提前配置类型别名集合，
     * <br>     2、推荐使用
     * <br>---------------------------------------------------------
     * </p>
     *
     * @param message message
     * @param bean    bean
     * @param <T>     the type of bean
     * @return String
     */
    private <T> String handleDynamicTextBySignature(String message, String[] protocol, T bean) throws Exception {
        String result = message;
        String alias = protocol[1];
        if (!aliasMapping.containsKey(prefix + alias)) {
            log.info("===={Missing alias [{}] mapping configuration}", alias);
            return result;
        }
        String type = aliasMapping.get(prefix + alias);
        String signature = protocol[3];
        Object[] params = StringUtil.isBlank(protocol[4]) ? null : new Object[]{protocol[4]};
        String expressionValue = this.getValueByMethodReflectInvoke(Class.forName(type), bean, signature, params).toString();
        String regex = "\\$\\{" + protocol[1] + "\\." + protocol[3] + "\\(" + protocol[4] + "\\)" + "\\}";
        result = result.replaceFirst(regex, expressionValue);
        return result;
    }

    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description: handle expression language
     * <br>     1、需提前配置类型别名集合，
     * <br>     2、推荐使用
     * <br>---------------------------------------------------------
     * </p>
     *
     * @param message message
     * @param bean    bean
     * @param <T>     the type of bean
     * @return String
     */
    private <T> String handleDynamicTextByPojoField(String message, String[] protocol, T bean) throws Exception {
        String result = message;
        String alias = protocol[1];
        if (!aliasMapping.containsKey(prefix + alias)) {
            log.info("===={Missing alias [{}] mapping configuration}", alias);
            return result;
        }
        String type = aliasMapping.get(prefix + alias);
        String field = protocol[2];
        String expressionVal = this.getFieldValueByGetter(field, Class.forName(type), bean).toString();
        String regex = "\\$\\{" + protocol[1] + "\\." + protocol[2] + "\\}";
        result = result.replaceFirst(regex, expressionVal);
        return result;
    }


    /**
     * 通过 Getter 获取 field 值
     *
     * @param field field
     * @param type  the type of bean
     * @param bean  pojo
     * @param <T>   the type of bean
     * @return result
     * @throws Exception
     */
    private <T> Object getFieldValueByGetter(String field, Class<?> type, T bean) throws Exception {
        String signature = "get" + StringUtil.firstCharUppercase(field);

//        Method method = type.getMethod(signature, null);
//        return method.invoke(bean, null);

        return getValueByMethodReflectInvoke(type, bean, signature, null);

    }


    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description：利用反射调用方法获取结果
     * <br>
     * <br>---------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param signature function signature
     * @param type      the type of bean
     * @param bean      pojo or util class
     * @param params    function params
     * @param <T>       the type of bean
     * @return result
     * @throws Exception
     */
    private <T> Object getValueByMethodReflectInvoke(Class<?> type, T bean, String signature, Object[] params) throws Exception {
        // ---- 方法获取异常，改为下面动态判断获取 method
//        Method method = type.getMethod(signature, ((params == null) ? null : String.class));
//        Object result = method.invoke(bean, params);

        Method method = params == null
                ? type.getMethod(signature)
                : type.getMethod(signature, String.class);
        Object result = params == null
                ? method.invoke(bean)
                : method.invoke(bean, params);
        return result;
    }


    /**
     * <p>
     * <br>---------------------------------------------------------
     * <br> description: process expression
     * <br> result schema: {expression, alias, field, signature, params}
     * <br> alias
     * <br> alias.field
     * <br> alias.signature()
     * <br> alias.signature(params)
     * <br>---------------------------------------------------------
     * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
     * </p>
     *
     * @param expression expression
     * @return result of String array
     * @throws Exception
     */
    public String[] explainExpression(String expression) throws Exception {
        String[] protocol = new String[5];
        protocol[0] = expression;

        String iExpression = expression.substring(2, expression.length() - 1);

        // ----static text mapping----${classpath}
        if (! iExpression.contains(".")) {
            protocol[1] = iExpression;
            return protocol;
        }

        // ----pojo field mapping----${user.name}
        protocol[1] = iExpression.split("\\.")[0];
        protocol[2] = iExpression.substring(protocol[1].length() + 1);
        if (!protocol[2].contains("(")) {
            return protocol;
        }

        // ----pojo signature mappin----${user.getName()}/${date.currentDate(yyyy-MM-dd)}
        int idxBracketL = protocol[2].indexOf("(");
        int idxBracketR = protocol[2].indexOf(")");
        if (idxBracketR < 0) {
            throw new Exception("==== the pattern of expression: [" + expression + "] is wrong");
        }
        protocol[4] = protocol[2].substring(idxBracketL + 1, idxBracketR);
        protocol[3] = protocol[2].substring(0, idxBracketL);
        protocol[2] = null;

        return protocol;
    }


}
