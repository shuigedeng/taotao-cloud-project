package com.taotao.cloud.mq.common.retry.core.support.listen;

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.cloud.mq.common.retry.api.model.RetryAttempt;
import com.taotao.cloud.mq.common.retry.api.support.listen.RetryListen;

/**
 * 不进行任何监听动作
 * @author shuigedeng
 * @since 0.0.1
 */
public class NoRetryListen implements RetryListen {

    /**
     * 获取单例
     * @return 单例
     */
    public static RetryListen getInstance() {
        return InstanceFactory.getInstance().singleton(NoRetryListen.class);
    }

    @Override
    public <R> void listen(RetryAttempt<R> attempt) {

    }

}
