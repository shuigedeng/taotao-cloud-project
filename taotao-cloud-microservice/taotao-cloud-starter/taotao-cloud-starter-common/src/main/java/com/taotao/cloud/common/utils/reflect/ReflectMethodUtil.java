/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.utils.reflect;


import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.MethodConst;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.support.handler.IHandler;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;

import com.taotao.cloud.common.utils.lang.ObjectUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * 反射方法工具类
 */
public final class ReflectMethodUtil {

    private ReflectMethodUtil() {}

    /**
     * 忽略的方法名称列表
     * （1）object 默认方法
     * （2）class 默认方法
     *
     * 可优化方案：
     * 将所有方法写死，放在列表中、
     * 缺点：占地方，无法动态更新。
     */
    private static final List<String> IGNORE_METHOD_LIST;

    static {
        Set<String> methodNameSet = new HashSet<>(64);
        for(Method method : Object.class.getMethods()) {
            methodNameSet.add(method.getName());
        }
        for(Method method : Class.class.getMethods()) {
            methodNameSet.add(method.getName());
        }
        IGNORE_METHOD_LIST = new ArrayList<>(methodNameSet);
    }

    /**
     * 获取忽略的方法列表
     * @return 忽略方法名称列表
     */
    public static List<String> getIgnoreMethodList() {
        return IGNORE_METHOD_LIST;
    }

    /**
     * 是否为应该忽略的方法名称
     * @param methodName 方法名称
     * @return 是否
     */
    public static boolean isIgnoreMethod(final String methodName) {
        return getIgnoreMethodList().contains(methodName);
    }

    /**
     * 获取方法类型的名称
     * @param method 方法反射信息
     * @return 参数列表
     */
    public static List<String> getParamTypeNames(final Method method) {
        ArgUtil.notNull(method, "method");

        Class<?>[] paramTypes = method.getParameterTypes();

        return ArrayUtil.toList(paramTypes, new IHandler<Class<?>, String>() {
            @Override
            public String handle(Class<?> aClass) {
                return aClass.getName();
            }
        });
    }

    /**
     * 获取参数名称
     *
     * https://blog.csdn.net/revitalizing/article/details/71036970
     *
     * （1）如果没有指定对应的注解信息，则直接返回 argIndex
     * 比如：arg0 arg1
     * @param method 方法信息
     * @return 方法名称列表
     * @see Param 参数注解
     */
    public static List<String> getParamNames(final Method method) {
        ArgUtil.notNull(method, "method");

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        return getParamNames(parameterAnnotations);
    }

    /**
     * 获取参数名称列表
     * @param parameterAnnotations 参数注解
     * @return 名称列表
     */
    public static List<String> getParamNames(final Annotation[][] parameterAnnotations) {
        if(ArrayUtil.isEmpty(parameterAnnotations)) {
            return Collections.emptyList();
        }

        final int paramSize = parameterAnnotations.length;
        List<String> resultList = Lists.newArrayList();
        for(int i = 0; i < paramSize; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            String paramName = getParamName(i, annotations);
            resultList.add(paramName);
        }

        return resultList;
    }

    /**
     * 获取参数名称
     * @param index 参数的下标
     * @param annotations 注解信息
     * @return 参数名称
     */
    private static String getParamName(final int index, final Annotation[] annotations) {
        final String defaultName = "arg"+index;
        if(ArrayUtil.isEmpty(annotations)) {
            return defaultName;
        }

        for(Annotation annotation : annotations) {
            //if(annotation.annotationType().equals(Param.class)) {
            //    Param param = (Param)annotation;
            //    return param.value();
            //}
        }

        return defaultName;
    }

    /**
     * 获取方法返回值的泛型
     * @param method 方法
     * @param index 泛型的下标
     * @return 返回类型的泛型
     */
    public static Class getReturnGenericType(final Method method, final int index) {
        Type returnType = method.getGenericReturnType();
        if(returnType instanceof ParameterizedType){
            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();
            return (Class) typeArguments[index];
        }
        return null;
    }

