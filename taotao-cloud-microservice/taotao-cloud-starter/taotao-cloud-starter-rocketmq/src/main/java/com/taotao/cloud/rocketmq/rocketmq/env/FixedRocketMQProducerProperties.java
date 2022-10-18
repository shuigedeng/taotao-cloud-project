package com.taotao.cloud.rocketmq.rocketmq.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 */
@ConfigurationProperties(prefix = "taotao.cloud.mq.rocketmq.producer")
public class FixedRocketMQProducerProperties {

	/**
	 * Namespace for this MQ Producer instance.
	 */
	private String namespace;

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
