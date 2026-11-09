package com.taotao.cloud.mq.consistency.ttc.common.filter;

import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;

/**
 * 消息过滤器接口（对标 RocketMQ MessageFilter）.
 * <p>
 * Broker 端在拉取消息时，根据消费者订阅的过滤条件调用过滤器，
 * 只将匹配的消息投递给消费者，减少无效消息传输。
 */
public interface MessageFilter {

    /**
     * 判断消息是否匹配过滤条件。
     *
     * @param msg 待过滤的消息
     * @return true 表示匹配（应投递给消费者），false 表示不匹配（应过滤掉）
     */
    boolean match(MessageExt msg);

    /**
     * 获取过滤器类型。
     */
    FilterType getFilterType();

    /**
     * 获取过滤表达式。
     */
    String getExpression();
}
