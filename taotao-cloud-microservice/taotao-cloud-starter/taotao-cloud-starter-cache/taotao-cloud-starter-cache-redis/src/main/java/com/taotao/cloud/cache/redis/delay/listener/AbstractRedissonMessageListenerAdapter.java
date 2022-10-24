package com.taotao.cloud.cache.redis.delay.listener;



import com.taotao.cloud.cache.redis.delay.MessageConversionException;
import com.taotao.cloud.cache.redis.delay.message.MessageConverter;
import com.taotao.cloud.cache.redis.delay.message.QueueMessage;
import com.taotao.cloud.cache.redis.delay.message.RedissonHeaders;
import com.taotao.cloud.cache.redis.delay.message.RedissonMessage;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * AbstractRedissonMessageListenerAdapter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public abstract class AbstractRedissonMessageListenerAdapter<T> implements RedissonMessageListener<T> {

    protected static class SimpleMessageConverter implements MessageConverter {

        @Override
        public QueueMessage<?> toMessage(Object payload, Map<String, Object> headers) throws MessageConversionException {
            return null;
        }

        @Override
        public String fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
            String charset = (String) redissonMessage.getHeaders().getOrDefault(RedissonHeaders.CHARSET_NAME, StandardCharsets.UTF_8.name());
            return new String(redissonMessage.getPayload());
        }
    }

}
