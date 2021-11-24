/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.zookeeper.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ZookeeperProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:39:04
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

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
