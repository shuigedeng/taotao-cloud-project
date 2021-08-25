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
package com.taotao.cloud.file.propeties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ftpProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/26 09:39
 */
@RefreshScope
@ConfigurationProperties(prefix = FtpProperties.PREFIX)
public class FtpProperties {

	public static final String PREFIX = "taotao.cloud.file.ftp";

	private String host;

	private String port;

	private String username;

	private String password;

	private String domain;

	private String remoteDicrory;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getRemoteDicrory() {
		return remoteDicrory;
	}

	public void setRemoteDicrory(String remoteDicrory) {
		this.remoteDicrory = remoteDicrory;
	}
}
