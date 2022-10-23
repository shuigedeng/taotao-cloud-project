package com.taotao.cloud.redis.delay.config;

import com.taotao.cloud.redis.delay.handler.IsolationStrategy;
import com.taotao.cloud.redis.delay.message.MessageConverter;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;

/**
 * QueueRegistryInfo 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-18 10:36:41
 */
public class QueueRegistryInfo {

    private String queueName;

    private String isolatedName;

    private RedissonQueue queue;

    private IsolationStrategy isolationHandler;

    private MessageConverter messageConverter;

    private RBlockingQueue<Object> blockingQueue;

    private RDelayedQueue<Object> delayedQueue;

    protected void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    protected void setIsolatedName(String isolatedName) {
        this.isolatedName = isolatedName;
    }

    protected void setQueue(RedissonQueue queue) {
        this.queue = queue;
    }

    protected void setIsolationHandler(IsolationStrategy isolationHandler) {
        this.isolationHandler = isolationHandler;
    }

    protected void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    protected void setBlockingQueue(RBlockingQueue<Object> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    protected void setDelayedQueue(RDelayedQueue<Object> delayedQueue) {
        this.delayedQueue = delayedQueue;
    }

	public String getQueueName() {
		return queueName;
	}

	public String getIsolatedName() {
		return isolatedName;
	}

	public RedissonQueue getQueue() {
		return queue;
	}

	public IsolationStrategy getIsolationHandler() {
		return isolationHandler;
	}

	public MessageConverter getMessageConverter() {
		return messageConverter;
	}

	public RBlockingQueue<Object> getBlockingQueue() {
		return blockingQueue;
	}

	public RDelayedQueue<Object> getDelayedQueue() {
		return delayedQueue;
	}
}
