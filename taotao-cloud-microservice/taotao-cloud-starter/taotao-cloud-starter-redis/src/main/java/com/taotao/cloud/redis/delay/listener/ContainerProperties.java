package com.taotao.cloud.redis.delay.listener;


import com.taotao.cloud.redis.delay.consts.ListenerType;
import com.taotao.cloud.redis.delay.handler.IsolationStrategy;
import com.taotao.cloud.redis.delay.handler.RedissonListenerErrorHandler;
import com.taotao.cloud.redis.delay.message.MessageConverter;

public class ContainerProperties {

    private String queue;

    private ListenerType listenerType;

    private RedissonListenerErrorHandler errorHandler;

    private IsolationStrategy isolationStrategy;

    private MessageConverter messageConverter;

    private int concurrency;

    private int maxFetch;

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public ListenerType getListenerType() {
		return listenerType;
	}

	public void setListenerType(ListenerType listenerType) {
		this.listenerType = listenerType;
	}

	public RedissonListenerErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(
		RedissonListenerErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public IsolationStrategy getIsolationStrategy() {
		return isolationStrategy;
	}

	public void setIsolationStrategy(
		IsolationStrategy isolationStrategy) {
		this.isolationStrategy = isolationStrategy;
	}

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	public void setMessageConverter(MessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	public int getConcurrency() {
		return concurrency;
	}

	public void setConcurrency(int concurrency) {
		this.concurrency = concurrency;
	}

	public int getMaxFetch() {
		return maxFetch;
	}

	public void setMaxFetch(int maxFetch) {
		this.maxFetch = maxFetch;
	}
}
