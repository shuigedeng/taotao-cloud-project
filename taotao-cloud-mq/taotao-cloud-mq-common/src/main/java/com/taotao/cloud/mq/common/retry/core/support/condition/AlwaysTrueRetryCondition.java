package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 恒为真重试条件
 * @author shuigedeng
 * @since 0.0.1
 */
public class AlwaysTrueRetryCondition implements RetryCondition {

    /**
     * 内部静态类
     */
    private static class AlwaysTrueRetryConditionHolder {
        private static final AlwaysTrueRetryCondition INSTANCE = new AlwaysTrueRetryCondition();
    }

    /**
     * 获取单例
     * @return 单例
     */
    public static RetryCondition getInstance() {
        return AlwaysTrueRetryConditionHolder.INSTANCE;
    }

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        return true;
    }

}
