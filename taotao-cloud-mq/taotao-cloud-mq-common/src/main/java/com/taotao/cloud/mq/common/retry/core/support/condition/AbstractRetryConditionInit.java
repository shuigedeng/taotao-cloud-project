package com.taotao.cloud.mq.common.retry.core.support.condition;

import com.taotao.boot.common.support.pipeline.Pipeline;
import com.taotao.boot.common.support.pipeline.impl.DefaultPipeline;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.condition.RetryCondition;

import java.util.List;

/**
 * 重试条件初始化类
 * 1. 满足任意一个即可
 * 2. 如果有更加复杂的需求，用户应该自定定义。
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractRetryConditionInit implements RetryCondition {

    @Override
    public boolean condition(RetryAttempt retryAttempt) {
        Pipeline<RetryCondition> pipeline = new DefaultPipeline<>();
        this.init(pipeline, retryAttempt);

        List<RetryCondition> retryConditions = pipeline.list();
        for (RetryCondition condition : retryConditions) {
            if (condition.condition(retryAttempt)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化列表
     *
     * @param pipeline     当前列表泳道
     * @param retryAttempt 执行信息
     */
    protected abstract void init(final Pipeline<RetryCondition> pipeline,
                                 final RetryAttempt retryAttempt);

}
