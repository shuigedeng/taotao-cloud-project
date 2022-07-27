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
import com.google.common.collect.Sets;
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.common.ArgUtil;

import com.taotao.cloud.common.utils.lang.ObjectUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 反射注解工具类
 */
public final class ReflectAnnotationUtil {

    private ReflectAnnotationUtil() {}


    /**
     * 更新值信息
     * @param annotation 注解
     * @param method 方法
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    public static void updateValue(final Annotation annotation,
                                   final String method,
                                   final Object value) {
        // 获取 memberValues
        Map<String, Object> memberValues = getAnnotationAttributes(annotation);

        // 修改 value 属性值
        memberValues.put(method, value);
    }

    /**
     * 获取值信息
     * @param annotation 注解
     * @param method 方法
     * @return 获取的属性值
     */
    @SuppressWarnings("unchecked")
    public static Object getValue(final Annotation annotation,
                                   final String method) {
        // 获取 memberValues
        Map<String, Object> memberValues = getAnnotationAttributes(annotation);

        // 修改 value 属性值
        return memberValues.get(method);
    }

    /**
     * 获取值信息-字符串形式
     * @param annotation 注解
     * @param method 方法
     * @return 对象值 String 形式
     */
    @SuppressWarnings("unchecked")
    public static String getValueStr(final Annotation annotation,
                                  final String method) {
        // 获取 memberValues
        Map<String, Object> memberValues = getAnnotationAttributes(annotation);

        // 修改 value 属性值
        Object object = memberValues.get(method);
        return ObjectUtil.objectToString(object);
    }

    /**
     * 获取对应的注解属性 map
     * https://segmentfault.com/a/1190000011213222?utm_source=tag-newest
     * @param annotation 直接
     * @return map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAnnotationAttributes(final Annotation annotation) {
        try {
            //获取 annotation 这个代理实例所持有的 InvocationHandler
            InvocationHandler h = Proxy.getInvocationHandler(annotation);
            // 获取 AnnotationInvocationHandler 的 memberValues 字段
            Field hField = h.getClass().getDeclaredField("memberValues");
            // 因为这个字段事 private final 修饰，所以要打开权限
            hField.setAccessible(true);

            // 获取 memberValues
            return (Map<String, Object>) hField.get(h);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取当前字段被指定注解标注的注解
     * @param annotation 注解
     * @param annotationClass 标注注解类型
     * @return 注解信息
     */
    public static Optional<Annotation> getAnnotation(final Annotation annotation,
                                                     final Class<? extends Annotation> annotationClass) {
        if(ObjectUtil.isNull(annotation)
            || ObjectUtil.isNull(annotationClass)) {
            return Optional.empty();
        }

        Annotation atAnnotation = annotation.annotationType().getAnnotation(annotationClass);
        if (ObjectUtil.isNotNull(atAnnotation)) {
            return Optional.of(atAnnotation);
        }

        return Optional.empty();
    }

    /**
     * 获取类指定的注解
     * @param clazz 类
     * @param annotationClass 指定注解类型
     * @return 结果
     */
    @SuppressWarnings("unchecked")
    public static Optional<Annotation> getAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notNull(annotationClass, "annotationClass");

        if(clazz.isAnnotationPresent(annotationClass)) {
            Annotation annotation = clazz.getAnnotation(annotationClass);
            return Optional.of(annotation);
        }

        return Optional.empty();
    }

    /**
     * 获取类对应的注解
     * （1）直接对应的注解
     * （2）被指定注解类型标注的注解。
     * @param clazz 类型
     * @param annotationClass 注解类
     * @return 结果列表
     */
    public static List<Annotation> getAnnotationRefs(final Class clazz, final Class<? extends Annotation> annotationClass) {
        ArgUtil.notNull(clazz, "clazz");
        ArgUtil.notNull(annotationClass, "annotationClass");


        Set<Annotation> annotationSet = Sets.newHashSet();
        Annotation[] annotations = clazz.getAnnotations();
        if(ArrayUtil.isEmpty(annotations)) {
            return Lists.newArrayList();
        }

        for(Annotation annotation : annotations) {
            // 注解为当前类
            if(annotation.annotationType().equals(annotationClass)) {
                annotationSet.add(annotation);
            } else if(annotation.annotationType().isAnnotationPresent(annotationClass)) {
                // 注解被当前指定注解指定
                annotationSet.add(annotation);
            }
        }

        return Lists.newArrayList(annotationSet);
    }

}
