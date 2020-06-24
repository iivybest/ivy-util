package org.ivy.xutil.bean;

import lombok.extern.slf4j.Slf4j;
import org.ivy.util.common.BeanUtil;
import org.ivy.util.common.StringUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * <br> Copyright@2020 www.ivybest.org Inc. All rights reserved.
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/6/9 9:57
 */
@Slf4j
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class BeanUtilSimpleTest {


    @Test
    public void test_02_process() throws Exception {
        MyBean bean = MyBean.newInstance().setAddr("");
        BeanUtil.handleField(bean, String.class, e -> e == null, () -> "test-2");
        log.info("===={}", bean);
    }

    @Test
    public void test_03_process_less_then_8() throws Exception {
        MyBean bean = MyBean.newInstance().setAddr("00");
        BeanUtil.handleField(bean, String.class,
                new BeanUtil.Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return StringUtil.isBlank(s);
                    }
                }, new BeanUtil.Supplier<String>() {
                    @Override
                    public String get() {
                        return "test-03";
                    }
                });
        log.info("===={}", bean);
    }

    @Test
    public void test_04_process_less_then_8() throws Exception {
        MyBean bean = MyBean.newInstance();
        bean.setAddr("00");
        BeanUtil.handleField(bean, String.class,
                new BeanUtil.Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        if (StringUtil.isBlank(s)) {
                            return "internal-04";
                        }
                        return "test-04-" + s;
                    }
                }
        );
        log.info("===={}", bean);
    }

    @Test
    public void test_05_process() throws Exception {
        MyBean bean = MyBean.newInstance().setAddr("00").setName("");

        BeanUtil.handleField(bean, String.class, e -> StringUtil.getNonNull(e, ""));
        log.info("===={}", bean);
    }
}
