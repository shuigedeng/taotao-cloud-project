package com.taotao.cloud.mq.rocketmq.transaction;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "pay_topic",
	consumerGroup = "transaction-consumer-group",
	selectorExpression = "*")
@Slf4j
public class SpringTxConsumer implements RocketMQListener<String> {

	@Override
	public void onMessage(String msg) {
		log.info("接收到消息 -> {}", msg);
	}
}
