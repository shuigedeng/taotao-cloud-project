package com.taotao.cloud.common.utils.reflect;

//import com.sun.org.apache.xpath.internal.Arg;
import com.taotao.cloud.common.utils.common.ArgUtils;
//import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;

import com.taotao.cloud.common.utils.lang.ObjectUtils;
import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * class 类型工具类
 */
public final class ClassTypeUtils {

    private ClassTypeUtils(){}

    /**
     * 常见的基础对象类型
     */
    private static final Class[] BASE_TYPE_CLASS = new Class[]{
            String.class, Boolean.class, Character.class, Byte.class, Short.class,
            Integer.class, Long.class, Float.class, Double.class, Void.class, Object.class, Class.class
    };

    /**
     * 是否为 map class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 map class
     */
    public static boolean isMap(final Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为 数组 class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 数组 class
     */
    public static boolean isArray(final Class<?> clazz) {
        return clazz.isArray();
    }

    /**
     * 是否为 Collection class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 Collection class
     */
    public static boolean isCollection(final Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为 Iterable class 类型
     *
     * @param clazz 对象类型
     * @return 是否为 数组 class
     */
    public static boolean isIterable(final Class<?> clazz) {
        return Iterable.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为基本类型
     * 1. 8大基本类型
     * 2. 常见的值类型
     *
     * @param clazz 对象类型
     * @return 是否为基本类型
     */
    public static boolean isBase(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        }
        for (Class baseClazz : BASE_TYPE_CLASS) {
            if (baseClazz.equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为枚举
     * @param clazz 类型
     * @return 枚举
     */
    public static boolean isEnum(Class<?> clazz) {
        ArgUtils.notNull(clazz, "clazz");
        return clazz.isEnum();
    }

    /**
     * 是否为抽象类
     *
     * @param clazz 类
     * @return 是否为抽象类
     */
    public static boolean isAbstract(Class<?> clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    /**
     * 是抽象类或者接口
     * @param clazz 类信息
     * @return 是否
     */
    public static boolean isAbstractOrInterface(Class<?> clazz) {
        return isAbstract(clazz)
                || clazz.isInterface();
    }

    /**
     * 是否为标准的类<br>
     * 这个类必须：
     *
     * <pre>
     * 0、不为 null
     * 1、非接口
     * 2、非抽象类
     * 3、非Enum枚举
     * 4、非数组
     * 5、非注解
     * 6、非原始类型（int, long等）
     * 7、非集合 Iterable
     * 8、非 Map.clas
     * 9、非 JVM 生成类
     * </pre>
     *
     * @param clazz 类
     * @return 是否为标准类
     */
    public static boolean isJavaBean(Class<?> clazz) {
        return null != clazz
                && !clazz.isInterface()
                && !isAbstract(clazz)
                && !clazz.isEnum()
                && !clazz.isArray()
                && !clazz.isAnnotation()
                && !clazz.isSynthetic()
                && !clazz.isPrimitive()
                && !isIterable(clazz)
                && !isMap(clazz);
    }

    /**
     * 判断一个类是JDK 自带的类型
     * jdk 自带的类，classLoader 是为空的。
     * @param clazz 类
     * @return 是否为 java 类
     */
    public static boolean isJdk(Class<?> clazz) {
        return clazz != null && clazz.getClassLoader() == null;
    }

    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的setXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean isBean(Class<?> clazz) {
        if (ClassTypeUtils.isJavaBean(clazz)) {
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取列表字段对应的类型
     * @param field 字段
     * @return 返回对应的 class 类型
     */
    public static Class getListType(Field field) {
        ParameterizedType listGenericType = (ParameterizedType) field.getGenericType();
        Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
        return (Class) listActualTypeArguments[0];
    }

    /**
     * 是否为通配符泛型
     * @param type 类型
     * @return 是否
     */
    public static boolean isWildcardGenericType(final Type type) {
        //final Class clazz = type.getClass();
        //return WildcardTypeImpl.class.equals(clazz);
	    return false;
    }

    /**
     * 是否为列表
     * @param clazz 类型
     * @return 结果
     */
    public static boolean isList(final Class clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为 set
     * @param clazz 类型
     * @return 结果
     */
    public static boolean isSet(final Class clazz) {
        return Set.class.isAssignableFrom(clazz);
    }

    /**
     * 是否为基本类型
     * @param clazz 对象类型
     * @return 是否
     */
    public static boolean isPrimitive(final Class clazz) {
        return clazz.isPrimitive();
    }

    /**
     * 是否为基本类型
     * @param object 对象
     * @return 是否
     */
    public static boolean isPrimitive(final Object object) {
        if(ObjectUtils.isNull(object)) {
            return false;
        }
        return isPrimitive(object.getClass());
    }

}
