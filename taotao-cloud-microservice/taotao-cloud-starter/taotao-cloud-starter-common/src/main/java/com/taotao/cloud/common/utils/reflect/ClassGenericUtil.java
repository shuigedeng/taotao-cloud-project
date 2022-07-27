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
import com.taotao.cloud.common.utils.collection.ArrayUtil;
import com.taotao.cloud.common.utils.collection.CollectionUtil;

import com.taotao.cloud.common.utils.lang.ObjectUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * class 泛型工具类
 */
public final class ClassGenericUtil {

    private ClassGenericUtil(){}

    /**
     * 获取当前类的泛型接口信息
     * @param clazz 类
     * @return 泛型接口信息
     */
    private static List<Type> getGenericInterfaces(final Class clazz) {
        Set<Type> typeSet = new HashSet<>();

        // 添加当前类的泛型接口信息
        Type[] types = clazz.getGenericInterfaces();
        if(ArrayUtil.isNotEmpty(types)) {
            typeSet.addAll(Lists.newArrayList(types));
        }

        // 当前类的泛型父类信息
        Type superType = clazz.getGenericSuperclass();
        if(ObjectUtil.isNotNull(superType)
                && superType.getClass().isInterface()) {
            typeSet.add(superType);
        }

        return Lists.newArrayList(typeSet);
    }


    /**
     * 获取泛型类型
     * @param clazz 数据类型
     * @param interfaceClass 接口对应的 class 信息
     * @param index 泛型的下标志位置
     * @return 对应的泛型类型
     */
    public static Class getGenericClass(final Class clazz,
                                        final Class interfaceClass,
                                        final int index) {
        List<Type> typeList = ClassGenericUtil.getGenericInterfaces(clazz);
        for(Type type : typeList) {
            if(type instanceof ParameterizedType
                    && interfaceClass.equals(((ParameterizedType) type).getRawType())
            ) {
                ParameterizedType p = (ParameterizedType)type;
                return (Class) p.getActualTypeArguments()[index];
            }
        }

        return Object.class;
    }

    /**
     * 获取元素的泛型
     * @param list 列表
     * @return 泛型
     */
    public static Class getGenericClass(final Collection<?> list){
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }

        for(Object object : list) {
            if(ObjectUtil.isNotNull(object)) {
                return object.getClass();
            }
        }
        return null;
    }

    /**
     * 获取当前类对应的泛型
     * @param clazz 指定类
     * @param index 索引
     * @return 默认返回 {@link Object#getClass()}
     */
    public static Class getGenericSupperClass(final Class clazz,
                                              final int index) {
        Class classType = Object.class;

        Type pageVoParserClass = clazz.getGenericSuperclass();
        if (pageVoParserClass instanceof ParameterizedType) {
            Type[] pageVoClassTypes = ((ParameterizedType)pageVoParserClass).getActualTypeArguments();
            classType = (Class) pageVoClassTypes[index];
        }

        return classType;
    }

    /**
     * 获取当前类对应的泛型
     * @param clazz 指定类
     * @return 默认返回 {@link Object#getClass()}
     */
    public static Class getGenericSupperClass(final Class clazz) {
        return getGenericSupperClass(clazz, 0);
    }

}
