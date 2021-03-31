package com.taotao.cloud.rocketmq.channel;

import com.taotao.cloud.rocketmq.constant.MessageConstant;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 生产者Channel
 */
public interface MateSource {

	/**
	 * 短消息通道
	 *
	 * @return MessageChannel
	 */
	@Output(MessageConstant.SMS_MESSAGE_OUTPUT)
	MessageChannel smsOutput();

	/**
	 * 邮件通道
	 *
	 * @return MessageChannel
	 */
	@Output(MessageConstant.EMAIL_MESSAGE_OUTPUT)
	MessageChannel emailOutput();

	/**
	 * 订单通道
	 *
	 * @return MessageChannel
	 */
	@Output(MessageConstant.ORDER_MESSAGE_OUTPUT)
	MessageChannel orderOutput();
}
