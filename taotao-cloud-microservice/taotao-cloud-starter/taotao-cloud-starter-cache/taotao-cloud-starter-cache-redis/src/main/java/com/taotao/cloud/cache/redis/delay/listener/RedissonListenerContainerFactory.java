package com.taotao.cloud.cache.redis.delay.listener;

/**
 * RedissonListenerContainerFactory 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:42
 */
public interface RedissonListenerContainerFactory {

    RedissonListenerContainer createListenerContainer(ContainerProperties containerProperties);

}
