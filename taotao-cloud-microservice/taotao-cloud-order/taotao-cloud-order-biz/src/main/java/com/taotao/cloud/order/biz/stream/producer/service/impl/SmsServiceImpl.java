package com.taotao.cloud.order.biz.stream.producer.service.impl;

import com.taotao.cloud.mq.pulsar.constant.MessageConstant;
import com.taotao.cloud.order.biz.stream.producer.service.ISmsService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/**
 * 发送短信实现类
 */
@Service
@AllArgsConstructor
public class SmsServiceImpl implements ISmsService {

	private final StreamBridge streamBridge;

	/**
	 * 采用StreamBridge的发送方式
	 *
	 * @param message 　短消息
	 * @link
	 * https://docs.spring.io/spring-cloud-stream/docs/3.1.0/reference/html/spring-cloud-stream.html#_binding_and_binding_names
	 */
	@Override
	public void sendSms(String message) {
		streamBridge.send(MessageConstant.SMS_MESSAGE_OUTPUT, message);
	}
}
