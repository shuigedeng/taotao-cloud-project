package com.taotao.cloud.cache.redis.delay.config;

import com.taotao.cloud.cache.redis.delay.handler.IsolationStrategy;
import com.taotao.cloud.cache.redis.delay.message.FastJsonCodec;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

/**
 * RedissonQueueBeanPostProcessor
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:24:26
 */
public class RedissonQueueBeanPostProcessor implements BeanFactoryAware, BeanPostProcessor {

	private BeanFactory beanFactory;

	private RedissonClient redissonClient;

	private RedissonQueueRegistry redissonQueueRegistry;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
		this.redissonClient = this.beanFactory.getBean(RedissonClient.class);
		this.redissonQueueRegistry = this.beanFactory.getBean(
			RedissonConfigUtils.REDISSON_QUEUE_REGISTRY_BEAN_NAME, RedissonQueueRegistry.class);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
		throws BeansException {
		if (bean instanceof RedissonQueue) {
			final RedissonQueue redissonQueue = (RedissonQueue) bean;
			final QueueRegistryInfo registryInfo = new QueueRegistryInfo();
			final String queueName = redissonQueue.getQueue();
			final QueueRegistryInfo registeredInfo = this.redissonQueueRegistry.getRegistryInfo(
				queueName);

			if (registeredInfo != null) {
				throw new BeanDefinitionValidationException(
					"duplicate bean of RedissonQueue named [" + queueName + "]");
			}

			final IsolationStrategy isolationHandler = redissonQueue.getIsolationHandler();
			final String isolatedName = isolationHandler == null ? queueName
				: isolationHandler.getRedisQueueName(queueName);
			final RBlockingQueue<Object> blockingQueue = this.redissonClient.getBlockingQueue(
				isolatedName, FastJsonCodec.INSTANCE);
			RDelayedQueue<Object> delayedQueue = null;
			if (redissonQueue.getDelay()) {
				delayedQueue = this.redissonClient.getDelayedQueue(blockingQueue);
			}

			registryInfo.setQueueName(queueName);
			registryInfo.setIsolatedName(isolatedName);
			registryInfo.setQueue(redissonQueue);
			registryInfo.setIsolationHandler(isolationHandler);
			registryInfo.setMessageConverter(redissonQueue.getMessageConverter());
			registryInfo.setBlockingQueue(blockingQueue);
			registryInfo.setDelayedQueue(delayedQueue);
			this.redissonQueueRegistry.registerQueueInfo(queueName, registryInfo);
		}
		return bean;
	}

}
