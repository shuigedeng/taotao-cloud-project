package com.taotao.cloud.cache.redis.delay.message;

import java.util.Map;
import org.springframework.util.Assert;

/**
 * QueueMessage 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class QueueMessage<T> {

	private final T payload;

	private final Map<String, Object> headers;

	public QueueMessage(T payload, Map<String, Object> headers) {
		Assert.notNull(payload, "payload must not be null");
		this.payload = payload;
		this.headers = headers;
	}

	public T getPayload() {
		return payload;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

}
