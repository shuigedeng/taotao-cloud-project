/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.core.heaven.util.lang;


import com.taotao.cloud.core.heaven.support.handler.IHandler;
import com.taotao.cloud.core.heaven.util.lang.reflect.ClassTypeUtil;
import com.taotao.cloud.core.heaven.util.lang.reflect.ClassUtil;
import com.taotao.cloud.core.heaven.util.lang.reflect.ReflectFieldUtil;
import com.taotao.cloud.core.heaven.util.util.ArrayUtil;
import com.taotao.cloud.core.heaven.util.util.CollectionUtil;
import com.taotao.cloud.core.heaven.util.util.MapUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Object 工具类
 *
 * @author bbhou
 * @since 0.0.1
 */
public final class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * 判断两个对象是否为同一对象
     * instanceof
     * isInstance
     * isAssignableFrom
     *
     * 注意：任何一个元素为 null，则认为是不同类型。
     * @param one 第一个元素
     * @param two 第二个元素
     * @return 是否为同一对象
     */
    public static boolean isSameType(Object one, Object two) {
        if(ObjectUtil.isNull(one)
            || ObjectUtil.isNull(two)) {
            return false;
        }
        Class clazzOne = one.getClass();

        return clazzOne.isInstance(two);
    }

    /**
     * 不是同一个类型
     *
     * @param one 第一个元素
     * @param two 第二个元素
     * @return 是否为不同对象
     */
    public static boolean isNotSameType(Object one, Object two) {
        return !isSameType(one, two);
    }


    /**
     * 判断当前对象是否为空
     * - 对象为空
     * - 空字符串
     * - 空集合/map
     * - 空数组
     * - 自定义空类型
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isNull(Object object) {
        return null == object;
    }

    /**
     * 判断对象是否非null
     *
     * @param object 元素
     * @return {@code true} 非空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * 判断内容是否为空
     * - 空字符串
     * - 空集合/map
     * - 空数组
     * - 自定义空类型
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        if (isNull(object)) {
            return true;
        }

        if (object instanceof String) {
            String string = (String) object;
            return StringUtil.isEmpty(string);
        }
        if (object instanceof Collection) {
            Collection collection = (Collection) object;
            return CollectionUtil.isEmpty(collection);
        }
        if (object instanceof Map) {
            Map map = (Map) object;
            return MapUtil.isEmpty(map);
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        }

        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param object 对象
     * @return 是否非空
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }


    /**
     * 判断两个对象是否相同
     * 1.如果不是同一种类型,则直接返回false
     *
     * @param except 期望值
     * @param real   实际值
     * @return 两个对象是否相同
     */
    public static boolean isEquals(Object except, Object real) {
        //1. 不是同一种类型
        if (isNotSameType(except, real)) {
            return false;
        }

        final Class exceptClass = except.getClass();
        final Class realClass = except.getClass();

        //2. 基本类型
        if (exceptClass.isPrimitive()
                && realClass.isPrimitive()
                && except != real) {
            return false;
        }

        //3. 数组
        if (ClassTypeUtil.isArray(exceptClass)
                && ClassTypeUtil.isArray(realClass)) {
            Object[] exceptArray = (Object[]) except;
            Object[] realArray = (Object[]) real;
            return Arrays.equals(exceptArray, realArray);
        }

        //3. Collection

        //4. map
        if (ClassTypeUtil.isMap(exceptClass) && ClassTypeUtil.isMap(realClass)) {
            Map exceptMap = (Map) except;
            Map realMap = (Map) real;
            return exceptMap.equals(realMap);
        }

        return except.equals(real);
    }

    /**
     * 判断两个对象是否不相同
     * 1.如果不是同一种类型,则返回true
     *
     * @param except 期望值
     * @param real   实际值
     * @return 两个对象是否不同
     */
    public static boolean isNotEquals(Object except, Object real) {
        return !isEquals(except, real);
    }

    /**
     * 对象转字符串
     * @param object 对象
     * @return 结果
     * @since 0.1.18
     */
    public static String objectToString(final Object object) {
        return objectToString(object, null);
    }

    /**
     * 对象转字符串
     * @param object 对象
     * @param defaultValue 默认值，原始对象为 null 时返回。
     * @return 结果
     * @since 0.1.18
     */
    public static String objectToString(final Object object,
                                        final String defaultValue) {
        if(ObjectUtil.isNull(object)) {
            return defaultValue;
        }
        return object.toString();
    }

    /**
     * 判断所有参数皆为null
     * @param object 对象
     * @param others 其他参数
     * @return 是否都为空
     * @since 0.1.29
     * @see #isNull(Object) 增强版本
     */
    public static boolean isNull(final Object object, final Object... others) {
        if(ObjectUtil.isNull(object)) {
            // 其他列表不为空，则遍历
            if(ArrayUtil.isNotEmpty(others)) {
                for(Object other : others) {
                    if(ObjectUtil.isNotNull(other)) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    /**
     * 判断两个元素是否相等或者都为 Null
     * @param left 元素1
     * @param right 元素2
     * @return 是否相等或者都为 Null
     * @since 0.1.29
     */
    public static boolean isEqualsOrNull(final Object left, final Object right) {
        if(isNull(left, right)) {
            return true;
        }
        if(isNull(left) || isNull(right)) {
            return false;
        }
        return isEquals(left, right);
    }

    /**
     * 可遍历的元素对象的某个元素，转换为列表
     *
     * @param object  可遍历对象
     * @param handler 转换方式
     * @param <R>     R 泛型
     * @return 结果列表
     * @since 0.1.25
     */
    @SuppressWarnings("unchecked")
    public static <R> List<R> toList(final Object object, IHandler<Object, R> handler) {
        if (ObjectUtil.isNull(object)) {
            return Collections.emptyList();
        }

        final Class clazz = object.getClass();

        // 集合
        if (ClassTypeUtil.isCollection(clazz)) {
            Collection collection = (Collection) object;
            return CollectionUtil.toList(collection, handler);
        }

        // 数组
        if (clazz.isArray()) {
            return ArrayUtil.toList(object, handler);
        }

        throw new UnsupportedOperationException("Not support foreach() for class: " + clazz.getName());
    }

    /**
     * 获取实体对象对应的 class 信息
     * @param object 实例对象
     * @return 对象 class 信息
     * @since 0.1.41
     */
    public static Class getClass(final Object object) {
        if(ObjectUtil.isNull(object)) {
            return null;
        }
        return object.getClass();
    }

    /**
     * empty 转换为 null
     * @param object 对象
     * @since 0.1.123
     */
    public static void emptyToNull(Object object) {
        if(null == object) {
            return;
        }

        List<Field> fieldList = ClassUtil.getAllFieldList(object.getClass());
        for(Field field : fieldList) {
            Object value = ReflectFieldUtil.getValue(field, object);
            if(ObjectUtil.isEmpty(value)) {
                ReflectFieldUtil.setValue(field, object, null);
            }
        }
    }

    /**
     * 基于反射的属性拷贝
     * @param source 源头
     * @param target 目标
     * @since 0.1.147
     */
    public static void copyProperties(Object source, Object target) {
        if(source == null || target == null) {
            return;
        }

        Map<String, Field> sourceFieldMap = ClassUtil.getAllFieldMap(source.getClass());
        Map<String, Field> targetFieldMap = ClassUtil.getAllFieldMap(target.getClass());

        // 遍历
        for(Map.Entry<String, Field> entry : sourceFieldMap.entrySet()) {
            String sourceFieldName = entry.getKey();
            Field sourceField = entry.getValue();
            Field targetField = targetFieldMap.get(sourceFieldName);

            if(targetField == null) {
                continue;
            }

            if(ClassUtil.isAssignable(sourceField.getType(), targetField.getType())) {
                Object sourceVal = ReflectFieldUtil.getValue(sourceField, source);
                ReflectFieldUtil.setValue(targetField, target, sourceVal);
            }
        }
    }

    /**
     * 是否为相同的值
     * null null 被认为相同
     * @param valueOne 第一个
     * @param valueTwo 第二个
     * @return 是否
     * @since 0.1.147
     */
    public static boolean isSameValue(Object valueOne, Object valueTwo) {
        if(valueOne == null && valueTwo == null) {
            return true;
        }

        if(valueOne == null || valueTwo == null) {
            return false;
        }

        return valueOne.equals(valueTwo);
    }

}
