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
package com.taotao.cloud.swagger.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.swagger.properties.Swagger2Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SwaggerAutoConfiguration
 *
 * @author dengtao
 * @since 2020/4/30 10:10
 * @version 1.0.0
 */
@Slf4j
@Import({Swagger2Configuration.class})
public class Swagger2AutoConfiguration implements BeanFactoryAware, InitializingBean {
    private static final String AUTH_KEY = "Authorization";

    private BeanFactory beanFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        LogUtil.info("[TAOTAO CLOUD][" + StarterNameConstant.TAOTAO_CLOUD_SWAGGER_STARTER + "]" + "swagger模块已启动");
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "taotao.cloud.swagger.enabled", matchIfMissing = true)
    public List<Docket> createRestApi(Swagger2Properties swagger2Properties) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();

        // 没有分组
        if (swagger2Properties.getDocket().size() == 0) {
            final Docket docket = createDocket(swagger2Properties);
            configurableBeanFactory.registerSingleton("defaultDocket", docket);
            docketList.add(docket);
            return docketList;
        }

        // 分组创建
        for (String groupName : swagger2Properties.getDocket().keySet()) {
            Swagger2Properties.DocketInfo docketInfo = swagger2Properties.getDocket().get(groupName);

            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(docketInfo.getTitle().isEmpty() ? swagger2Properties.getTitle() : docketInfo.getTitle())
                    .description(docketInfo.getDescription().isEmpty() ? swagger2Properties.getDescription() : docketInfo.getDescription())
                    .version(docketInfo.getVersion().isEmpty() ? swagger2Properties.getVersion() : docketInfo.getVersion())
                    .license(docketInfo.getLicense().isEmpty() ? swagger2Properties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swagger2Properties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(
                            new Contact(
                                    docketInfo.getContact().getName().isEmpty() ? swagger2Properties.getContact().getName() : docketInfo.getContact().getName(),
                                    docketInfo.getContact().getUrl().isEmpty() ? swagger2Properties.getContact().getUrl() : docketInfo.getContact().getUrl(),
                                    docketInfo.getContact().getEmail().isEmpty() ? swagger2Properties.getContact().getEmail() : docketInfo.getContact().getEmail()
                            )
                    )
                    .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? swagger2Properties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            if (docketInfo.getBasePath().isEmpty()) {
                docketInfo.getBasePath().add("/**");
            }

            List<Predicate<String>> basePath = new ArrayList<>(docketInfo.getBasePath().size());
            for (String path : docketInfo.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }

            // exclude-path处理
            List<Predicate<String>> excludePath = new ArrayList<>(docketInfo.getExcludePath().size());
            for (String path : docketInfo.getExcludePath()) {
                excludePath.add(PathSelectors.ant(path));
            }

            Docket docket = new Docket(DocumentationType.SWAGGER_2)
                    .host(swagger2Properties.getHost())
                    .apiInfo(apiInfo)
                    .globalOperationParameters(assemblyGlobalOperationParameters(swagger2Properties.getGlobalOperationParameters(),
                            docketInfo.getGlobalOperationParameters()))
                    .groupName(groupName)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                    .paths(
                            Predicates.and(
                                    Predicates.not(Predicates.or(excludePath)),
                                    Predicates.or(basePath)
                            )
                    )
                    .build()
                    .securitySchemes(securitySchemes())
                    .securityContexts(securityContexts());

            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    /**
     * 创建 Docket对象
     *
     * @param swagger2Properties swagger配置
     * @return Docket
     */
    private Docket createDocket(final Swagger2Properties swagger2Properties) {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swagger2Properties.getTitle())
                .description(swagger2Properties.getDescription())
                .version(swagger2Properties.getVersion())
                .license(swagger2Properties.getLicense())
                .licenseUrl(swagger2Properties.getLicenseUrl())
                .contact(new Contact(swagger2Properties.getContact().getName(),
                        swagger2Properties.getContact().getUrl(),
                        swagger2Properties.getContact().getEmail()))
                .termsOfServiceUrl(swagger2Properties.getTermsOfServiceUrl())
                .build();

        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (swagger2Properties.getBasePath().isEmpty()) {
            swagger2Properties.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList<>();
        for (String path : swagger2Properties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swagger2Properties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        return new Docket(DocumentationType.SWAGGER_2)
                .host(swagger2Properties.getHost())
                .apiInfo(apiInfo)
                .globalOperationParameters(buildGlobalOperationParametersFromSwaggerProperties(
                        swagger2Properties.getGlobalOperationParameters()))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Properties.getBasePackage()))
                .paths(
                        Predicates.and(
                                Predicates.not(Predicates.or(excludePath)),
                                Predicates.or(basePath)
                        )
                )
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> contexts = new ArrayList<>(1);
        SecurityContext securityContext = SecurityContext.builder()
                .securityReferences(defaultAuth())
                //.forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
        contexts.add(securityContext);
        return contexts;
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> references = new ArrayList<>(1);
        references.add(new SecurityReference(AUTH_KEY, authorizationScopes));
        return references;
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeys = new ArrayList<>(1);
        ApiKey apiKey = new ApiKey(AUTH_KEY, AUTH_KEY, "header");
        apiKeys.add(apiKey);
        return apiKeys;
    }

    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<Swagger2Properties.GlobalOperationParameter> globalOperationParameters) {
        List<Parameter> parameters = Lists.newArrayList();

        if (Objects.isNull(globalOperationParameters)) {
            return parameters;
        }
        for (Swagger2Properties.GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
            parameters.add(new ParameterBuilder()
                    .name(globalOperationParameter.getName())
                    .description(globalOperationParameter.getDescription())
                    .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                    .parameterType(globalOperationParameter.getParameterType())
                    .required(Boolean.parseBoolean(globalOperationParameter.getRequired()))
                    .build());
        }
        return parameters;
    }

    /**
     * 按照name覆盖局部参数
     *
     * @param globalOperationParameters globalOperationParameters
     * @param docketOperationParameters docketOperationParameters
     * @return java.util.List<springfox.documentation.service.Parameter>
     * @author dengtao
     * @since 2020/4/30 10:10
     */
    private List<Parameter> assemblyGlobalOperationParameters(
            List<Swagger2Properties.GlobalOperationParameter> globalOperationParameters,
            List<Swagger2Properties.GlobalOperationParameter> docketOperationParameters) {

        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
        }

        Set<String> docketNames = docketOperationParameters.stream()
                .map(Swagger2Properties.GlobalOperationParameter::getName)
                .collect(Collectors.toSet());

        List<Swagger2Properties.GlobalOperationParameter> resultOperationParameters = Lists.newArrayList();

        if (Objects.nonNull(globalOperationParameters)) {
            for (Swagger2Properties.GlobalOperationParameter parameter : globalOperationParameters) {
                if (!docketNames.contains(parameter.getName())) {
                    resultOperationParameters.add(parameter);
                }
            }
        }

        resultOperationParameters.addAll(docketOperationParameters);
        return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
    }
}
