package com.taotao.cloud.common.support.instance.impl;


import com.taotao.cloud.common.constant.PunctuationConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.support.instance.Instance;
import com.taotao.cloud.common.utils.common.ArgUtil;
import com.taotao.cloud.common.utils.lang.ObjectUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例化工厂类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:10:28
 */
public final class InstanceFactory implements Instance {

    private InstanceFactory(){}

    /**
     * 单例 map 对象
     * 1. key 是 class 的全称
     */
    private final Map<String, Object> singletonMap = new ConcurrentHashMap<>();

    /**
     * 线程内的 map 对象
     */
    private ThreadLocal<Map<String, Object>> mapThreadLocal = new ThreadLocal<>();

    /**
     * 静态内部类实现单例
     */
    private static class SingletonHolder {
        private static final InstanceFactory INSTANCE_FACTORY = new InstanceFactory();
    }

    /**
     * 获取单例对象
     * @return 实例化对象
     */
    public static InstanceFactory getInstance() {
        return SingletonHolder.INSTANCE_FACTORY;
    }

    /**
     * 静态方法单例
     * @param tClass 类信息
     * @param <T> 泛型
     * @return 结果
     */
    public static <T> T singletion(Class<T> tClass) {
        return getInstance().singleton(tClass);
    }

    /**
     * 静态方法单例
     * @param tClass 类信息
     * @param groupName 分组名称
     * @param <T> 泛型
     * @return 结果
     */
    public static <T> T singletion(Class<T> tClass, final String groupName) {
        return getInstance().singleton(tClass, groupName);
    }


    @Override
    public <T> T singleton(Class<T> tClass, String groupName) {
        return getSingleton(tClass, groupName, singletonMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T singleton(Class<T> tClass) {
        this.notNull(tClass);

        return this.getSingleton(tClass, singletonMap);
    }

    @Override
    public <T> T threadLocal(Class<T> tClass) {
        this.notNull(tClass);

        //1. 校验 map 是否存在
        Map<String, Object> map = mapThreadLocal.get();
        if(ObjectUtil.isNull(map)) {
            map = new ConcurrentHashMap<>();
        }

        //2. 获取对象
        T instance = this.getSingleton(tClass, map);

        //3. 更新 threadLocal
        mapThreadLocal.set(map);

        return instance;
    }

    @Override
    public <T> T multiple(Class<T> tClass) {
        this.notNull(tClass);

        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new CommonRuntimeException(e);
        } catch (InvocationTargetException e) {
	        e.printStackTrace();
        }
	    throw new CommonRuntimeException("类型异常");
    }

    @Override
    public <T> T threadSafe(Class<T> tClass) {
        //if(tClass.isAnnotationPresent(ThreadSafe.class)) {
        //    return this.singleton(tClass);
        //}
        //return this.multiple(tClass);

	    return this.singleton(tClass);
    }

    /**
     * 获取单例对象
     * @param tClass class 类型
     * @param instanceMap 实例化对象 map
     * @return 单例对象
     */
    @SuppressWarnings("unchecked")
    private <T> T getSingleton(final Class<T> tClass, final Map<String, Object> instanceMap) {
        this.notNull(tClass);

        final String fullClassName = tClass.getName();
        T instance = (T) instanceMap.get(fullClassName);
        if(ObjectUtil.isNull(instance)) {
            instance = this.multiple(tClass);
            instanceMap.put(fullClassName, instance);
        }
        return instance;
    }

    /**
     * 获取单例对象
     * @param tClass 查询 tClass
     * @param group 分组信息
     * @param instanceMap 实例化对象 map
     * @return 单例对象
     */
    @SuppressWarnings("unchecked")
    private <T> T getSingleton(final Class<T> tClass,
                               final String group, final Map<String, Object> instanceMap) {
        this.notNull(tClass);
        ArgUtil.notEmpty(group, "key");

        final String fullClassName = tClass.getName()+ PunctuationConst.MIDDLE_LINE+group;
        T instance = (T) instanceMap.get(fullClassName);
        if(ObjectUtil.isNull(instance)) {
            instance = this.multiple(tClass);
            instanceMap.put(fullClassName, instance);
        }
        return instance;
    }

    /**
     * 断言参数不可为 null
     * @param tClass class 信息
     */
    private void notNull(final Class tClass) {
        ArgUtil.notNull(tClass, "class");
    }

}
