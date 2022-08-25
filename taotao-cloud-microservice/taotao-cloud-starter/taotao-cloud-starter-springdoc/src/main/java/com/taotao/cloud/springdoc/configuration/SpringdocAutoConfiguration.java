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
package com.taotao.cloud.springdoc.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.springdoc.properties.SpringdocProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

/**
 * SwaggerAutoConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/30 10:10
 */
@AutoConfiguration
@EnableConfigurationProperties({SpringdocProperties.class})
@ConditionalOnProperty(prefix = SpringdocProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class SpringdocAutoConfiguration implements InitializingBean {

	@Value("${spring.cloud.client.ip-address}")
	private String ip;
	@Value("${server.port:8080}")
	private int port;
	@Value("${spring.mvc.servlet.path:/}")
	private String servletPath;

	@Autowired
	private SpringdocProperties properties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(SpringdocAutoConfiguration.class, StarterName.SPRINGDOC_STARTER);
	}

	@Bean
	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi
			.builder()
			.group(properties.getGroup())
			.pathsToMatch(properties.getPathsToMatch())
			.pathsToExclude(properties.getPathsToExclude())
			.packagesToScan(properties.getPackagesToScan())
			//.packagesToExclude(properties.getPackagesToExclude())
			.build();
	}

	@Bean
	public OpenApiCustomiser openApiCustomiser() {
		return openApi -> {
			final Paths paths = openApi.getPaths();

			Paths newPaths = new Paths();
			paths.keySet()
				.forEach(e -> newPaths.put("/api/v" + properties.getVersion() + e, paths.get(e)));
			openApi.setPaths(newPaths);

			openApi.getPaths().values().stream()
				.flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> {

				});
		};
	}

	@Bean
	public OpenAPI openApi() {
		Components components = new Components();

		Map<String, SecurityScheme> securitySchemes = properties.getSecuritySchemes();
		if (CollectionUtil.isEmpty(securitySchemes)) {
			// 添加auth认证header
			components.addSecuritySchemes("token",
				new SecurityScheme()
					.name("token")
					.description("token")
					.type(SecurityScheme.Type.HTTP)
					.in(In.HEADER)
					.scheme("basic")
			);
			components.addSecuritySchemes("bearer",
				new SecurityScheme()
					.name(HttpHeaders.AUTHORIZATION)
					.type(SecurityScheme.Type.HTTP)
					.in(In.HEADER)
					.scheme("bearer")
					.bearerFormat("JWT")
			);
		} else {
			securitySchemes.forEach(components::addSecuritySchemes);
		}

		Map<String, Header> headers = properties.getHeaders();
		if (CollectionUtil.isEmpty(headers)) {
			// 添加全局header
			components.addHeaders("taotao-cloud-request-version-header",
				new Header()
					.description("版本号")
					.schema(new StringSchema())
			);
			components.addHeaders("taotao-cloud-request-weight-header",
				new Header()
					.description("权重")
					.schema(new IntegerSchema())
			);
		} else {
			headers.forEach(components::addHeaders);
		}

		List<Server> servers = properties.getServers();
		if (CollectionUtil.isEmpty(servers)) {
			Server s1 = new Server();
			s1.setUrl("http://" + ip + ":" + port + "" + servletPath);
			s1.setDescription("本地地址");
			servers.add(s1);
			Server s2 = new Server();
			s2.setUrl("http://dev.taotaocloud.com/");
			s2.setDescription("测试环境地址");
			servers.add(s2);
			Server s3 = new Server();
			s3.setUrl("https://pre.taotaocloud.com/");
			s3.setDescription("预上线环境地址");
			servers.add(s3);
			Server s4 = new Server();
			s4.setUrl("https://pro.taotaocloud.com/");
			s4.setDescription("生产环境地址");
			servers.add(s4);
		}

		Contact contact = new Contact()
			.name("shuigedeng")
			.email("981376577@qq.com")
			.url("https://github.com/shuigedeng/taotao-cloud-project");
		License license = new License()
			.name("Apache 2.0")
			.url("https://github.com/shuigedeng/taotao-cloud-project/blob/master/LICENSE.txt");

		Info info = new Info()
			.title(properties.getTitle())
			.description(properties.getDescription())
			.version(properties.getVersion())
			.contact(Objects.isNull(properties.getContact()) ? contact : properties.getContact())
			.termsOfService(properties.getTermsOfService())
			.license(Objects.isNull(properties.getLicense()) ? license : properties.getLicense());

		ExternalDocumentation externalDocumentation = new ExternalDocumentation()
			.description(properties.getExternalDescription())
			.url(properties.getExternalUrl());

		return new OpenAPI()
			.components(components)
			.openapi(properties.getOpenapi())
			.info(info)
			.servers(servers)
			.externalDocs(externalDocumentation);
	}
}