    /**
     * 获取参数的泛型
     * @param method 方法
     * @param paramIndex 方法的下标
     * @param genericIndex 泛型的下标
     * @return 对应的类型
     */
    public static Class getParamGenericType(final Method method,
                                            final int paramIndex,
                                            final int genericIndex) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Type genericParameterType = genericParameterTypes[paramIndex];
        if(genericParameterType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericParameterType;
            Type[] parameterArgTypes = aType.getActualTypeArguments();
            return (Class) parameterArgTypes[genericIndex];
        }
        return null;
    }

    /**
     * 获取指定注解的方法
     * @param tClass 类信息
     * @param annotationClass 注解信息
     * @return 方法的 optional 信息
     */
    public static Optional<Method> getMethodOptional(final Class tClass, final Class<? extends Annotation> annotationClass) {
        final Method[] methods = tClass.getMethods();

        if(ArrayUtil.isEmpty(methods)) {
            return Optional.empty();
        }
        for(Method method : methods) {
            if(method.isAnnotationPresent(annotationClass)) {
                return Optional.of(method);
            }
        }
        return Optional.empty();
    }

    /**
     * 执行反射调用
     * @param instance 对象实例，为空的时候针对 static 方法
     * @param method 方法实例
     * @param args 参数信息
     * @return 调用结果
     */
    public static Object invoke(final Object instance, final Method method, Object... args) {
        ArgUtil.notNull(method, "method");

        try {
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 执行反射调用
     * @param instance 对象实例，为空的时候针对 static 方法
     * @param methodName 方法名称
     * @param args 参数信息
     * @return 调用结果
     */
    public static Object invoke(final Object instance, final String methodName, Object... args) {
        ArgUtil.notEmpty(methodName, "methodName");

        try {
            //1. 如果参数为空
            if(ArrayUtil.isEmpty(args)) {
                return invokeNoArgsMethod(instance, methodName);
            }

            //2. 如果参数不为空，则需要获取对应的参数列表
            final Class clazz = instance.getClass();
            Class<?>[] paramTypes = new Class[args.length];
            for(int i = 0; i < args.length; i++) {
                Object param = args[i];
                paramTypes[i] = param.getClass();
            }

            Method method = ClassUtil.getMethod(clazz, methodName, paramTypes);
            return method.invoke(instance, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 直接执行调用无参方法
     * @param instance 实例对象
     * @param method 方法信息
     * @return 结果
     */
    public static Object invokeNoArgsMethod(final Object instance,
                                          final Method method) {
        //0. fail-fast
        if(ObjectUtil.isNull(method)) {
            return null;
        }

        //1. 信息校验
        final String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();
        if(ArrayUtil.isNotEmpty(paramTypes)) {
            throw new CommonRuntimeException(methodName + " must be has no params.");
        }

        //2.反射调用
        return ReflectMethodUtil.invoke(instance, method);
    }

    /**
     * 直接执行调用无参方法
     * @param instance 实例对象
     * @param methodName 方法名称信息
     * @return 结果
     */
    public static Object invokeNoArgsMethod(final Object instance,
                                            final String methodName) {
        ArgUtil.notNull(instance, "instance");

        final Class clazz = instance.getClass();
        Method method = ClassUtil.getMethod(clazz, methodName);
        return invokeNoArgsMethod(instance, method);
    }

    /**
     * 直接执行调用无参方法
     *
     * 限制如下：
     * （1）工厂方法必须为静态
     * （2）工厂方法必须无参
     * （3）工厂方法必须返回指定对象信息
     * @param clazz 类信息
     * @param factoryMethod 工厂方法
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public static Object invokeFactoryMethod(final Class clazz,
                                           final Method factoryMethod) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notNull(factoryMethod, "factoryMethod");

        //1. 信息校验
        //1.1 无参
        final String methodName = factoryMethod.getName();
        Class<?>[] paramTypes = factoryMethod.getParameterTypes();
        if(ArrayUtil.isNotEmpty(paramTypes)) {
            throw new CommonRuntimeException(methodName + " must be has no params.");
        }
        //1.2 静态
        if(!Modifier.isStatic(factoryMethod.getModifiers())) {
            throw new CommonRuntimeException(methodName + " must be static.");
        }
        //1.3 返回值
        Class returnType = factoryMethod.getReturnType();
        if(!returnType.isAssignableFrom(clazz)) {
            throw new CommonRuntimeException(methodName + " must be return " + returnType.getName());
        }

        //2.反射调用
        return ReflectMethodUtil.invoke(null, factoryMethod);
    }

    /**
     * 获取泛型参数类型
     * @param method 方法信息
     * @param paramIndex 参数下标
     * @return 结果
     */
    public static Class getGenericReturnParamType(final Method method, final int paramIndex) {
        ArgUtil.notNull(method, "method");
        ArgUtil.notNegative(paramIndex, "paramIndex");

        Type returnType = method.getGenericReturnType();
        if(ObjectUtil.isNull(returnType)) {
            return null;
        }

        return TypeUtil.getGenericParamType(returnType, paramIndex);
    }

    /***
     * 调用 setter 方法，进行设置值
     * @param instance 实例信息
     * @param propertyName 属性名称
     * @param value 对象值
     */
    public static void invokeSetterMethod(final Object instance,
                                          final String propertyName,
                                          final Object value) {
        ArgUtil.notNull(instance, "instance");
        ArgUtil.notNull(propertyName, "propertyName");

        if(ObjectUtil.isNull(value)) {
            return;
        }

        final Class<?> clazz = instance.getClass();
        String setMethodName = buildSetMethodName(propertyName);

        // 反射获取对应的方法
        final Class<?> paramType = value.getClass();
        try {
            Method method = clazz.getMethod(setMethodName, paramType);
            method.invoke(instance, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /***
     * 调用 getter 方法，获取属性值
     * @param instance 实例信息
     * @param fieldName 属性名称
     * @param fieldType 字段类型
     * @return 结果
     */
    public static Object invokeGetterMethod(final Object instance,
                                          final String fieldName,
                                          final Class fieldType) {
        ArgUtil.notNull(instance, "instance");
        ArgUtil.notNull(fieldType, "fieldType");
        ArgUtil.notEmpty(fieldName, "fieldName");

        final Class<?> clazz = instance.getClass();
        String getMethodName = buildGetMethodName(fieldType, fieldName);

        // 反射获取对应的方法
        try {
            Method method = clazz.getMethod(getMethodName);
            return method.invoke(instance);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /***
     * 调用 getter 方法，获取属性值
     * @param instance 实例信息
     * @param fieldName 属性名称
     * @return 结果
     */
    public static Object invokeGetterMethod(final Object instance,
                                            final String fieldName) {
        return invokeGetterMethod(instance, fieldName, String.class);
    }

    /***
     * 调用 getter 方法，获取属性值
     * @param instance 实例信息
     * @param field 字段类型
     * @return  结果
     */
    public static Object invokeGetterMethod(final Object instance,
                                            final Field field) {
        final Class<?> fieldType = field.getType();
        final String fieldName = field.getName();

        return invokeGetterMethod(instance, fieldName, fieldType);
    }

    /**
     * 构建设置方法名称
     * @param propertyName 属性名称
     * @return set 方法名称
     */
    public static String buildSetMethodName(final String propertyName) {
        ArgUtil.notEmpty(propertyName, "propertyName");

        return MethodConst.SET_PREFIX + StringUtil.firstToUpperCase(propertyName);
    }

    /**
     * 构建设置方法名称
     * （1）boolean 会变为 isXXX
     * （2）常规都是 getXXX
     * @param fieldType 字段类型
     * @param propertyName 属性名称
     * @return set 方法名称
     */
    public static String buildGetMethodName(final Class fieldType,
                                            final String propertyName) {
        ArgUtil.notNull(fieldType, "fieldType");
        ArgUtil.notEmpty(propertyName, "propertyName");

        if(boolean.class.equals(fieldType)) {
            return MethodConst.IS_PREFIX + StringUtil.firstToUpperCase(propertyName);
        }
        return MethodConst.GET_PREFIX + StringUtil.firstToUpperCase(propertyName);
    }

    /**
     * 构建设置方法名称
     * 1. 默认使用 getXXX
     * @param propertyName 属性名称
     * @return set 方法名称
     */
    public static String buildGetMethodName(final String propertyName) {
        return buildGetMethodName(String.class, propertyName);
    }

}
