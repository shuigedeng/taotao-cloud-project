package com.taotao.cloud.order.biz.kafka;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSink;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

	//, @Payload String msg
	@StreamListener(value=TaoTaoCloudSink.ORDER_MESSAGE_INPUT)
	public void test(Message<String> message) {
		String payload = message.getPayload();
		System.out.println("order Consumer"+payload);
	}


}
