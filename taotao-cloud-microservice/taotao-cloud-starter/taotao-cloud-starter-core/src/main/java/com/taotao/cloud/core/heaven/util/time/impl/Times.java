package com.taotao.cloud.core.heaven.util.time.impl;

/**
 * 时间工具类
 */
public final class Times {

    private Times(){}

    /**
     * 获取系统时间
     * @return 时间
     * @since 0.1.37
     */
    public static long systemTime() {
        return DefaultSystemTime.getInstance().time();
    }

}
