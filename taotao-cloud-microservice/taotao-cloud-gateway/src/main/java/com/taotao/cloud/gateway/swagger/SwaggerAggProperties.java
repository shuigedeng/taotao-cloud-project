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
package com.taotao.cloud.gateway.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;
import java.util.Set;

/**
 * swagger聚合配置
 *
 * @author dengtao
 * @date 2020/4/29 22:14
 * @since v1.0
 */
@Data
@RefreshScope
@ConfigurationProperties("taotao.cloud.gateway.swagger-agg")
public class SwaggerAggProperties {

	private boolean enabled = false;

	/**
	 * Swagger返回JSON文档的接口路径（全局配置）
	 */
	private String apiDocsPath = "/api-docs";

	/**
	 * Swagger文档版本（全局配置）
	 */
	private String swaggerVersion = "2.0";

	/**
	 * 自动生成文档的路由名称，设置了generateRoutes之后，ignoreRoutes不生效
	 */
	private Set<String> generateRoutes = new HashSet<>();

	/**
	 * 不自动生成文档的路由名称，设置了generateRoutes之后，本配置不生效
	 */
	private Set<String> ignoreRoutes = new HashSet<>();

	/**
	 * 是否显示该路由
	 */
	public boolean isShow(String route) {
		int generateRoutesSize = generateRoutes.size();
		int ignoreRoutesSize = ignoreRoutes.size();

		if (generateRoutesSize > 0 && !generateRoutes.contains(route)) {
			return false;
		}

		return ignoreRoutesSize <= 0 || !ignoreRoutes.contains(route);
	}
}
