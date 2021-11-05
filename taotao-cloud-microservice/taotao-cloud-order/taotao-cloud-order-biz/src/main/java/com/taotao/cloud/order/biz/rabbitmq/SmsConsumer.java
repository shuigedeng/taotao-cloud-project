package com.taotao.cloud.order.biz.rabbitmq;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class SmsConsumer {

	//, @Payload String msg
	//@StreamListener(value=TaoTaoCloudSink.SMS_MESSAGE_INPUT, condition = "headers['version']=='1.0'")
	@StreamListener(value=TaoTaoCloudSink.SMS_MESSAGE_INPUT)
	public void test(Message<String> message) {
		String payload = message.getPayload();
		System.out.println("sms Consumer"+payload);
	}


}
