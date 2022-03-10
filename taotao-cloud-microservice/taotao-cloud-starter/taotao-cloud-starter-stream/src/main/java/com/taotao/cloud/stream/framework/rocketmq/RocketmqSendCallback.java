package com.taotao.cloud.stream.framework.rocketmq;

import com.taotao.cloud.common.utils.log.LogUtil;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * RocketmqSendCallback
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-25 10:00:39
 */
public class RocketmqSendCallback implements SendCallback {

    @Override
    public void onSuccess(SendResult sendResult) {
        LogUtil.info("async onSuccess SendResult={}", sendResult);
    }

    @Override
    public void onException(Throwable throwable) {
	    LogUtil.error("async onException Throwable", throwable);
    }
}
