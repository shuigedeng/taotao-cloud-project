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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SwaggerProvider
 *
 * @author dengtao
 * @since 2020/4/29 22:14
 * @version 1.0.0
 */
@Primary
@ConditionalOnProperty(prefix = "taotao.cloud.swagger-agg", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerProvider implements SwaggerResourcesProvider {
	private final RouteLocator routeLocator;
	private final GatewayProperties gatewayProperties;
	private final SwaggerAggProperties swaggerAggProperties;

	public SwaggerProvider(RouteLocator routeLocator,
						   GatewayProperties gatewayProperties,
						   SwaggerAggProperties swaggerAggProperties) {
		this.routeLocator = routeLocator;
		this.gatewayProperties = gatewayProperties;
		this.swaggerAggProperties = swaggerAggProperties;
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		Set<String> routes = new HashSet<>();
		//取出Spring Cloud Gateway中的route
		routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
		//结合application.yml中的路由配置，只获取有效的route节点
		gatewayProperties.getRoutes().stream().filter(
			routeDefinition -> (
				routes.contains(routeDefinition.getId()) && swaggerAggProperties.isShow(routeDefinition.getId())
			)
		).forEach(routeDefinition -> routeDefinition.getPredicates().stream()
			.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
			.forEach(predicateDefinition -> resources.add(
				swaggerResource(
					routeDefinition.getId(),
					predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**", swaggerAggProperties.getApiDocsPath())
				)
				)
			)
		);
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(swaggerAggProperties.getSwaggerVersion());
		return swaggerResource;
	}
}
