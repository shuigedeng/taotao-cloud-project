package com.taotao.cloud.mq.consistency.ttc.client.consumer;

import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeConcurrentlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.ConsumeOrderlyStatus;
import com.taotao.cloud.mq.consistency.ttc.client.consumer.MessageQueueContext;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;

/**
 * 消息监听器（对标 RocketMQ MessageListener）.
 * <p>
 * PushConsumer 拉回消息后回调该接口，实现订单处理 / 业务逻辑。
 */
public interface MessageListener {

    /**
     * 默认实现：顺序消费 (顺序返回即可，不抛异常即 ack).
     */
    interface Orderly extends MessageListener {
        /**
         * 顺序消费 — 同一 MessageQueue 内必须串行处理。
         *
         * @param msgs   本次拉到的消息列表
         * @param mq     消息所属 MessageQueue
         * @return 消费结果 (SUCCESS / RECONSUME_LATER / SUSPEND_CURRENT_QUEUE_A_MOMENT)
         */
        ConsumeOrderlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext mq);
    }

    /**
     * 并发消费 — 多线程并行处理, 无顺序保证.
     */
    interface Concurrently extends MessageListener {
        /**
         * 并发消费。
         *
         * @param msgs 本次拉到的消息列表
         * @param mq   消息所属 MessageQueue
         * @return 消费结果 (CONSUME_SUCCESS / RECONSUME_LATER)
         */
        ConsumeConcurrentlyStatus consumeMessage(MessageExt[] msgs, MessageQueueContext mq);
    }
}
