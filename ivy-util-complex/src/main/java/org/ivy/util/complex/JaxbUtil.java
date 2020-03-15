package org.ivy.util.complex;


import org.ivy.util.common.SimpleResourcePooling;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author ivybest (ivybestdev@163.com)
 * @version 1.0
 * @description Jaxb Xml marshal util
 * @date 2020/3/4 20:31
 */
public class JaxbUtil {

    private JAXBContext ctx;
    private SimpleResourcePooling<JaxbHelper> pools;
    private int capacity;
    private static int capacity_default;
    private AtomicInteger idx_counter;
    private static Map<Class<?>, JaxbUtil> typeJaxbUtil;

    static {
        typeJaxbUtil = new HashMap<>(16 * 4);
        capacity_default = 300;
    }

    // ----私有化构造器
    private JaxbUtil() {
    }

    /**
     * @param t
     * @param capacity
     * @return AdvCachedJaxbUtil
     * @throws JAXBException
     * @Title instance
     * @Description 获取带有资源池的JAXB工具类 <br>------------------------------------------
     * 1、第一次调用 instance，会进行 jaxb工具实例化
     * 2、再次调用 instance， 不支持修改 capacity
     * <br>------------------------------------------
     */
    public static <T> JaxbUtil instance(Class<T> t, int capacity) throws JAXBException {
        if (!JaxbUtil.typeJaxbUtil.containsKey(t)) {
            synchronized (JaxbUtil.class) {
                if (!JaxbUtil.typeJaxbUtil.containsKey(t)) {
                    JaxbUtil.typeJaxbUtil.put(t, new JaxbUtil().initialize(t, capacity));
                }
            }
        }
        return JaxbUtil.typeJaxbUtil.get(t);
    }

    public static <T> JaxbUtil instance(Class<T> t) throws JAXBException {
        return instance(t, capacity_default);
    }


    private <T> JaxbUtil initialize(Class<T> t, int capacity) throws JAXBException {
        this.idx_counter = new AtomicInteger(0);
        this.capacity = capacity > 0 ? capacity : capacity_default;
        // ----初始化全局 ctx
        this.ctx = JAXBContext.newInstance(t);
        // ----初始化 JaxbHelper 资源池]
        //this.pools = SimpleResourcePooling.getInstance(this.capacity, () -> generateJaxbHelper(t, ctx, this.idx_counter));
        this.pools = SimpleResourcePooling.getInstance(this.capacity,
                new SimpleResourcePooling.Supplier<JaxbHelper>() {
                    @Override
                    public JaxbHelper get() {
                        JaxbHelper jaxbHelper = generateJaxbHelper(null, ctx, JaxbUtil.this.idx_counter);
                        return jaxbHelper;
                    }
                });
        return this;
    }

    /**
     * @param ctx
     * @return JaxbHelper
     * @throws JAXBException
     * @Title generateJaxbHelper
     * @Description 创建一个 JaxbHelper 实例
     */
    private <T> JaxbHelper generateJaxbHelper(Class<T> t, JAXBContext ctx, AtomicInteger idx) {
        JaxbHelper ins = null;
        try {
            ins = new JaxbHelper("jaxbHelper-[" + ctx.getClass().getName() + "]-" + idx.getAndIncrement(), ctx);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return ins;
    }


    public <T> String marshal(T t) throws JAXBException, XMLStreamException, FactoryConfigurationError {
        return this.marshal(t, true, true, null);
    }

    public <T> String marshal(T t, boolean isFragment) throws JAXBException, XMLStreamException, FactoryConfigurationError {
        return this.marshal(t, isFragment, true, null);
    }

    public <T> String marshal(T t, boolean isFragment, boolean isFormatted) throws JAXBException, XMLStreamException, FactoryConfigurationError {
        return this.marshal(t, isFragment, isFormatted, null);
    }

    public <T> String marshal(T t, boolean isFragment, boolean isFormatted, String schemaLocation) throws JAXBException, XMLStreamException, FactoryConfigurationError {
        JaxbHelper helper = this.pools.get();
        String xml = null;
        try {
            //System.out.println("====>" + helper.getName() + ", freeSource: " + this.pools.getFreeCapcity());
            xml = helper.marshal(t, isFragment, isFormatted, schemaLocation);
        } finally {
            // ----释放资源到资源池
            this.pools.release(helper);
        }
        return xml;
    }

    /**
     * 解组
     *
     * @param msg
     * @return
     * @throws JAXBException
     * @author miao.xl
     * @date 2015年10月29日 下午4:29:10
     * @version 1.0
     */
    public <T> T unmarshal(String msg) throws JAXBException {
        JaxbHelper helper = this.pools.get();
        T t = null;
        try {
            t = helper.unmarshal(msg);
        } finally {
            // ----释放资源到资源池
            this.pools.release(helper);
        }
        return t;
    }

    /**
     *  unmarshal
     *
     * @return T
     * @throws JAXBException
     */
    public <T> T unmarshal(InputStream in) throws JAXBException {
        JaxbHelper helper = this.pools.get();
        T t = null;
        try {
            t = helper.unmarshal(in);
        } finally {
            // ----释放资源到资源池
            this.pools.release(helper);
        }
        return t;
    }


    private static class JaxbHelper {
        private String idx;
        private JAXBContext ctx;
        private Marshaller marshaller;
        private Unmarshaller unmarshaller;

        public JaxbHelper(String idx, JAXBContext ctx) throws JAXBException {
            this.idx = idx;
            this.ctx = ctx;
            this.marshaller = this.ctx.createMarshaller();
            this.unmarshaller = this.ctx.createUnmarshaller();
            this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");    //编码格式
        }

        public String getName() {
            return this.idx;
        }


        public <T> String marshal(T t, boolean isFragment, boolean isFormatted, String schemaLocation) throws JAXBException, XMLStreamException, FactoryConfigurationError {
            Writer writer = new StringWriter();
            // 使用 pkg 生成 ctx，生成 xml 会多一个  xmlns:ns0="http://www.w3.org/2001/XMLSchema-instance" 不影响使用
            // 是否省略xm头声明信息
            this.marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !isFragment);
            // 是否格式化生成的xml串
            this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, isFormatted);
            if (null != schemaLocation && schemaLocation.length() > 0) {
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLocation);
            }
            // 自定义 XmlStreamWriter，处理特殊字符，具体利用RegExp处理
//			XMLStreamWriter writer2 = XMLOutputFactory.newInstance().createXMLStreamWriter(writer);
//			XMLStreamWriter writer3 = new MyXmlStreamWriter(writer2);
            marshaller.marshal(t, writer);
            return writer.toString();
        }

        /**
         * 解组
         *
         * @param msg
         * @return
         * @throws JAXBException
         * @author miao.xl
         * @date 2015年10月29日 下午4:29:10
         * @version 1.0
         */
        @SuppressWarnings("unchecked")
        public <T> T unmarshal(String msg) throws JAXBException {
            return (T) unmarshaller.unmarshal(new StringReader(msg));
        }

        /**
         * @return T
         * @throws JAXBException
         * @Title unmarshal
         * @Description 解组
         */
        @SuppressWarnings("unchecked")
        public <T> T unmarshal(InputStream in) throws JAXBException {
            return (T) unmarshaller.unmarshal(in);
        }

    }


}
