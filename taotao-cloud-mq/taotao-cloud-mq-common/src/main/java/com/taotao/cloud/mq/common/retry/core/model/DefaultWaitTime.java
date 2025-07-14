package com.taotao.cloud.mq.common.retry.core.model;

import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

import java.util.concurrent.TimeUnit;

/**
 * @author shuigedeng
 * @since 0.0.1
 */
public class DefaultWaitTime implements WaitTime {

    /**
     * 等待时间
     */
    private final long time;

    /**
     * 时间单位
     */
    private final TimeUnit unit;

    public DefaultWaitTime(long time) {
        this.time = time;
        this.unit = TimeUnit.MILLISECONDS;
    }

    public DefaultWaitTime(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public long time() {
        return this.time;
    }

    @Override
    public TimeUnit unit() {
        return this.unit;
    }

}
