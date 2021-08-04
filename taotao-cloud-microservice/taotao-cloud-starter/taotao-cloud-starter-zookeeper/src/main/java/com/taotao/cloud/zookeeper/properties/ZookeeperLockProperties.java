package com.taotao.cloud.zookeeper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * zookeeper配置
 */
@RefreshScope
@ConfigurationProperties(prefix = ZookeeperLockProperties.PREFIX)
public class ZookeeperLockProperties {

	public static final String PREFIX = "taotao.cloud.zookeeper.lock";

	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
