package com.taotao.cloud.mq.broker.support.push;

/**
 * 消息推送服务
 *
 * @author shuigedeng
 * @since 2024.05
 */
public interface IBrokerPushService {

    /**
     * 异步推送
     * @param context 消息
     */
    void asyncPush(final BrokerPushContext context);

}
