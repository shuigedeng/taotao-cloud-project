package com.taotao.cloud.redis.delay.handler;

import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.redis.delay.config.RedissonTemplate;
import com.taotao.cloud.redis.delay.message.RedissonHeaders;
import com.taotao.cloud.redis.delay.message.RedissonMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * RequeueRedissonListenerErrorHandler
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:25:42
 */
public class RequeueRedissonListenerErrorHandler implements RedissonListenerErrorHandler {

	private static final long DEFAULT_MAX_REQUEUE_TIMES = 1000;

	private final RedissonTemplate redissonTemplate;

	private final long maxRequeueTimes;

	private final RequeueDelayStrategy delayStrategy;

	public RequeueRedissonListenerErrorHandler(RedissonTemplate redissonTemplate) {
		this(redissonTemplate, DEFAULT_MAX_REQUEUE_TIMES);
	}

	public RequeueRedissonListenerErrorHandler(RedissonTemplate redissonTemplate,
		long maxRequeueTimes) {
		this(redissonTemplate, maxRequeueTimes, new DefaultRequeueDelayStrategy());
	}

	public RequeueRedissonListenerErrorHandler(RedissonTemplate redissonTemplate,
		long maxRequeueTimes, RequeueDelayStrategy delayStrategy) {
		Assert.notNull(redissonTemplate, "redissonTemplate must not be null");
		Assert.isTrue(maxRequeueTimes > 0, "maxRequeueTimes must be positive");
		Assert.notNull(delayStrategy, "requeueDelayStrategy must not be null");
		this.redissonTemplate = redissonTemplate;
		this.maxRequeueTimes = maxRequeueTimes;
		this.delayStrategy = delayStrategy;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void handleError(RedissonMessage message, Message<?> messagingMessage,
		Throwable throwable) {
		Object payload = messagingMessage.getPayload();
		if (message == null && payload instanceof List) {
			List<?> payloadList = (List<?>) payload;
			List<Map<String, Object>> batchHeaders = (List) messagingMessage.getHeaders().get(
				RedissonHeaders.BATCH_CONVERTED_HEADERS);
			for (int i = 0; i < payloadList.size(); i++) {
				Object payloadToRequeue = payloadList.get(i);
				Map<String, Object> rawHeaders = batchHeaders.get(i);
				this.requeue(payloadToRequeue, new HashMap<>(rawHeaders), throwable);
			}
			return;
		}
		this.requeue(payload, new HashMap<>(messagingMessage.getHeaders()), throwable);
	}

	private void requeue(Object payload, Map<String, Object> headers, Throwable throwable) {
		final String queueName = (String) headers.get(RedissonHeaders.DELIVERY_QUEUE_NAME);
		if (!StringUtils.hasText(queueName)) {
			LogUtils.warn("message [{}] delivery queue name is empty, abandon it",
				JsonUtils.toJSONString(payload), throwable);
			return;
		}
		Long requeueTimes = getLongVal(headers.get(RedissonHeaders.REQUEUE_TIMES));
		if (requeueTimes < this.maxRequeueTimes) {
			headers.put(RedissonHeaders.REQUEUE_TIMES, ++requeueTimes);
		} else {
			LogUtils.warn("message [{}] reach the max requeue times, abandon it",
				JsonUtils.toJSONString(payload), throwable);
			return;
		}
		final long delay = this.delayStrategy.getDelay(payload, headers);
		//present as delay message
		if (delay > 0) {
			this.redissonTemplate.sendWithDelay(queueName, payload, headers, delay);
			return;
		}
		this.redissonTemplate.send(queueName, payload, headers);
	}

	private static Long getLongVal(Object target) {
		if (target == null) {
			return 0L;
		}
		if (target instanceof Number) {
			return ((Number) target).longValue();
		}
		return 0L;
	}

	public interface RequeueDelayStrategy {

		/**
		 * @param payload message body
		 * @param headers message headers
		 * @return the delay millis that expected
		 */
		long getDelay(Object payload, Map<String, Object> headers);
	}

	private static class DefaultRequeueDelayStrategy implements RequeueDelayStrategy {

		@Override
		public long getDelay(Object payload, Map<String, Object> headers) {
			Object delay = headers.get(RedissonHeaders.EXPECTED_DELAY_MILLIS);
			return RequeueRedissonListenerErrorHandler.getLongVal(delay);
		}
	}

}
