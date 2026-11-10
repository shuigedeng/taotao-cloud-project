package com.taotao.cloud.mq.common.trace;

/**
 * 消息轨迹类型枚举（对标 RocketMQ TraceType）.
 */
public enum TraceType {

    /**
     * Producer 发送消息.
     */
    ProducerSend,

    /**
     * Broker 存储消息.
     */
    BrokerStore,

    /**
     * Consumer 消费消息.
     */
    ConsumerConsume,

    /**
     * Consumer 消费失败.
     */
    ConsumerConsumeFail,

    /**
     * 消息重试.
     */
    MessageRetry,

    /**
     * 消息进入死信队列.
     */
    MessageDeadLetter
}
