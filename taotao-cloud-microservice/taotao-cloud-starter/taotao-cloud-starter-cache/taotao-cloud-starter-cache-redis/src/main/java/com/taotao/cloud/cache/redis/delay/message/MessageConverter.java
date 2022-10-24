package com.taotao.cloud.cache.redis.delay.message;


import com.taotao.cloud.cache.redis.delay.MessageConversionException;
import java.util.Map;

/**
 * MessageConverter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public interface MessageConverter {

	/**
	 * convert payload and headers to message that can send to redis queue directly
	 *
	 * @param payload payload
	 * @param headers headers
	 * @return mq message
	 * @throws MessageConversionException when can't convert
	 */
	QueueMessage<?> toMessage(Object payload, Map<String, Object> headers)
		throws MessageConversionException;

	/**
	 * convert redisson message to required object
	 *
	 * @param redissonMessage redisson message
	 * @return entity
	 * @throws MessageConversionException when can't convert
	 */
	Object fromMessage(RedissonMessage redissonMessage) throws MessageConversionException;

}
