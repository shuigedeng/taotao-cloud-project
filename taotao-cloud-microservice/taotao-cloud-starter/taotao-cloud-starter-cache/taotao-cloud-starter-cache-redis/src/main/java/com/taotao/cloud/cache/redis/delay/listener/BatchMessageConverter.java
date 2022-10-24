package com.taotao.cloud.cache.redis.delay.listener;


import com.taotao.cloud.cache.redis.delay.MessageConversionException;
import com.taotao.cloud.cache.redis.delay.message.MessageConverter;
import com.taotao.cloud.cache.redis.delay.message.QueueMessage;
import com.taotao.cloud.cache.redis.delay.message.RedissonMessage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.Message;

/**
 * BatchMessageConverter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public interface BatchMessageConverter extends MessageConverter {

    @Override
    default QueueMessage<?> toMessage(Object payload, Map<String, Object> headers) throws MessageConversionException {
        throw new UnsupportedOperationException("please see [toListMessage] method");
    }

    @Override
    default Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
        return this.fromMessage(Collections.singletonList(redissonMessage));
    }

    List<QueueMessage<?>> toListMessage(Object payload, Map<String, Object> headers);

    Message<?> fromMessage(List<RedissonMessage> redissonMessages) throws MessageConversionException;

}
