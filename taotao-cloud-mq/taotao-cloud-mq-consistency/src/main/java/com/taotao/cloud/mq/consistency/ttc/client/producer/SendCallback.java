package com.taotao.cloud.mq.consistency.ttc.client.producer;

import com.taotao.cloud.mq.consistency.ttc.common.protocol.SendResult;

/**
 * 发送回调接口
 */
public interface SendCallback {

    /**
     * 发送成功回调
     *
     * @param sendResult 发送结果
     */
    void onSuccess(SendResult sendResult);

    /**
     * 发送异常回调
     *
     * @param e 异常
     */
    void onException(Throwable e);
}
