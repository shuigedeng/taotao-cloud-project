package com.taotao.cloud.mq.client.consumer.rebalance;

import com.taotao.cloud.mq.common.MessageQueue;

import java.util.List;

/**
 * 消息队列分配策略接口（对标 RocketMQ AllocateMessageQueueStrategy）.
 * <p>
 * 当 ConsumerGroup 中有多个 Consumer 实例时，需要把该 Topic 的所有队列
 * 均匀分配给各个 Consumer，保证:
 * <ul>
 *   <li>每个队列只被一个 Consumer 消费</li>
 *   <li>各 Consumer 分到的队列数尽量均衡</li>
 *   <li>Consumer 数量变化时自动触发 rebalance</li>
 * </ul>
 */
public interface AllocateMessageQueueStrategy {

    /**
     * 分配队列.
     *
     * @param consumerGroup  消费者组名
     * @param currentCID     当前消费者实例 ID
     * @param mqAll          该 Topic 下所有可读队列
     * @param cidAll         消费组内所有存活消费者 ID (已排序)
     * @return 分配给当前消费者实例的队列列表
     */
    List<MessageQueue> allocate(
            String consumerGroup,
            String currentCID,
            List<MessageQueue> mqAll,
            List<String> cidAll);

    /**
     * 策略名称.
     */
    String getName();
}
