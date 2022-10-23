package com.taotao.cloud.mq.rocketmq.rocketmq.core;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mq.common.Message;
import com.taotao.cloud.mq.common.MessageQueueProvider;
import com.taotao.cloud.mq.common.producer.MessageQueueProducerException;
import com.taotao.cloud.mq.common.producer.MessageSendCallback;
import com.taotao.cloud.mq.common.producer.MessageSendResult;
import com.taotao.cloud.mq.rocketmq.rocketmq.env.FixedRocketMQProducerProperties;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * RockMQ 生产者
 */
public class RocketMQProvider implements MessageQueueProvider {

	private static final String ROCKETMQ_PROVIDER_SEND_INTERRUPTED = "RocketMQProvider send interrupted: {}";

	private static final String ROCKETMQ_PROVIDER_CONSUME_ERROR = "RocketMQProvider send error: {}";

	private final RocketMQTemplate rocketMQTemplate;

	private final FixedRocketMQProducerProperties fixedRocketMQProducerProperties;

	public RocketMQProvider(RocketMQTemplate rocketMQTemplate,
		FixedRocketMQProducerProperties fixedRocketMQProducerProperties) {
		this.rocketMQTemplate = rocketMQTemplate;
		this.fixedRocketMQProducerProperties = fixedRocketMQProducerProperties;
	}

	/**
	 * 同步发送消息
	 *
	 * @param message
	 * @return
	 * @throws MessageQueueProducerException
	 */
	@Override
	public MessageSendResult syncSend(Message message) throws MessageQueueProducerException {
		try {
			SendResult sendResult = rocketMQTemplate.getProducer().send(transfer(message));
			return transfer(sendResult);
		} catch (InterruptedException e) {
			LogUtils.error(ROCKETMQ_PROVIDER_SEND_INTERRUPTED, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new MessageQueueProducerException(e.getMessage());
		} catch (Exception e) {
			LogUtils.error(ROCKETMQ_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageQueueProducerException(e.getMessage());
		}
	}

	/**
	 * 异步发送消息
	 *
	 * @param message
	 * @param messageCallback
	 * @throws MessageQueueProducerException
	 */
	@Override
	public void asyncSend(Message message, MessageSendCallback messageCallback)
		throws MessageQueueProducerException {
		DefaultMQProducer producer = rocketMQTemplate.getProducer();
		if (StringUtils.isNotBlank(message.getNamespace())) {
			producer.setNamespace(message.getNamespace());
		} else if (StringUtils.isNotBlank(fixedRocketMQProducerProperties.getNamespace())) {
			producer.setNamespace(fixedRocketMQProducerProperties.getNamespace());
		}

		try {
			producer.send(transfer(message), new SendCallback() {

				@Override
				public void onSuccess(SendResult sendResult) {
					messageCallback.onSuccess(transfer(sendResult));
				}

				@Override
				public void onException(Throwable e) {
					messageCallback.onFailed(e);
				}
			});
		} catch (InterruptedException e) {
			LogUtils.error(ROCKETMQ_PROVIDER_SEND_INTERRUPTED, e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new MessageQueueProducerException(e.getMessage());
		} catch (Exception e) {
			LogUtils.error(ROCKETMQ_PROVIDER_CONSUME_ERROR, e.getMessage(), e);
			throw new MessageQueueProducerException(e.getMessage());
		}
	}

	/**
	 * 转换为 RocketMQ 消息
	 *
	 * @param message
	 * @return
	 */
	private org.apache.rocketmq.common.message.Message transfer(Message message) {
		org.apache.rocketmq.common.message.Message rocketMsg =
			new org.apache.rocketmq.common.message.Message(message.getTopic(), message.getTags(),
				message.getKey(), message.getBody().getBytes(StandardCharsets.UTF_8));
		if (message.getDelayTimeLevel() > 0) {
			rocketMsg.setDelayTimeLevel(message.getDelayTimeLevel());
		}
		return rocketMsg;
	}

	/**
	 * 转化为自定义的 MessageSendResult
	 *
	 * @param sendResult
	 * @return
	 */
	private MessageSendResult transfer(SendResult sendResult) {
		MessageSendResult result = new MessageSendResult();
		result.setTopic(sendResult.getMessageQueue().getTopic());
		result.setPartition(sendResult.getMessageQueue().getQueueId());
		result.setOffset(sendResult.getQueueOffset());
		result.setTransactionId(sendResult.getTransactionId());
		return result;
	}
}
