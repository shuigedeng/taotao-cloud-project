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
package com.taotao.cloud.gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

/**
 * 动态路由配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/2 19:33
 */
@Configuration
@ConditionalOnProperty(prefix = "taotao.cloud.nacos.dynamic.route", name = "enabled", havingValue = "false")
public class DynamicRouteConfiguration {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Configuration
	@ConditionalOnProperty(prefix = "taotao.cloud.nacos.dynamic.route", name = "type", havingValue = "nacos", matchIfMissing = true)
	public class NacosDynamicRoute {
		// @Autowired
		// private NacosConfigProperties nacosConfigProperties;
		//
		// @Bean
		// public NacosRouteDefinitionRepository nacosRouteDefinitionRepository() {
		// 	return new NacosRouteDefinitionRepository(publisher, nacosConfigProperties);
		// }
	}
}
