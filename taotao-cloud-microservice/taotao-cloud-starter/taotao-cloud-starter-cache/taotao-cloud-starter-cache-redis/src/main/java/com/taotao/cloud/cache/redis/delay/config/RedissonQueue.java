package com.taotao.cloud.cache.redis.delay.config;

import com.taotao.cloud.cache.redis.delay.handler.IsolationStrategy;
import com.taotao.cloud.cache.redis.delay.message.MessageConverter;

import java.util.Objects;
import org.springframework.util.Assert;


/**
 * RedissonQueue 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:42
 */
public class RedissonQueue {

	private final String queue;

	public String getQueue() {
		return queue;
	}

	private final boolean delay;

	public boolean getDelay() {
		return delay;
	}

	private final IsolationStrategy isolationHandler;

	public IsolationStrategy getIsolationHandler() {
		return isolationHandler;
	}

	private final MessageConverter messageConverter;

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	public RedissonQueue(String queue) {
		this(queue, false);
	}

	public RedissonQueue(String queue, boolean delay) {
		this(queue, delay, null);
	}

	public RedissonQueue(String queue, boolean delay, IsolationStrategy isolationHandler) {
		this(queue, delay, isolationHandler, null);
	}

	public RedissonQueue(String queue, boolean delay, IsolationStrategy isolationHandler,
		MessageConverter messageConverter) {
		Assert.hasText(queue, "queue name must not be empty");
		this.queue = queue;
		this.delay = delay;
		this.isolationHandler = isolationHandler;
		this.messageConverter = messageConverter;
	}

	@Override
	public int hashCode() {
		return this.queue.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RedissonQueue that = (RedissonQueue) o;
		return Objects.equals(this.queue, that.queue);
	}

}
