package com.taotao.cloud.openapi.common.util;

import com.taotao.cloud.openapi.common.model.Binary;

import java.util.Collection;

/**
 * 类型工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:07:53
 */
public class TypeUtil extends cn.hutool.core.util.TypeUtil {

    /**
     * 判断是不是 byte[]类型
     *
     * @param clazz 类型
     * @return 是否是 byte[]类型
     */
    public static boolean isPrimitiveByteArray(Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isArray()) {
            Class elementClass = clazz.getComponentType();
            if (byte.class.equals(elementClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是不是 byte[] or Byte[] 类型
     *
     * @param clazz 类型
     * @return 是否是 byte[] or Byte[] 类型
     */
    public static boolean isByteArray(Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isArray()) {
            Class elementClass = clazz.getComponentType();
            if (byte.class.equals(elementClass) || Byte.class.equals(elementClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是 Binary[] 及其子类数组类型
     *
     * @param clazz 类型
     * @return 是否是 Binary[] 及其子类数组类型
     */
    public static boolean isBinaryArray(Class clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.isArray()) {
            Class elementClass = clazz.getComponentType();
            if (Binary.class.isAssignableFrom(elementClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Binary及其子类的集合类型
     *
     * @param obj 对象
     * @return 是否是Binary及其子类的集合类型
     */
    public static boolean isBinaryCollection(Object obj) {
        Class clazz = obj.getClass();
        //由于获取不到泛型信息，故改用直接获取元素来判断(假定集合内都是存储Binary或其子类的)
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection coll = (Collection) obj;
            Object element = coll.iterator().next();
            if (element != null && Binary.class.isAssignableFrom(element.getClass())) {
                return true;
            }
        }
        return false;
    }
}
