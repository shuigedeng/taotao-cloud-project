package com.taotao.cloud.sys.biz.api.controller.tools.quartz.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.simpl.CascadingClassLoadHelper;
import org.quartz.spi.ClassLoadHelper;
import org.springframework.cglib.core.ReflectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;

@Slf4j
public class CascadingClassLoadHelperExtend extends CascadingClassLoadHelper {
    @Override
    public void initialize() {
        super.initialize();

        // 反射  loadHelpers 添加我的的类加载器
        Field loadHelpers = FieldUtils.getDeclaredField(CascadingClassLoadHelper.class, "loadHelpers", true);
        try {
            LinkedList<ClassLoadHelper> classLoadHelpers = (LinkedList<ClassLoadHelper>) loadHelpers.get(this);
            classLoadHelpers.add(new ClassLoaderHelperCustom());
        } catch (IllegalAccessException e) {log.error(e.getMessage(),e);}
    }

    static class ClassLoaderHelperCustom implements ClassLoadHelper{
        public static ThreadLocal<ClassLoader> classLoaderThreadLocal = new ThreadLocal<>();

        @Override
        public void initialize() {

        }

        @Override
        public Class<?> loadClass(String name) throws ClassNotFoundException {
            ClassLoader classLoader = classLoaderThreadLocal.get();
            return classLoader.loadClass(name);
        }

        @Override
        public <T> Class<? extends T> loadClass(String name, Class<T> clazz) throws ClassNotFoundException {
            return (Class<? extends T>) loadClass(name);
        }

        @Override
        public URL getResource(String name) {
            ClassLoader classLoader = classLoaderThreadLocal.get();
            return classLoader.getResource(name);
        }

        @Override
        public InputStream getResourceAsStream(String name) {
            return classLoaderThreadLocal.get().getResourceAsStream(name);
        }

        @Override
        public ClassLoader getClassLoader() {
            return classLoaderThreadLocal.get();
        }
    }
}
