package com.taotao.cloud.cache.redis.delay.listener;


import com.taotao.cloud.cache.redis.delay.MessageConversionException;
import com.taotao.cloud.cache.redis.delay.handler.RedissonListenerErrorHandler;
import com.taotao.cloud.cache.redis.delay.message.MessageConverter;
import com.taotao.cloud.cache.redis.delay.message.QueueMessage;
import com.taotao.cloud.cache.redis.delay.message.RedissonHeaders;
import com.taotao.cloud.cache.redis.delay.message.RedissonMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.util.Assert;

/**
 * BatchRedissonMessageListenerAdapter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class BatchRedissonMessageListenerAdapter extends
	AbstractRedissonMessageListenerAdapter<List<RedissonMessage>> {

	private final InvocableHandlerMethod invocableHandlerMethod;

	private final RedissonListenerErrorHandler errorHandler;

	private BatchMessagingMessageConverter batchMessagingMessageConverter;

	public BatchRedissonMessageListenerAdapter(InvocableHandlerMethod invocableHandlerMethod,
		MessageConverter messageConverter) {
		this(invocableHandlerMethod, messageConverter, null);
	}

	public BatchRedissonMessageListenerAdapter(InvocableHandlerMethod invocableHandlerMethod,
		MessageConverter messageConverter,
		RedissonListenerErrorHandler errorHandler) {
		this.invocableHandlerMethod = invocableHandlerMethod;
		this.errorHandler = errorHandler;
		MessageConverter payloadConverter = messageConverter;
		if (payloadConverter == null) {
			payloadConverter = new SimpleMessageConverter();
		}
		this.batchMessagingMessageConverter = new BatchMessagingMessageConverter(payloadConverter);
	}

	@Override
	public void onMessage(List<RedissonMessage> redissonMessage) throws Exception {
		Message<?> message = this.batchMessagingMessageConverter.fromMessage(redissonMessage);
		try {
			this.invocableHandlerMethod.invoke(message);
		} catch (Exception e) {
			if (this.errorHandler != null) {
				this.errorHandler.handleError(null, message, e);
			} else {
				throw e;
			}
		}
	}

	private static class BatchMessagingMessageConverter implements BatchMessageConverter {

		private final MessageConverter payloadConverter;

		private BatchMessagingMessageConverter(MessageConverter payloadConverter) {
			Assert.notNull(payloadConverter, "payloadConverter must not be null");
			this.payloadConverter = payloadConverter;
		}

		@Override
		public QueueMessage<?> toMessage(Object payload, Map<String, Object> headers)
			throws MessageConversionException {
			return null;
		}

		@Override
		public List<QueueMessage<?>> toListMessage(Object payload, Map<String, Object> headers) {
			return null;
		}

		@Override
		public Message<?> fromMessage(List<RedissonMessage> redissonMessages)
			throws MessageConversionException {
			List<Object> payloads = new ArrayList<>();
			Map<String, Object> headers = new HashMap<>(4);
			List<Map<String, Object>> batchConvertedHeaders = new ArrayList<>();
			headers.put(RedissonHeaders.RECEIVED_TIMESTAMP, System.currentTimeMillis());
			headers.put(RedissonHeaders.BATCH_CONVERTED_HEADERS, batchConvertedHeaders);
			redissonMessages.forEach(redissonMessage -> {
				Map<String, Object> rawHeaders = redissonMessage.getHeaders();
				Object convertedPayload = this.payloadConverter.fromMessage(redissonMessage);
				Object payload = convertedPayload;
				Map<String, Object> convertedHeaders = rawHeaders;
				if (convertedPayload instanceof Message) {
					Message convertedMessage = (Message) convertedPayload;
					payload = convertedMessage.getPayload();
					convertedHeaders = convertedMessage.getHeaders();
				}
				payloads.add(payload);
				batchConvertedHeaders.add(convertedHeaders);
				headers.putIfAbsent(RedissonHeaders.DELIVERY_QUEUE_NAME,
					rawHeaders.get(RedissonHeaders.DELIVERY_QUEUE_NAME));
			});
			return new GenericMessage<>(payloads, headers);
		}
	}

}
