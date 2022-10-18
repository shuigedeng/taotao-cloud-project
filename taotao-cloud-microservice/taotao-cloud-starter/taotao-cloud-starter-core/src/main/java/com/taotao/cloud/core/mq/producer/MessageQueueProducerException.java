package com.taotao.cloud.core.mq.producer;


import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * 消息队列生产者异常
 */
public class MessageQueueProducerException extends BaseException {

	public MessageQueueProducerException(String message) {
		super(message);
	}

	public MessageQueueProducerException(Integer code, String message) {
		super(code, message);
	}

	public MessageQueueProducerException(Throwable e) {
		super(e);
	}

	public MessageQueueProducerException(String message, Throwable e) {
		super(message, e);
	}

	public MessageQueueProducerException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public MessageQueueProducerException(ResultEnum result) {
		super(result);
	}

	public MessageQueueProducerException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
