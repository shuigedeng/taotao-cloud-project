package com.taotao.cloud.order.biz.stream.kafka;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSource;
import jakarta.annotation.Resource;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class OrderProvider {

	@Resource
	private TaoTaoCloudSource source;

	public String send(String content) {
		source.orderOutput().send(MessageBuilder.withPayload(content)
				//.setHeader("routingKey", "login.user.succeed")
				//.setHeader("version", "1.0")
				//.setHeader("x-delay", 5000)
				.build());
		return "success";
	}
}
