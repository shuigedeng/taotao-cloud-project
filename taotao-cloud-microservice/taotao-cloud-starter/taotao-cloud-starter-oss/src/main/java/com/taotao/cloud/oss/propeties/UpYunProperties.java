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
package com.taotao.cloud.oss.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * UpYunProperties
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 09:39
 */
@RefreshScope
@ConfigurationProperties(prefix = UpYunProperties.PREFIX)
public class UpYunProperties {

	public static final String PREFIX = "taotao.cloud.oss.upyun";

	/**
	 * 服务名
	 */
	private String bucketName;

	/**
	 * 操作员名称
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 图片对外域名
	 */
	private String domain;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
