package org.ivy.xutil.bean;

import org.ivy.util.common.StringUtil;

/**
 * <p>
 * <br>---------------------------------------------------------
 * <br>
 * <br>---------------------------------------------------------
 * </p>
 *
 * @author Ivybest (ivybestdev@163.com)
 * @version 1.0
 * @date 2020/6/9 10:00
 */
public class MyBean {

    private String id;
    private String name;
    private String addr;
    private int age;

    public static MyBean newInstance() {
        return new MyBean();
    }


    public String getId() {
        return id;
    }

    public MyBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MyBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddr() {
        return addr;
    }

    public MyBean setAddr(String addr) {
        this.addr = addr;
        return this;
    }

    public int getAge() {
        return age;
    }

    public MyBean setAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return StringUtil.bean2String(this);
    }
}
