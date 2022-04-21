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

package com.taotao.cloud.processor.common;


import com.taotao.cloud.processor.annotation.AutoConfigDataLoader;
import com.taotao.cloud.processor.annotation.AutoConfigDataLocationResolver;
import com.taotao.cloud.processor.annotation.AutoConfigImportFilter;
import com.taotao.cloud.processor.annotation.AutoContextInitializer;
import com.taotao.cloud.processor.annotation.AutoDatabaseInitializerDetector;
import com.taotao.cloud.processor.annotation.AutoDependsOnDatabaseInitializationDetector;
import com.taotao.cloud.processor.annotation.AutoEnableCircuitBreaker;
import com.taotao.cloud.processor.annotation.AutoEnvPostProcessor;
import com.taotao.cloud.processor.annotation.AutoFailureAnalyzer;
import com.taotao.cloud.processor.annotation.AutoListener;
import com.taotao.cloud.processor.annotation.AutoLoggingSystemFactory;
import com.taotao.cloud.processor.annotation.AutoRunListener;
import com.taotao.cloud.processor.annotation.AutoTemplateProvider;

/**
 * 注解类型
 */
public enum BootAutoType {

	/**
	 * Component，组合注解，添加到 spring.factories
	 */
	COMPONENT("org.springframework.stereotype.Component",
		"org.springframework.boot.autoconfigure.EnableAutoConfiguration"),
	/**
	 * ApplicationContextInitializer 添加到 spring.factories
	 */
	CONTEXT_INITIALIZER(AutoContextInitializer.class.getName(),
		"org.springframework.context.ApplicationContextInitializer"),
	/**
	 * ApplicationListener 添加到 spring.factories
	 */
	LISTENER(AutoListener.class.getName(), "org.springframework.context.ApplicationListener"),
	/**
	 * SpringApplicationRunListener 添加到 spring.factories
	 */
	RUN_LISTENER(AutoRunListener.class.getName(),
		"org.springframework.boot.SpringApplicationRunListener"),
	/**
	 * EnvironmentPostProcessor 添加到 spring.factories
	 */
	ENV_POST_PROCESSOR(AutoEnvPostProcessor.class.getName(),
		"org.springframework.boot.env.EnvironmentPostProcessor"),
	/**
	 * FailureAnalyzer 添加到 spring.factories
	 */
	FAILURE_ANALYZER(AutoFailureAnalyzer.class.getName(),
		"org.springframework.boot.diagnostics.FailureAnalyzer"),
	/**
	 * AutoConfigurationImportFilter spring.factories
	 */
	AUTO_CONFIGURATION_IMPORT_FILTER(AutoConfigImportFilter.class.getName(),
		"org.springframework.boot.autoconfigure.AutoConfigurationImportFilter"),
	/**
	 * TemplateAvailabilityProvider 添加到 spring.factories
	 */
	TEMPLATE_AVAILABILITY_PROVIDER(AutoTemplateProvider.class.getName(),
		"org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider"),
	/**
	 * auto EnableCircuitBreaker
	 */
	AUTO_ENABLE_CIRCUIT_BREAKER(AutoEnableCircuitBreaker.class.getName(),
		"org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker"),
	/**
	 * auto ConfigDataLocationResolver
	 */
	AUTO_CONFIG_DATA_LOCATION_RESOLVER(AutoConfigDataLocationResolver.class.getName(),
		"org.springframework.boot.context.config.ConfigDataLocationResolver"),
	/**
	 * auto ConfigDataLoader
	 */
	AUTO_CONFIG_DATA_LOADER(AutoConfigDataLoader.class.getName(),
		"org.springframework.boot.context.config.ConfigDataLoader"),
	/**
	 * auto DatabaseInitializerDetector
	 */
	AUTO_DATABASE_INITIALIZER_DETECTOR(AutoDatabaseInitializerDetector.class.getName(),
		"org.springframework.boot.sql.init.dependency.DatabaseInitializerDetector"),
	/**
	 * auto DependsOnDatabaseInitializationDetector
	 */
	AUTO_DEPENDS_ON_DATABASE_INITIALIZATION_DETECTOR(
		AutoDependsOnDatabaseInitializationDetector.class.getName(),
		"org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitializationDetector"),
	/**
	 * auto LoggingSystemFactory
	 */
	AUTO_LOGGING_SYSTEM(AutoLoggingSystemFactory.class.getName(),
		"org.springframework.boot.logging.LoggingSystemFactory"),
	;

	private final String annotation;
	private final String configureKey;

	BootAutoType(String annotation, String configureKey) {
		this.annotation = annotation;
		this.configureKey = configureKey;
	}

	public String getAnnotation() {
		return annotation;
	}

	public String getConfigureKey() {
		return configureKey;
	}
}
