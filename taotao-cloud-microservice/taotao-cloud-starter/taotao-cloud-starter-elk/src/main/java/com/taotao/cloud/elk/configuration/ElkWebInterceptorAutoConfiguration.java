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
package com.taotao.cloud.elk.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.elk.aspect.WebControllerAspect;
import com.taotao.cloud.elk.interceptor.ElkWebInterceptor;
import com.taotao.cloud.elk.properties.ElkWebAspectProperties;
import com.taotao.cloud.elk.properties.ElkWebProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ElkWebConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/3 10:43
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = ElkWebProperties.PREFIX, name = "enabled", havingValue = "true")
public class ElkWebInterceptorAutoConfiguration implements WebMvcConfigurer, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(ElkWebInterceptorAutoConfiguration.class, StarterName.ELK_STARTER);
	}

	@Bean
	public ElkWebInterceptor elkWebInterceptor() {
		return new ElkWebInterceptor();
	}

	@Bean
	@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
	@ConditionalOnProperty(prefix = ElkWebAspectProperties.PREFIX, name = "enabled", havingValue = "true")
	public WebControllerAspect webControllerAspect() {
		return new WebControllerAspect();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(elkWebInterceptor());
	}


}
