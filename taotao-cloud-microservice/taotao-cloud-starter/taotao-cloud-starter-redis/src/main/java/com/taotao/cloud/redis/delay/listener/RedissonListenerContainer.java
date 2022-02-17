package com.taotao.cloud.redis.delay.listener;

import org.redisson.api.RedissonClient;
import org.springframework.context.SmartLifecycle;


public interface RedissonListenerContainer extends SmartLifecycle {

    ContainerProperties getContainerProperties();

    void setListener(RedissonMessageListener<?> listener);

    void setRedissonClient(RedissonClient redissonClient);

}
