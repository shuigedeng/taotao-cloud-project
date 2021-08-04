package com.taotao.cloud.zookeeper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * zookeeper配置
 */
@RefreshScope
@ConfigurationProperties(prefix = ZookeeperProperties.PREFIX)
public class ZookeeperProperties {

	public static final String PREFIX = "taotao.cloud.zookeeper";


	private boolean enabled = false;

	/**
	 * zk连接集群，多个用逗号隔开
	 */
	private String connectString = "127.0.0.1:2181";

	/**
	 * 会话超时时间(毫秒)
	 */
	private int sessionTimeout = 15000;

	/**
	 * 连接超时时间(毫秒)
	 */
	private int connectionTimeout = 15000;

	/**
	 * 初始重试等待时间(毫秒)
	 */
	private int baseSleepTime = 2000;

	/**
	 * 重试最大次数
	 */
	private int maxRetries = 10;

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getBaseSleepTime() {
		return baseSleepTime;
	}

	public void setBaseSleepTime(int baseSleepTime) {
		this.baseSleepTime = baseSleepTime;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
