package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 根据结果进行重试的抽象类
 * @author shuigedeng
 * @since 0.0.1
 * @param <R> 泛型
 */
public abstract class AbstractResultRetryCondition<R> implements RetryCondition<R> {

    @Override
    public boolean condition(RetryAttempt<R> retryAttempt) {
        return resultCondition(retryAttempt.result());
    }

    /**
     * 对结果进行判断
     * @param result 结果信息
     * @return 对结果进行判断
     */
    protected abstract boolean resultCondition(final R result);

    /**
     * 判断是否有结果信息
     * 1. 有，返回 true
     * 2. 无，返回 false
     * @param result 返回对象
     * @return 是否有结果
     */
    protected boolean hasResult(final R result) {
        return ObjectUtils.isNotNull(result);
    }

}
