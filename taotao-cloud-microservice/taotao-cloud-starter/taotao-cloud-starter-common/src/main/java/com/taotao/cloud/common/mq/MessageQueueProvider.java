package com.taotao.cloud.common.mq;


import com.taotao.cloud.common.mq.producer.MessageQueueProducerException;
import com.taotao.cloud.common.mq.producer.MessageSendCallback;
import com.taotao.cloud.common.mq.producer.MessageSendResult;

/**
 * 消息队列生产者
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
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
