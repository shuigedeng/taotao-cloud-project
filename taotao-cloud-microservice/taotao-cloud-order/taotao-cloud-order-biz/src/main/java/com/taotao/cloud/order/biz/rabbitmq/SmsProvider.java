
package com.taotao.cloud.order.biz.rabbitmq;

import com.taotao.cloud.rocketmq.channel.TaoTaoCloudSource;
import java.util.UUID;
import javax.annotation.Resource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class SmsProvider {

	@Resource
	private TaoTaoCloudSource source;

	public String send() {
		source.smsOutput().send(MessageBuilder.withPayload(UUID.randomUUID().toString())
			.setHeader("routingKey", "login.user.succeed")
			.setHeader("version", "1.0")
			.setHeader("x-delay", 5000)
			.build());
		return "success";
	}
}
