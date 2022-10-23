package com.taotao.cloud.mq.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 消息队列配置
 */
@ConfigurationProperties(prefix = MessageQueueProperties.PREFIX)
public class MessageQueueProperties {

	public static final String PREFIX = "taotao.cloud.mq";

	public static final String ENABLED = PREFIX + ".enabled";

	private String type;

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
