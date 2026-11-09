package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import com.taotao.cloud.mq.consistency.ttc.common.MessageQueue;

/**
 * 消费上下文 — 业务回调时携带的最小信息。
 * <p>
 * 对标 RocketMQ ConsumeConcurrentlyContext / ConsumeOrderlyContext 的交集子集，
 * 只暴露业务回调真正会用到的字段 (MessageQueue)，避免把 RocketMQ 全部字段拖过来。
 */
public class MessageQueueContext {

    private final MessageQueue messageQueue;

    public MessageQueueContext(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }
}
