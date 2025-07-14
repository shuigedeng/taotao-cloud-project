package com.taotao.cloud.mq.common.retry.api.core;

import com.taotao.cloud.mq.common.retry.api.context.RetryContext;

/**
 *
 * 重试接口
 * @author shuigedeng
 * @since 0.0.1
 * @param <R> 泛型模板
 */
public interface Retry<R> {

    /**
     * 执行重试
     * @param context 执行上下文
     * @return 执行结果
     */
    R retryCall(final RetryContext<R> context);

}
