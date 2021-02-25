package com.taotao.cloud.demo.rocketmq.producer.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 配置消息生产者
 */
@EnableBinding({RocketMqConfig.MySource.class})
public class RocketMqConfig {

	public interface MySource {
		@Output("output3")
		MessageChannel output();
	}

}
