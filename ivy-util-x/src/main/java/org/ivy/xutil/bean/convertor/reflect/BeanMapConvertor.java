package org.ivy.xutil.bean.convertor.reflect;

import org.ivy.xutil.bean.BeanUtil;
import org.ivy.xutil.bean.convertor.keyhandler.BeanMapKeyHandler;
import org.ivy.xutil.bean.convertor.valuehandler.BeanMapValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean Map 转换器
 *
 * @author Ares miao.xl@live.cn
 * @version V1.0
 * @date 2017年3月28日 上午10:40:39
 */
public class BeanMapConvertor {
    private static final Logger LOG = LoggerFactory.getLogger(BeanMapConvertor.class);
    // key 处理器
    private BeanMapKeyHandler keyHandler;
    // value类型处理器
    private Map<String, BeanMapValueHandler> valueHandlers;
    // 需要忽略字段
    private List<String> neglect;

    public BeanMapConvertor() {
        super();
        this.init();
    }

    private void init() {
        this.neglect = new ArrayList<String>();
        this.neglect.add("serialVersionUID");

        this.valueHandlers = new HashMap<String, BeanMapValueHandler>();
    }

    /**
     * setBeanMapKeyHandler
     * 设置Key处理器
     *
     * @param handler BeanMapValueHandler
     * @return BeanMapConvertor
     */
    public BeanMapConvertor setBeanMapKeyHandler(BeanMapKeyHandler handler) {
        this.keyHandler = handler;
        return this;
    }

    /**
     * 添加value类型处理器
     *
     * @param handler BeanMapValueHandler
     * @return BeanMapConvertor
     */
    public BeanMapConvertor addBeanMapValueHandler(BeanMapValueHandler... handler) {
        for (BeanMapValueHandler item : handler) {
            this.valueHandlers.put(item.getClass().getSimpleName(), item);
        }
        return this;
    }


    /**
     * 根据value类型，寻找对应的value类型处理器
     *
     * @param type type
     * @return BeanMapValueHandler
     */
    private BeanMapValueHandler beanMapValueHandlerDispatch(Class<?> type) {
        if (this.valueHandlers.containsKey((type.getSimpleName() + "TypeValueHandler"))) {
            return this.valueHandlers.get((type.getSimpleName() + "TypeValueHandler"));
        } else {
            return null;
        }
    }

    /**
     * 处指定类型value值
     *
     * @param bean  bean
     * @param field field
     * @param <T>   bean's type
     * @return T
     */
    private <T> Object handleValue(T bean, Field field) {
        if (valueHandlers == null
                || valueHandlers.size() <= 0
                || beanMapValueHandlerDispatch(field.getType()) == null) {
            return BeanUtil.getFieldValue(bean, field);
        }

        return beanMapValueHandlerDispatch(field.getType()).handle(bean, field);
    }

    /**
     * handleKey
     *
     * @param key key
     * @return String
     */
    private String handleKey(String key) {
        if (null == this.keyHandler) {
            return key;
        } else {
            return this.keyHandler.handle(key);
        }
    }


    /**
     * convert Bean to Map
     *
     * @param bean 要转化的JavaBean 对象
     * @param <T>  bean's type
     * @return 转化出来的 Map 对象
     */
    public <T> Map<String, Object> convertBean2Map(T bean) {
        if (bean == null) {
            return null;
        }
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Field[] fields = bean.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (!this.neglect.contains(field.getName())) {
                rtnMap.put(this.handleKey(field.getName()), this.handleValue(bean, field));
            }
        }
        return rtnMap;
    }

    /**
     * convertMap2Bean
     * 将一个 Map 对象转化为一个 JavaBean [谨慎使用]
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @param <T>  bean's type
     * @return 转化出来的 JavaBean 对象
     * @throws Exception Exception
     */
    public <T> T convertMap2Bean(Class<T> type, Map<String, ?> map) throws Exception {
        if (map == null) {
            return null;
        }

        T bean = type.newInstance();
        this.convertMap2Bean(bean, map);
        return bean;
    }

    /**
     * convertMap2Bean
     * map转换为bean，[谨慎使用]
     *
     * @param bean bean
     * @param map  map
     * @param <T>  bean's type
     */
    public <T> void convertMap2Bean(T bean, Map<String, ?> map) throws Exception {
        if (map == null) {
            return;
        }
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field f : fields) {
            String key = this.handleKey(f.getName());
            if ((!this.neglect.contains(f.getName())) && map.containsKey(key)) {
                BeanUtil.setFiledValue(bean, f, map.get(key));
//				LOG.debug("----> " + map.get(key));
            }
        }
    }


}
