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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * SwaggerAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/30 10:10
 */
@Slf4j
public class OpenapiAutoConfiguration implements BeanFactoryAware, InitializingBean,
	EnvironmentAware {

	private static final String AUTH_KEY = "Authorization";

	private BeanFactory beanFactory;
	private Environment environment;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_SPRINGDOC_STARTER + "]"
			+ "springdoc模块已启动");
	}

	@Bean
	public GroupedOpenApi groupedOpenApi() {
		String applicationName = environment.getProperty("spring.application.name", "");

		return GroupedOpenApi
			.builder()
			.group(applicationName)
			.pathsToMatch("/public/**")
			.build();
	}

	@Bean
	public OpenApiCustomiser consumerTypeHeaderOpenAPICustomiser() {
		String applicationName = environment.getProperty("spring.application.name", "");

		return openApi -> {
			final Paths paths = openApi.getPaths();

			Paths newPaths = new Paths();
			paths.keySet().forEach(e -> newPaths.put("/" + applicationName + e, paths.get(e)));
			openApi.setPaths(newPaths);

			openApi.getPaths().values().stream()
				.flatMap(pathItem -> pathItem.readOperations().stream())
				.forEach(operation -> operation.addParametersItem(
					new HeaderParameter().$ref("#/components/parameters/myConsumerTypeHeader")));
		};
	}

	@Bean
	public OpenAPI openApi() {
		Components components = new Components();

		// 添加auth认证header
		components.addSecuritySchemes("token",
			new SecurityScheme()
				.description("token")
				.type(SecurityScheme.Type.APIKEY)
				.name("token")
				.in(In.HEADER)
				.scheme("basic")
		);

		components.addSecuritySchemes("bearer",
			new SecurityScheme()
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
		);

		// 添加全局header
		components.addParameters("meta",
			new Parameter()
				.description("元数据")
				.in(In.HEADER.toString())
				.schema(
					new StringSchema()
				)
				.name("meta")
		);

		components.addHeaders("version",
			new Header()
				.description("版本号")
				.schema(new StringSchema())
		);

		String taotaoCloudVersion = environment.getProperty("taotaoCloudVersion");
		String applicationName = environment.getProperty("spring.application.name", "");

		return new OpenAPI()
			.components(components)
			.openapi(applicationName)
			.info(
				new Info()
					.title(applicationName.toUpperCase() + " API")
					.description("TaoTao Cloud 电商及大数据平台")
					.version(taotaoCloudVersion)
					.license(
						new License()
							.name("Apache 2.0")
							.url(
								"https://github.com/shuigedeng/taotao-cloud-project/blob/master/LICENSE.txt")
					)
			)
			.externalDocs(
				new ExternalDocumentation()
					.description("TaoTao Cloud Wiki Documentation")
					.url("https://github.com/shuigedeng/taotao-cloud-project/wiki")
			);
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
