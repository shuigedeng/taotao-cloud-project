package com.taotao.cloud.cache.redis.delay.handler;

import com.taotao.cloud.cache.redis.delay.message.RedissonMessage;
import org.springframework.messaging.Message;

/**
 * RedissonListenerErrorHandler 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
@FunctionalInterface
public interface RedissonListenerErrorHandler {

    /**
     * error handler
     *
     * @param message          redisson message
     * @param messagingMessage spring message
     * @param throwable        throwable
     */
    void handleError(RedissonMessage message, Message<?> messagingMessage, Throwable throwable);

}
