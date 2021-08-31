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
package com.taotao.cloud.job.elastic.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ZookeeperProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 20:33
 */
@RefreshScope
@ConfigurationProperties(prefix = ZookeeperProperties.PREFIX)
public class ZookeeperProperties {

	public static final String PREFIX = "taotao.cloud.job.elastic.zookeeper";

	/**
	 * 连接Zookeeper服务器的列表. 包括IP地址和端口号. 多个地址用逗号分隔. 如: host1:2181,host2:2181
	 */
	private String serverLists;

	/**
	 * 命名空间.
	 */
	private String namespace;

	/**
	 * 等待重试的间隔时间的初始值. 单位毫秒.
	 */
	private int baseSleepTimeMilliseconds = 1000;

	/**
	 * 等待重试的间隔时间的最大值. 单位毫秒.
	 */
	private int maxSleepTimeMilliseconds = 3000;

	/**
	 * 最大重试次数.
	 */
	private int maxRetries = 3;

	/**
	 * 会话超时时间. 单位毫秒.
	 */
	private int sessionTimeoutMilliseconds;

	/**
	 * 连接超时时间. 单位毫秒.
	 */
	private int connectionTimeoutMilliseconds;

	/**
	 * 连接Zookeeper的权限令牌. 缺省为不需要权限验证.
	 */
	private String digest;

	public String getServerLists() {
		return serverLists;
	}

	public void setServerLists(String serverLists) {
		this.serverLists = serverLists;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public int getBaseSleepTimeMilliseconds() {
		return baseSleepTimeMilliseconds;
	}

	public void setBaseSleepTimeMilliseconds(int baseSleepTimeMilliseconds) {
		this.baseSleepTimeMilliseconds = baseSleepTimeMilliseconds;
	}

	public int getMaxSleepTimeMilliseconds() {
		return maxSleepTimeMilliseconds;
	}

	public void setMaxSleepTimeMilliseconds(int maxSleepTimeMilliseconds) {
		this.maxSleepTimeMilliseconds = maxSleepTimeMilliseconds;
	}

	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}

	public int getSessionTimeoutMilliseconds() {
		return sessionTimeoutMilliseconds;
	}

	public void setSessionTimeoutMilliseconds(int sessionTimeoutMilliseconds) {
		this.sessionTimeoutMilliseconds = sessionTimeoutMilliseconds;
	}

	public int getConnectionTimeoutMilliseconds() {
		return connectionTimeoutMilliseconds;
	}

	public void setConnectionTimeoutMilliseconds(int connectionTimeoutMilliseconds) {
		this.connectionTimeoutMilliseconds = connectionTimeoutMilliseconds;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

}
