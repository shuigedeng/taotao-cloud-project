package com.taotao.cloud.redis.delay.listener;



import com.taotao.cloud.redis.delay.exception.MessageConversionException;
import com.taotao.cloud.redis.delay.message.MessageConverter;
import com.taotao.cloud.redis.delay.message.QueueMessage;
import com.taotao.cloud.redis.delay.message.RedissonHeaders;
import com.taotao.cloud.redis.delay.message.RedissonMessage;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public abstract class AbstractRedissonMessageListenerAdapter<T> implements RedissonMessageListener<T> {

    protected static class SimpleMessageConverter implements MessageConverter {

        @Override
        public QueueMessage<?> toMessage(Object payload, Map<String, Object> headers) throws MessageConversionException {
            return null;
        }

        @Override
        public String fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
            String charset = (String) redissonMessage.getHeaders().getOrDefault(RedissonHeaders.CHARSET_NAME, StandardCharsets.UTF_8.name());
            return new String(redissonMessage.getPayload(), Charset.forName(charset));
        }
    }

}
