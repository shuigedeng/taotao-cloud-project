package com.taotao.cloud.web.docx4j.input.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 反射工具
 */
public interface ReflectUtil {
    /**
     * 获得非静态字段列表
     * @param clazz class
     * @return 字段列表
     */
    static List<Field> getNonStaticFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            fields.addAll(ReflectUtil.getNonStaticFields(superClass));
        }

        fields.addAll(
            Stream.of(clazz.getDeclaredFields())
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList())
        );

        return fields;
    }

    /**
     * 获得字段的get方法
     * @param clazz     字段所属class
     * @param fieldName 字段名称
     * @return {@link Method}
     */
    static Method getFieldGetter(Class<?> clazz, String fieldName) {
        try {
            return new PropertyDescriptor(fieldName, clazz).getReadMethod();
        } catch (IntrospectionException e) {
            return null;
        }
    }

    /**
     * 获得字段的set方法
     * @param clazz     字段所属class
     * @param fieldName 字段名称
     * @return {@link Method}
     */
    static Method getFieldSetter(Class<?> clazz, String fieldName) {
        try {
            return new PropertyDescriptor(fieldName, clazz).getWriteMethod();
        } catch (IntrospectionException e) {
            return null;
        }
    }

    /**
     * 获得字段或字段get方法上的注解
     * @param field      字段
     * @param annotation 注解
     * @param <T>        注解类型
     * @return {@link T}
     */
    static <T extends Annotation> T getAnnotation(Field field, Class<T> annotation) {
        return
            // 优先获取字段上的注解
            Optional.ofNullable(field.getAnnotation(annotation))
                // 若字段上没有 获取get方法上的注解
                .orElseGet(() ->
                    Optional.ofNullable(ReflectUtil.getFieldGetter(field.getDeclaringClass(), field.getName()))
                        .map(m -> m.getAnnotation(annotation))
                        .orElse(null)
                );
    }
}
