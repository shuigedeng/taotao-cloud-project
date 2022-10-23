package com.taotao.cloud.redis.delay.listener;


/**
 * DefaultRedissonListenerContainerFactory 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:42
 */
public class DefaultRedissonListenerContainerFactory implements RedissonListenerContainerFactory {

	@Override
	public RedissonListenerContainer createListenerContainer(
		ContainerProperties containerProperties) {
		int concurrency = containerProperties.getConcurrency();

		return new ConcurrentRedissonListenerContainer(containerProperties,
			Math.max(concurrency, 1));
	}

}
