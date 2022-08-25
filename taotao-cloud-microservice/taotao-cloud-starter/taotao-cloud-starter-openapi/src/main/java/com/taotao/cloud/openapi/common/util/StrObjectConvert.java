package com.taotao.cloud.openapi.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Type;

/**
 * 字符串对象转换工具类
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:07:40
 */
public class StrObjectConvert {

    /**
     * 字符串转对象
     *
     * @param str  字符串
     * @param type 对象类型
     * @return 对象
     */
    public static Object strToObj(String str, Type type) {
        if (str == null) {
            return null;
        }
        String typeName = type.getTypeName();
        boolean isClassType = false;
        if (type instanceof Class) {
            isClassType = true;
        }
        Object ins;
        if (isClassType && ClassUtil.isBasicType((Class) type)) {
            //是基本类型（包括包装类）：Boolean, Character, Byte, Short, Integer, Long, Float, Double, Void
            if (typeName.equals(Void.class.getName()) || typeName.equals(void.class.getName())) {
                //void类型单独处理
                ins = null;
            } else {
                //使用hutool类型转换工具将字符串转换为对象
                ins = Convert.convert(type, str);
            }
        } else if (typeName.equals(String.class.getName())) {
            ins = str;
        } else if (isClassType && ((Class) type).isEnum()) {
            //枚举类型
            ins = Enum.valueOf((Class) type, str);
        } else {
            //对象转换：可以将JSON字符串直接转换为任意对象（Bean、Map、集合、数组等）
            ins = JSONUtil.toBean(str, type, false);
        }
        return ins;
    }

    /**
     * 对象转字符串
     *
     * @param obj  对象
     * @param type 对象类型
     * @return 字符串
     */
    public static String objToStr(Object obj, Type type) {
        if (obj == null) {
            return null;
        }
        String typeName = type.getTypeName();
        String str;
        if (ObjectUtil.isBasicType(obj)) {
            //是基本类型（包括包装类）：Boolean, Character, Byte, Short, Integer, Long, Float, Double, Void
            if (typeName.equals(Void.class.getName()) || typeName.equals(void.class.getName())) {
                //void类型单独处理
                str = null;
            } else {
                str = String.valueOf(obj);
            }
        } else if (type.getTypeName().equals(String.class.getName())) {
            //字符串类型，无需转换
            str = (String) obj;
        } else if (obj.getClass().isEnum()) {
            //枚举类型，转为为字符串
            str = obj.toString();
        } else {
            //对象转换：可以将任意对象（Bean、Map、集合、数组等）直接转换为JSON字符串
            str = JSONUtil.toJsonStr(obj);
        }
        return str;
    }
}
