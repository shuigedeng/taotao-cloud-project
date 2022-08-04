package com.taotao.cloud.sys.biz.api.controller.tools.classloader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;

/**
 * 为每一个类加载器标识一个名称
 */
@Slf4j
public class ExtendClassloader extends URLClassLoader {
    private String name;

    public ExtendClassloader(String name, URL[] urls) {
        super(urls, getSystemClassLoader());
        this.name = name;
    }

    public ExtendClassloader(String name, URL url) {
        super(new URL[]{url}, getSystemClassLoader());
        this.name = name;
    }

    /**
     * 获取当前类加载器加载的类
     * @return
     */
    public Vector<Class<?>> getLoadClasses(){
       return (Vector<Class<?>>)ReflectionUtils.getField(classesField,this);
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public String getName() {
        return name;
    }

    private static final Field classesField;
    static {
        classesField = FieldUtils.getField(ClassLoader.class, "classes", true);
    }
}
