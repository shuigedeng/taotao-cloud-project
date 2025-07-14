package com.taotao.cloud.mq.common.retry.api.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 重试等待策略
 * 1. 所有的实现必须要有无参构造器，因为会基于反射处理类信息。
 * 2. 尽可能的保证为线程安全的，比如 stateless。
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryWait {

    /**
     * 等待时间
     * @param retryWaitContext 上下文信息
     * @return 等待时间的结果信息
     * @since 0.0.3 参数调整
     */
    WaitTime waitTime(final RetryWaitContext retryWaitContext);

}
