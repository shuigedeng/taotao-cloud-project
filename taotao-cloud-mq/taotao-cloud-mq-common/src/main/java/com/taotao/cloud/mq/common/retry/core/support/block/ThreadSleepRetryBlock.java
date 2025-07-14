package com.taotao.cloud.mq.common.retry.core.support.block;

import com.taotao.boot.common.support.instance.impl.InstanceFactory;
import com.taotao.cloud.mq.common.retry.api.exception.RetryException;
import com.taotao.cloud.mq.common.retry.api.model.WaitTime;
import com.taotao.cloud.mq.common.retry.api.support.block.RetryBlock;

/**
 * 线程沉睡的阻塞方式
 * @author shuigedeng
 * @since 0.0.1
 */
public class ThreadSleepRetryBlock implements RetryBlock {

    /**
     * 获取单例
     * @return 获取单例
     */
    public static RetryBlock getInstance() {
        return InstanceFactory.getInstance().singleton(ThreadSleepRetryBlock.class);
    }

    @Override
    public void block(WaitTime waitTime) {
        try {
            waitTime.unit().sleep(waitTime.time());
        } catch (InterruptedException e) {
            //restore status
            Thread.currentThread().interrupt();

            throw new RetryException(e);
        }
    }

}
