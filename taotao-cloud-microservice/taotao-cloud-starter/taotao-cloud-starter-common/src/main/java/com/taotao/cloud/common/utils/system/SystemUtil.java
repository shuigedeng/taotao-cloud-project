/*
 * Copyright (c)  2019. houbinbin Inc.
 * heaven All rights reserved.
 */

package com.taotao.cloud.common.utils.system;


import com.taotao.cloud.common.constant.SystemConst;

/**
 * <p> 系统工具类 </p>
 */
public final class SystemUtil {

    private SystemUtil(){}

    /**
     * 获取换行符号
     * @return 换行符号
     */
    public static String getLineSeparator() {
        return getProperty(SystemConst.LINE_SEPARATOR);
    }

    /**
     * 获取属性信息
     * @param key 标识
     * @return 结果
     */
    public static String getProperty(final String key) {
        return System.getProperty(key);
    }

}
