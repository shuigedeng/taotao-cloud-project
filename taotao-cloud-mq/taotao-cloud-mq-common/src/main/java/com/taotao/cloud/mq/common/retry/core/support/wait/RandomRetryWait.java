package com.taotao.cloud.mq.common.retry.core.support.wait;

import com.taotao.cloud.mq.common.retry.api.context.RetryWaitContext;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机等待策略
 * @author shuigedeng
 * @since 0.0.1
 */
public class RandomRetryWait extends AbstractRetryWait {

    @Override
    public WaitTime waitTime(RetryWaitContext retryWaitContext) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long time = random.nextLong(retryWaitContext.min(), retryWaitContext.max()-retryWaitContext.min());
        return super.rangeCorrect(time, retryWaitContext.min(), retryWaitContext.max());
    }

}
