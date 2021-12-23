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
package com.taotao.cloud.openapi.configuration;

import cn.hutool.core.collection.CollectionUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.openapi.properties.OpenApiProperties;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * SwaggerAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:10
 */
@Configuration
@EnableConfigurationProperties({OpenApiProperties.class})
@ConditionalOnProperty(prefix = OpenApiProperties.PREFIX, name = "enabled", havingValue = "true")
public class OpenapiAutoConfiguration implements InitializingBean{

	@Value("${spring.cloud.client.ip-address}")
	private String ip;

	@Autowired
	private OpenApiProperties properties;

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(OpenapiAutoConfiguration.class, StarterName.OPENAPI_STARTER);
	}

	@Bean
	public GroupedOpenApi groupedOpenApi() {
		return GroupedOpenApi
			.builder()
			.group(properties.getGroup())
			.pathsToMatch(properties.getPathsToMatch())
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
					.description("token")
					.type(SecurityScheme.Type.HTTP)
					.name("token")
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
			s1.setUrl("http://" + ip + ":9999/");
			s1.setDescription("本地地址");
			servers.add(s1);
			Server s2 = new Server();
			s2.setUrl("http://dev.taotaocloud.com/");
			s2.setDescription("测试环境地址");
			servers.add(s2);
			Server s3 = new Server();
			s3.setUrl("http://pre.taotaocloud.com/");
			s3.setDescription("预上线环境地址");
			servers.add(s3);
			Server s4 = new Server();
			s4.setUrl("http://pro.taotaocloud.com/");
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

//	@Bean
//	@ConditionalOnMissingBean
//	@ConditionalOnProperty(name = "taotao.cloud.springdoc.enabled", matchIfMissing = true)
//	public List<Docket> createRestApi(SpringdocProperties springdocProperties) {
//		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
//		List<Docket> docketList = new LinkedList<>();
//
//		// 没有分组
//		if (springdocProperties.getDocket().size() == 0) {
//			final Docket docket = createDocket(springdocProperties);
//			configurableBeanFactory.registerSingleton("defaultDocket", docket);
//			docketList.add(docket);
//			return docketList;
//		}
//
//		// 分组创建
//		for (String groupName : springdocProperties.getDocket().keySet()) {
//			SpringdocProperties.DocketInfo docketInfo = springdocProperties.getDocket()
//				.get(groupName);
//
//			ApiInfo apiInfo = new ApiInfoBuilder()
//				.title(docketInfo.getTitle().isEmpty() ? springdocProperties.getTitle()
//					: docketInfo.getTitle())
//				.description(
//					docketInfo.getDescription().isEmpty() ? springdocProperties.getDescription()
//						: docketInfo.getDescription())
//				.version(docketInfo.getVersion().isEmpty() ? springdocProperties.getVersion()
//					: docketInfo.getVersion())
//				.license(docketInfo.getLicense().isEmpty() ? springdocProperties.getLicense()
//					: docketInfo.getLicense())
//				.licenseUrl(
//					docketInfo.getLicenseUrl().isEmpty() ? springdocProperties.getLicenseUrl()
//						: docketInfo.getLicenseUrl())
//				.contact(
//					new Contact(
//						docketInfo.getContact().getName().isEmpty() ? springdocProperties
//							.getContact().getName() : docketInfo.getContact().getName(),
//						docketInfo.getContact().getUrl().isEmpty() ? springdocProperties
//							.getContact()
//							.getUrl() : docketInfo.getContact().getUrl(),
//						docketInfo.getContact().getEmail().isEmpty() ? springdocProperties
//							.getContact().getEmail() : docketInfo.getContact().getEmail()
//					)
//				)
//				.termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? springdocProperties
//					.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
//				.build();
//
//			// base-path处理
//			// 当没有配置任何path的时候，解析/**
//			if (docketInfo.getBasePath().isEmpty()) {
//				docketInfo.getBasePath().add("/**");
//			}
//
//			List<Predicate<String>> basePath = new ArrayList<>(docketInfo.getBasePath().size());
//			for (String path : docketInfo.getBasePath()) {
//				basePath.add(PathSelectors.ant(path));
//			}
//
//			// exclude-path处理
//			List<Predicate<String>> excludePath = new ArrayList<>(
//				docketInfo.getExcludePath().size());
//			for (String path : docketInfo.getExcludePath()) {
//				excludePath.add(PathSelectors.ant(path));
//			}
//
//			Docket docket = new Docket(DocumentationType.SWAGGER_2)
//				.host(springdocProperties.getHost())
//				.apiInfo(apiInfo)
//				.globalOperationParameters(assemblyGlobalOperationParameters(
//					springdocProperties.getGlobalOperationParameters(),
//					docketInfo.getGlobalOperationParameters()))
//				.groupName(groupName)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
//				.paths(
//					Predicates.and(
//						Predicates.not(Predicates.or(excludePath)),
//						Predicates.or(basePath)
//					)
//				)
//				.build()
//				.securitySchemes(securitySchemes())
//				.securityContexts(securityContexts());
//
//			configurableBeanFactory.registerSingleton(groupName, docket);
//			docketList.add(docket);
//		}
//		return docketList;
//	}
//
//	/**
//	 * 创建 Docket对象
//	 *
//	 * @param springdocProperties swagger配置
//	 * @return Docket
//	 */
//	private Docket createDocket(final SpringdocProperties springdocProperties) {
//		ApiInfo apiInfo = new ApiInfoBuilder()
//			.title(springdocProperties.getTitle())
//			.description(springdocProperties.getDescription())
//			.version(springdocProperties.getVersion())
//			.license(springdocProperties.getLicense())
//			.licenseUrl(springdocProperties.getLicenseUrl())
//			.contact(new Contact(springdocProperties.getContact().getName(),
//				springdocProperties.getContact().getUrl(),
//				springdocProperties.getContact().getEmail()))
//			.termsOfServiceUrl(springdocProperties.getTermsOfServiceUrl())
//			.build();
//
//		// base-path处理
//		// 当没有配置任何path的时候，解析/**
//		if (springdocProperties.getBasePath().isEmpty()) {
//			springdocProperties.getBasePath().add("/**");
//		}
//		List<Predicate<String>> basePath = new ArrayList<>();
//		for (String path : springdocProperties.getBasePath()) {
//			basePath.add(PathSelectors.ant(path));
//		}
//
//		// exclude-path处理
//		List<Predicate<String>> excludePath = new ArrayList<>();
//		for (String path : springdocProperties.getExcludePath()) {
//			excludePath.add(PathSelectors.ant(path));
//		}
//
//		return new Docket(DocumentationType.SWAGGER_2)
//			.host(springdocProperties.getHost())
//			.apiInfo(apiInfo)
//			.globalOperationParameters(buildGlobalOperationParametersFromSwaggerProperties(
//				springdocProperties.getGlobalOperationParameters()))
//			.select()
//			.apis(RequestHandlerSelectors.basePackage(springdocProperties.getBasePackage()))
//			.paths(
//				Predicates.and(
//					Predicates.not(Predicates.or(excludePath)),
//					Predicates.or(basePath)
//				)
//			)
//			.build()
//			.securitySchemes(securitySchemes())
//			.securityContexts(securityContexts());
//	}
//

//
//	private List<SecurityContext> securityContexts() {
//		List<SecurityContext> contexts = new ArrayList<>(1);
//		SecurityContext securityContext = SecurityContext.builder()
//			.securityReferences(defaultAuth())
//			//.forPaths(PathSelectors.regex("^(?!auth).*$"))
//			.build();
//		contexts.add(securityContext);
//		return contexts;
//	}
//
//	private List<SecurityReference> defaultAuth() {
//		AuthorizationScope authorizationScope = new AuthorizationScope("global",
//			"accessEverything");
//		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//		authorizationScopes[0] = authorizationScope;
//		List<SecurityReference> references = new ArrayList<>(1);
//		references.add(new SecurityReference(AUTH_KEY, authorizationScopes));
//		return references;
//	}
//
//	private List<ApiKey> securitySchemes() {
//		List<ApiKey> apiKeys = new ArrayList<>(1);
//		ApiKey apiKey = new ApiKey(AUTH_KEY, AUTH_KEY, "header");
//		apiKeys.add(apiKey);
//		return apiKeys;
//	}
//
//	private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
//		List<SpringdocProperties.GlobalOperationParameter> globalOperationParameters) {
//		List<Parameter> parameters = Lists.newArrayList();
//
//		if (Objects.isNull(globalOperationParameters)) {
//			return parameters;
//		}
//		for (SpringdocProperties.GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
//			parameters.add(new ParameterBuilder()
//				.name(globalOperationParameter.getName())
//				.description(globalOperationParameter.getDescription())
//				.modelRef(new ModelRef(globalOperationParameter.getModelRef()))
//				.parameterType(globalOperationParameter.getParameterType())
//				.required(Boolean.parseBoolean(globalOperationParameter.getRequired()))
//				.build());
//		}
//		return parameters;
//	}
//
//	/**
//	 * 按照name覆盖局部参数
//	 *
//	 * @param globalOperationParameters globalOperationParameters
//	 * @param docketOperationParameters docketOperationParameters
//	 * @return java.util.List<springfox.documentation.service.Parameter>
//	 * @author shuigedeng
//	 * @since 2020/4/30 10:10
//	 */
//	private List<Parameter> assemblyGlobalOperationParameters(
//		List<SpringdocProperties.GlobalOperationParameter> globalOperationParameters,
//		List<SpringdocProperties.GlobalOperationParameter> docketOperationParameters) {
//
//		if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
//			return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
//		}
//
//		Set<String> docketNames = docketOperationParameters.stream()
//			.map(SpringdocProperties.GlobalOperationParameter::getName)
//			.collect(Collectors.toSet());
//
//		List<SpringdocProperties.GlobalOperationParameter> resultOperationParameters = Lists
//			.newArrayList();
//
//		if (Objects.nonNull(globalOperationParameters)) {
//			for (SpringdocProperties.GlobalOperationParameter parameter : globalOperationParameters) {
//				if (!docketNames.contains(parameter.getName())) {
//					resultOperationParameters.add(parameter);
//				}
//			}
//		}
//
//		resultOperationParameters.addAll(docketOperationParameters);
//		return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
//	}
}
