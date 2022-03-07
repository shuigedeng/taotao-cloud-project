package com.taotao.cloud.core.heaven.util.time.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.time.Time;

/**
 * 默认系统时间
 */
@ThreadSafe
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
