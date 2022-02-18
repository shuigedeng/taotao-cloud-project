package com.taotao.cloud.redis.delay.message;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

/**
 * RedissonMessage 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class RedissonMessage {

	private final String payload;

	private final Map<String, Object> headers;

	public RedissonMessage(String payload, Map<String, Object> headers) {
		Assert.notNull(payload, "payload must not be null");
		this.payload = payload;
		if (headers == null) {
			headers = new HashMap<>();
		}
		this.headers = headers;
	}

	public String getPayload() {
		return this.payload;
	}

	public Map<String, Object> getHeaders() {
		return this.headers;
	}

}
