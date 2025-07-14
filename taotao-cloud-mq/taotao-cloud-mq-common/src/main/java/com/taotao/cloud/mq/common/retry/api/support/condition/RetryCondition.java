package com.taotao.cloud.mq.common.retry.api.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

/**
 * 重试执行的条件
 *
 * 注意：实现类应该有无参构造函数
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryCondition<R> {

    /**
     * 是否满足重试的条件
     * @param retryAttempt 重试相关信息
     * @return 是否
     */
    boolean condition(final RetryAttempt<R> retryAttempt);

}
