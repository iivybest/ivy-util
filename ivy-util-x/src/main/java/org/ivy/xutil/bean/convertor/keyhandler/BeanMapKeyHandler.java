package org.ivy.xutil.bean.convertor.keyhandler;

/**
 * <p>BeanMapKeyHandler</p>
 *
 * @author miao.xl
 * @version 1.0
 * @date 2016年8月3日-上午10:22:48
 */
public interface BeanMapKeyHandler {

    /**
     * 处理 beanmap 中的 key,[由 bean 的 field 映射到 map 的 key]
     *
     * @param key key
     * @return String
     */
    public String handle(String key);

}
