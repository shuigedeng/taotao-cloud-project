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


import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.collection.CollectionUtils;
import com.taotao.cloud.common.utils.common.ArgUtils;
import com.taotao.cloud.common.utils.lang.ObjectUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 反射字段工具类
 */
public final class ReflectFieldUtils {

    private ReflectFieldUtils() {}

    /**
     * 是否可以设置
     * @param sourceField 原始字段
     * @param targetField 目标字段
     * @return 结果
     */
    public static boolean isAssignable(final Field sourceField, final Field targetField) {
        // 如果有任何一个字段为空，直接返回
        if(ObjectUtils.isNull(sourceField)
                || ObjectUtils.isNull(targetField)) {
            return false;
        }

        // 如果 target 的字段为 final 则不进行设置
        if(Modifier.isFinal(targetField.getModifiers())) {
            return false;
        }

        final Class<?> sourceType = sourceField.getType();
        final Class<?> targetType = targetField.getType();

        return ClassUtils.isAssignable(sourceType, targetType);
    }

    /**
     * 判断字段为字符串类型
     *
     * @param field 字段
     * @return {@code true} 是
     */
    public static Boolean isString(Field field) {
        return field.getType() == String.class;
    }

    /**
     * 判断字段是否不为字符串类型
     * @param field 字段
     * @return  {@code true} 是
     */
    public static Boolean isNotString(Field field) {
        return !isString(field);
    }


    /**
     * 判断字段field,声明了clazz注解
     *
     * @param field 字段
     * @param clazz 注解
     * @return 是否声明了
     */
    public static boolean isAnnotationPresent(Field field, Class<? extends Annotation> clazz) {
        return field.isAnnotationPresent(clazz);
    }

    /**
     * 判断字段field,未声明clazz注解
     *
     * @param field 字段
     * @param clazz 注解
     * @return 是否未声明
     */
    public static boolean isNotAnnotationPresent(Field field, Class<? extends Annotation> clazz) {
        return !isAnnotationPresent(field, clazz);
    }

    /**
     * 获取字段的泛型参数类型
     * [java通过反射获取List中的泛型](https://blog.csdn.net/yy19900811/article/details/24239945?utm_source=blogxgwz4)
     * @param field 字段
     * @param paramIndex 泛型参数的下标
     * @return 泛型信息
     */
    public static Class getGenericParamType(final Field field, final int paramIndex) {
        if(ObjectUtils.isNull(field)) {
            return null;
        }

        field.setAccessible(true);
        Type genericType = field.getGenericType();
        return TypeUtils.getGenericParamType(genericType, paramIndex);
    }

    /**
     * 当前类包含指定的注解信息
     * @param clazz 类
     * @param annotationClass 注解类
     * @return 是否包含
     */
    public static boolean containsAnnotationField(final Class clazz,
                                                  final Class<? extends Annotation> annotationClass) {
        ArgUtils.notNull(clazz, "Clazz");
        ArgUtils.notNull(annotationClass, "Annotation class");

        List<Field> fieldList = ClassUtils.getAllFieldList(clazz);
        if(CollectionUtils.isEmpty(fieldList)) {
            return false;
        }

        for(Field field : fieldList) {
            if(field.isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取字段的类型
     * 主要作用于集合类型
     * <pre>
     * String[] 返回 String
     * Collection&lt;String&gt; 返回 String
     * Map&lt;String, Integer&gt; 返回 0: String, 1: Integer
     * </pre>
     * @param field 集合类型的字段
     * @param typeIndex 类型的下标
     * @return 对应的类型
     */
    public static Class getComponentType(final Field field,
                                         final int typeIndex) {
        final Class fieldType = field.getType();

        // 数组
        if(ClassTypeUtils.isArray(fieldType)) {
            return fieldType.getComponentType();
        }

        // 集合
        if(ClassTypeUtils.isCollection(fieldType)) {
            return getGenericParamType(field, 0);
        }

        // map 信息
        if(ClassTypeUtils.isMap(fieldType)) {
            return getGenericParamType(field, typeIndex);
        }

        return fieldType;
    }

    /**
     * 获取字段的类型
     * @param field 集合类型的字段
     * @return 对应的类型
     */
    public static Class getComponentType(final Field field) {
        return getComponentType(field, 0);
    }

    /**
     * 设置字段的值
     * @param field 字段
     * @param instance 实例对象
     * @param fieldValue 值
     * @see #setValue(Object, String, Object) 设置对象
     */
    public static void setValue(final Field field, final Object instance, final Object fieldValue) {
        try {
            field.setAccessible(true);
            field.set(instance, fieldValue);
        } catch (IllegalAccessException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 设置字段值
     * @param instance 实例
     * @param fieldName 字段名称
     * @param fieldValue 字段值
     */
    public static void setValue(final Object instance, final String fieldName, final Object fieldValue) {
        ArgUtils.notNull(instance, "instance");

        try {
            final Class clazz = instance.getClass();
            Map<String, Field> fieldNameMap = ClassUtils.getAllFieldMap(clazz);
            Field field = fieldNameMap.get(fieldName);
            field.setAccessible(true);
            field.set(instance, fieldValue);
        } catch (IllegalAccessException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取对应的值信息
     * @param field 字段
     * @param instance 实例
     * @return 值
     */
    public static Object getValue(final Field field, final Object instance) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 获取对应的值信息
     * @param fieldName 字段名称
     * @param instance 实例
     * @return 值
     */
    public static Object getValue(final String fieldName, final Object instance) {
        Field field = getField(instance, fieldName);
        return getValue(field, instance);
    }

    /**
     * 获取指定字段名称的字段信息
     * @param object 对象实例
     * @param fieldName 字段名称
     * @return 字段信息
     */
    public static Field getField(final Object object, final String fieldName) {
        ArgUtils.notNull(object, "object");

        final Class clazz = object.getClass();
        return getField(clazz, fieldName);
    }

    /**
     * 获取指定字段名称的字段信息
     * @param clazz 类名称
     * @param fieldName 字段名称
     * @return 字段信息
     */
    public static Field getField(final Class clazz, final String fieldName) {
        ArgUtils.notNull(clazz, "clazz");
        ArgUtils.notEmpty(fieldName, "fieldName");

        List<Field> fieldList = ClassUtils.getAllFieldList(clazz);

        for(Field field : fieldList) {
            String name = field.getName();
            if(name.equals(fieldName)) {
                return field;
            }
        }

        throw new CommonRuntimeException("Field not found for fieldName: " + fieldName);
    }

}
