package com.taotao.cloud.common.utils.date.impl;

/**
 * 时间工具类
 */
public final class Times {

    private Times(){}

    /**
     * 获取系统时间
     * @return 时间
     */
    public static long systemTime() {
        return DefaultSystemTime.getInstance().time();
    }

}
