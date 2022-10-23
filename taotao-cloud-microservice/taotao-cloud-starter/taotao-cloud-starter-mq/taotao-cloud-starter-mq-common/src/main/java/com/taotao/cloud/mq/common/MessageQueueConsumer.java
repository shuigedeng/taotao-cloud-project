package com.taotao.cloud.mq.common;


import com.taotao.cloud.mq.common.consumer.Acknowledgement;
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
