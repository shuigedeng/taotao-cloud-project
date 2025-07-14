package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 固定时间间隔等待
 * @author shuigedeng
 * @since 0.0.1
 */
public class FixedRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        return super.rangeCorrect(retryWaitContext.value(), retryWaitContext.min(), retryWaitContext.max());
    }

}
