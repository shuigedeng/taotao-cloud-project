package com.taotao.cloud.redis.delay.message;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;


public class QueueMessageBuilder<T> {

	private T payload;

	private Map<String, Object> headers;

	public static <T> QueueMessageBuilder<T> withPayload(T payload) {
		Assert.notNull(payload, "payload must not be null");
		QueueMessageBuilder<T> builder = new QueueMessageBuilder<>();
		builder.payload = payload;
		return builder;
	}

	public QueueMessageBuilder<T> headers(Map<String, Object> headers) {
		if (headers == null) {
			headers = new HashMap<>();
		}
		this.headers = headers;
		return this;
	}

	public QueueMessage<T> build() {
		return new QueueMessage<>(this.payload, this.headers);
	}

}
