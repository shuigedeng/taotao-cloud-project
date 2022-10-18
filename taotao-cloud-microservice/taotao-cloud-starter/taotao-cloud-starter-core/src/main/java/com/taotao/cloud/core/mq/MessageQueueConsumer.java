package com.taotao.cloud.core.mq;


import com.taotao.cloud.core.mq.consumer.Acknowledgement;

import java.util.List;

/**
 * 消息队列消费者
 */
public interface MessageQueueConsumer {

	/**
	 * 消费消息
	 *
	 * @param messages
	 */
	void consume(List<Message> messages, Acknowledgement ack);
}
