package com.taotao.cloud.mq.common.retry.core.model;

import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;

import java.util.Date;

/**
 * 尝试执行的时候消耗时间
 * @author shuigedeng
 * @since 0.0.1
 */
public class DefaultAttemptTime implements AttemptTime {

    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 消耗的时间
     */
    private long costTimeInMills;

    @Override
    public Date startTime() {
        return startTime;
    }

    public DefaultAttemptTime startTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public Date endTime() {
        return endTime;
    }

    public DefaultAttemptTime endTime(Date endTime) {
        this.endTime = endTime;
        return this;
    }

    @Override
    public long costTimeInMills() {
        return costTimeInMills;
    }

    public DefaultAttemptTime costTimeInMills(long costTimeInMills) {
        this.costTimeInMills = costTimeInMills;
        return this;
    }
}
