package com.taotao.cloud.mq.common.retry.api.support.stop;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

/**
 * 结束的条件
 * @author shuigedeng
 * @since 0.0.1
 * @see com.github.houbb.sisyphus.api.support.condition.RetryCondition 执行的条件
 */
public interface RetryStop {

    /**
     * 停止执行
     * @param attempt 执行信息
     * @return 是否停止
     */
    boolean stop(final RetryAttempt attempt);

}
