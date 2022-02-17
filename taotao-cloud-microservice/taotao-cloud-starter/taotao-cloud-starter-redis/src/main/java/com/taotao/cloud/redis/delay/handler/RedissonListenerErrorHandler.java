package com.taotao.cloud.redis.delay.handler;

import com.taotao.cloud.redis.delay.message.RedissonMessage;
import org.springframework.messaging.Message;


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
