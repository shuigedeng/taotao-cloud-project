package com.taotao.cloud.redis.delay.message;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;


public class RedissonMessage {

	private final byte[] payload;

	private final Map<String, Object> headers;

	public RedissonMessage(byte[] payload, Map<String, Object> headers) {
		Assert.notNull(payload, "payload must not be null");
		this.payload = payload;
		if (headers == null) {
			headers = new HashMap<>();
		}
		this.headers = headers;
	}

	public byte[] getPayload() {
		return this.payload;
	}

	public Map<String, Object> getHeaders() {
		return this.headers;
	}

}
