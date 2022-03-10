/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.utils.reflect;

/**
 * 反射数组具类
 */
public final class ReflectArrayUtil {

    private ReflectArrayUtil() {}

    /**
     * 获取数组的元素类型
     * @param objects 数组
     * @return 元素类型
     */
    public static Class getComponentType(final Object[] objects) {
        Class arrayClass = objects.getClass();
        return arrayClass.getComponentType();
    }

}
