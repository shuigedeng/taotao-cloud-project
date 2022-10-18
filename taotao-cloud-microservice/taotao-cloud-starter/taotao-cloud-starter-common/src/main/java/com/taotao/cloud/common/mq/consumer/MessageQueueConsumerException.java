package com.taotao.cloud.common.mq.consumer;


import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BaseException;

/**
 * 消息队列消费者异常
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class MessageQueueConsumerException extends BaseException {


	public MessageQueueConsumerException(String message) {
		super(message);
	}

	public MessageQueueConsumerException(Integer code, String message) {
		super(code, message);
	}

	public MessageQueueConsumerException(Throwable e) {
		super(e);
	}

	public MessageQueueConsumerException(String message, Throwable e) {
		super(message, e);
	}

	public MessageQueueConsumerException(Integer code, String message, Throwable e) {
		super(code, message, e);
	}

	public MessageQueueConsumerException(ResultEnum result) {
		super(result);
	}

	public MessageQueueConsumerException(ResultEnum result, Throwable e) {
		super(result, e);
	}
}
