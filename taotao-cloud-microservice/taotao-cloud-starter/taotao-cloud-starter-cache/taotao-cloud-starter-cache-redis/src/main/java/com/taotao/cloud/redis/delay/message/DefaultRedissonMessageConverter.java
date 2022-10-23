package com.taotao.cloud.redis.delay.message;


import com.taotao.cloud.redis.delay.MessageConversionException;
import java.util.Map;
import java.util.UUID;

/**
 * DefaultRedissonMessageConverter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class DefaultRedissonMessageConverter implements MessageConverter {

	@Override
	public QueueMessage<?> toMessage(Object payload, Map<String, Object> headers) {
		headers.put(RedissonHeaders.MESSAGE_ID, UUID.randomUUID().toString());
		return QueueMessageBuilder.withPayload(payload).headers(headers).build();
	}

	@Override
	public Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException {
		return new String(redissonMessage.getPayload());
	}

}
