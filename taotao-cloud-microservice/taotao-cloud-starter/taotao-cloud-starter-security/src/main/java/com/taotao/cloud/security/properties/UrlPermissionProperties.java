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

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * url权限配置
 *
 * @author dengtao
 * @version 1.0.0ø
 * @since 2020/5/2 11:21
 */
@Data
@ConfigurationProperties(prefix = "taotao.cloud.oauth2.security.auth.url-permission")
public class UrlPermissionProperties {

	/**
	 * 是否开启url级别权限
	 */
	private Boolean enable = false;

	/**
	 * 白名单，配置需要url权限认证的应用id（与黑名单互斥，只能配置其中一个），不配置默认所有应用都生效 配置enable为true时才生效
	 */
	private List<String> includeClientIds = new ArrayList<>();

	/**
	 * 黑名单，配置不需要url权限认证的应用id（与白名单互斥，只能配置其中一个） 配置enable为true时才生效
	 */
	private List<String> exclusiveClientIds = new ArrayList<>();

	/**
	 * 配置只进行登录认证，不进行url权限认证的api 所有已登录的人都能访问的api
	 */
	private String[] ignoreUrls = {};
}
