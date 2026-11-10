package com.taotao.cloud.mq.client.consumer;

/**
 * 顺序消费结果（对标 RocketMQ ConsumeOrderlyStatus）.
 */
public enum ConsumeOrderlyStatus {
    /** 消费成功 */
    SUCCESS,
    /** 处理失败，稍后重试 */
    RECONSUME_LATER,
    /** 暂停当前队列一会儿 (用于流控) */
    SUSPEND_CURRENT_QUEUE_A_MOMENT
}
