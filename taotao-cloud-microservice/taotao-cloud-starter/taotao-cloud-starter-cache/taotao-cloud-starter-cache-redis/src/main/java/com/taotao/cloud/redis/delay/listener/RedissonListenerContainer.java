package com.taotao.cloud.redis.delay.listener;

import org.redisson.api.RedissonClient;
import org.springframework.context.SmartLifecycle;

/**
 * RedissonListenerContainer 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:42
 */
public interface RedissonListenerContainer extends SmartLifecycle {

    ContainerProperties getContainerProperties();

    void setListener(RedissonMessageListener<?> listener);

    void setRedissonClient(RedissonClient redissonClient);

}
