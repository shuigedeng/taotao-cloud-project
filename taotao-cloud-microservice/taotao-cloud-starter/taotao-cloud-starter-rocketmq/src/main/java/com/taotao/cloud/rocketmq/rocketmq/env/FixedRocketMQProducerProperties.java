package com.taotao.cloud.rocketmq.rocketmq.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 完善 RocketMQ 4.7.x 版本的配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConfigurationProperties(prefix = "rocketmq.producer")
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
