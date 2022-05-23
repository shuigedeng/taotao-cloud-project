/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * HttpClientProperties
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:43:08
 */
@RefreshScope
@ConfigurationProperties(prefix = HttpClientProperties.PREFIX)
public class HttpClientProperties {

	public static final String PREFIX = "taotao.cloud.core.httpclient";

	/**
	 * 是否开启httpclient
	 */
	private boolean enabled = true;

	/**
	 * Tcp是否粘包(批量封包发送)
	 */
	private boolean tcpNoDelay = true;

	/**
	 * 总连接池大小
	 */
	private int maxTotal = 500;

	/**
	 * 单个host连接池大小
	 */
	private int defaultMaxPerRoute = 500;

	/**
	 * 连接是否需要验证有效时间
	 */
	private int validateAfterInactivity = 10000;

	/**
	 * 连接超时时间 【常用】
	 */
	private int connectTimeout = 10000;

	/**
	 * socket通讯超时时间 【常用】
	 */
	private int socketTimeout = 15000;

	/**
	 * 请求从连接池获取超时时间
	 */
	private int connectionRequestTimeout = 2000;

	/**
	 * 连接池共享
	 */
	private boolean connectionManagerShared = true;

	/**
	 * 回收时间间隔 s
	 */
	private int evictIdleConnectionsTime = 60;

	/**
	 * 是否回收
	 */
	private boolean isEvictExpiredConnections = true;

	/**
	 * 长连接保持时间 s
	 */
	private int connectionTimeToLive = -1;

	/**
	 * 重试次数 【常用】
	 */
	private int retryCount = 3;

	public boolean getTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	public int getValidateAfterInactivity() {
		return validateAfterInactivity;
	}

	public void setValidateAfterInactivity(int validateAfterInactivity) {
		this.validateAfterInactivity = validateAfterInactivity;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public void setConnectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}

	public boolean isConnectionManagerShared() {
		return connectionManagerShared;
	}

	public void setConnectionManagerShared(boolean connectionManagerShared) {
		this.connectionManagerShared = connectionManagerShared;
	}

	public int getEvictIdleConnectionsTime() {
		return evictIdleConnectionsTime;
	}

	public void setEvictIdleConnectionsTime(int evictIdleConnectionsTime) {
		this.evictIdleConnectionsTime = evictIdleConnectionsTime;
	}

	public boolean isEvictExpiredConnections() {
		return isEvictExpiredConnections;
	}

	public void setEvictExpiredConnections(boolean evictExpiredConnections) {
		isEvictExpiredConnections = evictExpiredConnections;
	}

	public int getConnectionTimeToLive() {
		return connectionTimeToLive;
	}

	public void setConnectionTimeToLive(int connectionTimeToLive) {
		this.connectionTimeToLive = connectionTimeToLive;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}
}
