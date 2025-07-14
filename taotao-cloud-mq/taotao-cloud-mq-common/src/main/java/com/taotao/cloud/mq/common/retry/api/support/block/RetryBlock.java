package com.taotao.cloud.mq.common.retry.api.support.block;

import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 阻塞的方式
 * @author shuigedeng
 * @since 0.0.1
 */
public interface RetryBlock {

    /**
     * 阻塞方式
     * @param waitTime 等待时间
     */
    void block(final WaitTime waitTime);

}
