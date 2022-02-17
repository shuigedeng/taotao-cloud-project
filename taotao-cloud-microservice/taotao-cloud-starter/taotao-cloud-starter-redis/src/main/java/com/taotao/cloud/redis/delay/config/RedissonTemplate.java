package com.taotao.cloud.redis.delay.config;

import com.taotao.cloud.redis.delay.message.DefaultRedissonMessageConverter;
import com.taotao.cloud.redis.delay.message.MessageConverter;
import com.taotao.cloud.redis.delay.message.QueueMessage;
import com.taotao.cloud.redis.delay.message.RedissonHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.util.Assert;


public class RedissonTemplate implements BeanFactoryAware, SmartInitializingSingleton {

	private BeanFactory beanFactory;

	private RedissonQueueRegistry redissonQueueRegistry;
	private MessageConverter globalMessageConverter = new DefaultRedissonMessageConverter();

	public MessageConverter getGlobalMessageConverter() {
		return globalMessageConverter;
	}

	public void setGlobalMessageConverter(MessageConverter globalMessageConverter) {
		Assert.notNull(globalMessageConverter, "MessageConverter must not be null");
		this.globalMessageConverter = globalMessageConverter;
	}

	public void send(final String queueName, final Object payload) {
		this.send(queueName, payload, new HashMap<>(8));
	}

	public void send(final String queueName, final Object payload, Map<String, Object> headers) {
		this.checkQueueAndPayload(queueName, payload);

		final QueueRegistryInfo registryInfo = this.checkAndGetRegistryInfo(queueName);
		final RBlockingQueue<Object> blockingQueue = registryInfo.getBlockingQueue();
		final MessageConverter messageConverter = this.getRequiredMessageConverter(queueName);

		this.fillInfrastructureHeaders(queueName, headers);
		QueueMessage<?> message = messageConverter.toMessage(payload, headers);
		blockingQueue.offer(message);
	}


	public void sendWithDelay(final String queueName, final Object payload, final long delay) {
		this.sendWithDelay(queueName, payload, new HashMap<>(8), delay);
	}

	public void sendWithDelay(final String queueName, final Object payload,
		Map<String, Object> headers, final long delay) {
		this.checkQueueAndPayload(queueName, payload);
		Assert.isTrue(delay > 0, "delay millis must be positive");

		final QueueRegistryInfo registryInfo = this.checkAndGetRegistryInfo(queueName);
		final RDelayedQueue<Object> delayedQueue = registryInfo.getDelayedQueue();
		Assert.notNull(delayedQueue, "the delay queue doesn't define");
		final MessageConverter messageConverter = this.getRequiredMessageConverter(queueName);

		this.fillInfrastructureHeaders(queueName, headers);
		headers.put(RedissonHeaders.EXPECTED_DELAY_MILLIS, delay);
		QueueMessage<?> message = messageConverter.toMessage(payload, headers);
		delayedQueue.offer(message, delay, TimeUnit.MILLISECONDS);
	}

	private void checkQueueAndPayload(String queueName, Object payload) {
		Assert.hasText(queueName, "queueName must not be empty");
		Assert.notNull(payload, "payload must not be null");
	}

	private QueueRegistryInfo checkAndGetRegistryInfo(String queueName) {
		QueueRegistryInfo registryInfo = this.redissonQueueRegistry.getRegistryInfo(queueName);
		Assert.notNull(registryInfo, "queue not registered");
		RBlockingQueue blockingQueue = registryInfo.getBlockingQueue();
		Assert.notNull(blockingQueue, "target queue doesn't define");
		return registryInfo;
	}

	private MessageConverter getRequiredMessageConverter(String queueName) {
		final QueueRegistryInfo registryInfo = this.redissonQueueRegistry.getRegistryInfo(
			queueName);
		MessageConverter messageConverter = registryInfo.getMessageConverter();
		if (messageConverter == null) {
			messageConverter = this.globalMessageConverter;
		}
		return messageConverter;
	}

	private void fillInfrastructureHeaders(final String queueName,
		final Map<String, Object> headers) {
		headers.put(RedissonHeaders.DELIVERY_QUEUE_NAME, queueName);
		headers.put(RedissonHeaders.SEND_TIMESTAMP, System.currentTimeMillis());
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterSingletonsInstantiated() {
		this.redissonQueueRegistry = this.beanFactory.getBean(
			RedissonConfigUtils.REDISSON_QUEUE_REGISTRY_BEAN_NAME, RedissonQueueRegistry.class);
	}

}
