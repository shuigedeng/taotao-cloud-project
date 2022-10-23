package com.taotao.cloud.mq.rocketmq.transaction;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SpringTransactionProducer {

	@Autowired
	private RocketMQTemplate rocketMQTemplate;

	/**
	 * 发送消息
	 */
	public void sendMsg(String topic, String msg) {
		Message<String> message = MessageBuilder.withPayload(msg).build();
		this.rocketMQTemplate.sendMessageInTransaction("transaction-producer-group", topic, message, null);
		log.info("发送成功");
	}
}
