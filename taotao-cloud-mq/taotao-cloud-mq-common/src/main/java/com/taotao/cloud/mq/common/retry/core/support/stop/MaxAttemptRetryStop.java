package com.taotao.cloud.mq.common.retry.core.support.stop;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.stop.RetryStop;

/**
 * 最大尝试次数终止策略
 * @author shuigedeng
 * @since 0.0.1
 */
public class MaxAttemptRetryStop implements RetryStop {

    /**
     * 最大重试次数
     * 1. 必须为正整数
     */
    private final int maxAttempt;

    public MaxAttemptRetryStop(int maxAttempt) {
        ArgUtils.positive(maxAttempt, "MaxAttempt");
        this.maxAttempt = maxAttempt;
    }

    @Override
    public boolean stop(RetryAttempt attempt) {
        int times = attempt.attempt();
        return times >= maxAttempt;
    }

}
