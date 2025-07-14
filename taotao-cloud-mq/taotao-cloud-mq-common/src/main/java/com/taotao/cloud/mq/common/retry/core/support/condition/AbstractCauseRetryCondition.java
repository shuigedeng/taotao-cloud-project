package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 根据结果进行重试的抽象类
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractCauseRetryCondition implements RetryCondition {

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        return causeCondition(retryAttempt.cause());
    }

    /**
     * 对异常信息进行判断
     * 1. 用户可以判定是否有异常
     * @param throwable 异常信息
     * @return 对异常信息进行判断
     */
    protected abstract boolean causeCondition(final Throwable throwable);

    /**
     * 判断是否有异常信息
     * 1. 有，返回 true
     * 2. 无，返回 false
     * @param throwable 异常信息
     * @return 是否有异常信息
     */
    protected boolean hasException(final Throwable throwable) {
        return ObjectUtils.isNotNull(throwable);
    }

}
