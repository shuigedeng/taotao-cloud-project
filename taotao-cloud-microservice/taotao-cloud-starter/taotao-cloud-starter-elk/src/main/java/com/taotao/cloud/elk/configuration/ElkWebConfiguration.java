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
package com.taotao.cloud.elk.configuration;

import com.taotao.cloud.elk.component.ElkWebInterceptor;
import com.taotao.cloud.elk.component.WebControllerAspect;
import com.taotao.cloud.elk.constant.ElkConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ElkWebConfiguration
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/3 10:43
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnProperty(
	prefix = ElkConstant.BASE_ELK_WEB_PREFIX,
	name = ElkConstant.ENABLED,
	havingValue = ElkConstant.TRUE
)
public class ElkWebConfiguration implements WebMvcConfigurer {

	@Bean
	public ElkWebInterceptor elkWebInterceptor() {
		return new ElkWebInterceptor();
	}

	@Bean
	@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
	@ConditionalOnProperty(
		prefix = ElkConstant.BASE_ELK_WEB_ASPECT_PREFIX,
		name = ElkConstant.ENABLED,
		havingValue = ElkConstant.TRUE
	)
	public WebControllerAspect webControllerAspect() {
		return new WebControllerAspect();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(elkWebInterceptor());
	}


}
