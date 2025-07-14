package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.AttemptTime;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 根据时间进行重试的抽象类
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractTimeRetryCondition implements RetryCondition {

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        return timeCondition(retryAttempt.time());
    }

    /**
     * 对消耗时间信息进行判断
     * 1. 用户可以判定是执行重试
     * 2. 比如任务执行的时间过长，过者任务执行的时间不在预期的时间范围内
     * @param attemptTime 时间信息
     * @return 对消耗时间信息进行判断
     */
    protected abstract boolean timeCondition(final AttemptTime attemptTime);

}
