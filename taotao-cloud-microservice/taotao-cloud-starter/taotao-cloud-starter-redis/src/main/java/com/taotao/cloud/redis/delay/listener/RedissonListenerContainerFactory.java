package com.taotao.cloud.redis.delay.listener;


public interface RedissonListenerContainerFactory {

    RedissonListenerContainer createListenerContainer(ContainerProperties containerProperties);

}
