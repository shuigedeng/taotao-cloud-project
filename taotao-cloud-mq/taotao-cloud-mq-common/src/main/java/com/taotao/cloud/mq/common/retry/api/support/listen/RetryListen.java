package com.taotao.cloud.mq.common.retry.api.support.listen;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;

/**
 * 重试监听接口
 *
 * 注意：实现类应该有无参构造函数
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryListen {

    /**
     * 执行重试监听
     * @param attempt 重试
     * @param <R> 泛型
     */
    <R> void listen(final RetryAttempt<R> attempt);

}
