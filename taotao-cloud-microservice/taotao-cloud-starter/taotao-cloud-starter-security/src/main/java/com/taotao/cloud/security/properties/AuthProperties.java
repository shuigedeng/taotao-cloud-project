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
package com.taotao.cloud.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 认证配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/30 08:52
 */
@Data
@ConfigurationProperties(prefix = "taotao.cloud.oauth2.security.auth")
public class AuthProperties {

	/**
	 * 配置要认证的url（默认不需要配置）
	 * <p>
	 * 优先级大于忽略认证配置`taotao.security.ignore.httpUrls` 意思是如果同一个url同时配置了`忽略认证`和`需要认证`，则该url还是会被认证
	 */
	private String[] httpUrls = {};

	/**
	 * token自动续签配置（目前只有redis实现）
	 */
	private RenewProperties renew = new RenewProperties();

	/**
	 * url权限配置
	 */
	private UrlPermissionProperties urlPermission = new UrlPermissionProperties();
}
