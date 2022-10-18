package com.taotao.cloud.core.mq;


import com.taotao.cloud.core.mq.producer.MessageQueueProducerException;
import com.taotao.cloud.core.mq.producer.MessageSendCallback;
import com.taotao.cloud.core.mq.producer.MessageSendResult;

/**
 * 消息队列生产者
 */
public interface MessageQueueProvider {

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 * @throws MessageQueueProducerException
	 */
	MessageSendResult syncSend(Message message) throws MessageQueueProducerException;

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 * @throws MessageQueueProducerException
	 */
	void asyncSend(Message message, MessageSendCallback messageCallback)
		throws MessageQueueProducerException;
}
