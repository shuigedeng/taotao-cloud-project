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

import com.taotao.cloud.common.utils.LogUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * OpenApiConfig
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/04 13:48
 */
@Component
public class OpenApiConfiguration {

	@Value("${taotaoCloudVersion}")
	String version;

	@Bean
	@Lazy(false)
	public List<GroupedOpenApi> apis(
		SwaggerUiConfigParameters swaggerUiConfigParameters,
		RouteDefinitionLocator locator) {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();

		assert definitions != null;

		for (RouteDefinition definition : definitions) {
			LogUtil
				.info("spring cloud gateway route definition : {0}, uri: {1}", definition.getId(),
					definition.getUri().toString());
		}

		definitions.stream()
			.filter(routeDefinition -> routeDefinition.getId().startsWith("taotao-cloud"))
			.filter(routeDefinition -> !routeDefinition.getId()
				.startsWith("ReactiveCompositeDiscoveryClient_"))
			.forEach(routeDefinition -> {
				String name = routeDefinition.getId();
				swaggerUiConfigParameters.addGroup(name);
				GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
			});
		return groups;
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.tags(new ArrayList<>())
			.extensions(new HashMap<>())
			.openapi("TAOTAO CLOUD API")
			.paths(
				new Paths()
					.addPathItem("", new PathItem())
					.addPathItem("", new PathItem())
			)
			.servers(new ArrayList<>())
			.security(new ArrayList<>())
			.schemaRequirement("", new SecurityScheme()
				.scheme("")
				.description("")
				.extensions(new HashMap<>())
				.bearerFormat("")
				.name("")
			)
			.externalDocs(
				new ExternalDocumentation()
					.description("")
					.extensions(new HashMap<>())
					.url("")
			)
			.components(
				new Components()
					.schemas(new HashMap<>())
					.responses(new HashMap<>())
					.parameters(new HashMap<>())
					.examples(new HashMap<>())
					.requestBodies(new HashMap<>())
					.headers(new HashMap<>())
					.securitySchemes(new HashMap<>())
					.links(new HashMap<>())
					.callbacks(new HashMap<>())
					.extensions(new HashMap<>())
			)
			.info(
				new Info()
					.title("TAOTAO CLOUD API")
					.version(version)
					.description("TAOTAO CLOUD API")
					.extensions(new HashMap<>())
					.contact(new Contact()
						.name("")
						.url("")
						.email("")
						.extensions(new HashMap<>())
					)
					.termsOfService("")
					.license(new License()
						.name("Apache 2.0")
						.url("http://springdoc.org")
					)
			);
	}
}
