package com.taotao.cloud.redis.delay.listener;


public class DefaultRedissonListenerContainerFactory implements RedissonListenerContainerFactory {

	@Override
	public RedissonListenerContainer createListenerContainer(
		ContainerProperties containerProperties) {
		int concurrency = containerProperties.getConcurrency();

		return new ConcurrentRedissonListenerContainer(containerProperties,
			Math.max(concurrency, 1));
	}

}
