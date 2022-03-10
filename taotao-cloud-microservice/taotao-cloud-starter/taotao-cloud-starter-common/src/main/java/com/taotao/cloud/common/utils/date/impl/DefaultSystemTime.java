package com.taotao.cloud.common.utils.date.impl;


import com.taotao.cloud.common.utils.date.Time;

/**
 * 默认系统时间
 */
class DefaultSystemTime implements Time {

    private static final DefaultSystemTime INSTANCE = new DefaultSystemTime();

    public static Time getInstance() {
        return INSTANCE;
    }

    @Override
    public long time() {
        return System.currentTimeMillis();
    }

}
