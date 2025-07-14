package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

/**
 * 恒为假重试条件
 * @author shuigedeng
 * @since 0.0.1
 */
public class AlwaysFalseRetryCondition implements RetryCondition {

    /**
     * 内部静态类
     */
    private static class AlwaysFalseRetryConditionHolder {
        private static final AlwaysFalseRetryCondition INSTANCE = new AlwaysFalseRetryCondition();
    }

    /**
     * 获取单例
     * @return 单例
     */
    public static RetryCondition getInstance() {
        return AlwaysFalseRetryConditionHolder.INSTANCE;
    }

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        return false;
    }
    
}
