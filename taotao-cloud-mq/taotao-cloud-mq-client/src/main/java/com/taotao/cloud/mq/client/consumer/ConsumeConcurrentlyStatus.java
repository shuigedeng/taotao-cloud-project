package com.taotao.cloud.mq.client.consumer;

/**
 * 并发消费结果（对标 RocketMQ ConsumeConcurrentlyStatus）.
 */
public enum ConsumeConcurrentlyStatus {
    /** 消费成功 */
    CONSUME_SUCCESS,
    /** 处理失败，后续重新投递 */
    RECONSUME_LATER
}
