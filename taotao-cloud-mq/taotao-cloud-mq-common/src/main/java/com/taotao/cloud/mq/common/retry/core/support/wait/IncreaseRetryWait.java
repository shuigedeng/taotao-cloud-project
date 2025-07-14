package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

/**
 * 递增策略
 * @since 0.0.1
 * @author shuigedeng
 */
public class IncreaseRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        final int previousAttempt = retryWaitContext.attempt()-1;
        long result = Math.round(retryWaitContext.value() + previousAttempt*retryWaitContext.factor());

        return super.rangeCorrect(result, retryWaitContext.min(), retryWaitContext.max());
    }

}
