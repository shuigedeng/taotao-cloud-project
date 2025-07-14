package com.taotao.cloud.mq.common.retry.core.support.listen;

import com.taotao.boot.common.support.pipeline.Pipeline;
import com.taotao.boot.common.support.pipeline.impl.DefaultPipeline;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;

import java.util.List;

/**
 * 监听器初始化
 * @author shuigedeng
 * @since 0.0.1
 */
public abstract class AbstractRetryListenInit implements RetryListen {

    @Override
    public <R> void listen(RetryAttempt<R> attempt) {
        Pipeline<RetryListen> pipeline = new DefaultPipeline<>();
        this.init(pipeline, attempt);

        //执行
        final List<RetryListen> retryListenList = pipeline.list();
        for(RetryListen retryListen : retryListenList) {
            retryListen.listen(attempt);
        }
    }

    /**
     * 初始化监听器列表
     * @param pipeline 泳道
     * @param attempt 重试信息
     */
    protected abstract void init(final Pipeline<RetryListen> pipeline,
                                 final RetryAttempt attempt);

}
