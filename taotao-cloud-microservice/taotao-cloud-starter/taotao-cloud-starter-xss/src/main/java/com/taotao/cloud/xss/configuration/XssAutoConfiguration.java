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

package com.taotao.cloud.xss.configuration;

import cn.hutool.core.collection.CollUtil;
import com.taotao.cloud.xss.filter.XssFilter;
import com.taotao.cloud.xss.filter.XssHttpServletFilter;
import com.taotao.cloud.xss.interceptor.XssCleanInterceptor;
import com.taotao.cloud.xss.properties.XssProperties;
import com.taotao.cloud.xss.support.DefaultXssCleaner;
import com.taotao.cloud.xss.support.FormXssClean;
import com.taotao.cloud.xss.support.JacksonXssClean;
import com.taotao.cloud.xss.support.XssCleaner;
import com.taotao.cloud.xss.support.XssStringJsonDeserializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.taotao.cloud.xss.filter.XssFilter.IGNORE_PARAM_VALUE;
import static com.taotao.cloud.xss.filter.XssFilter.IGNORE_PATH;

/**
 * jackson xss 配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
@EnableConfigurationProperties({XssProperties.class})
@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true")
public class XssAutoConfiguration implements WebMvcConfigurer {

	private final XssProperties xssProperties;

	public XssAutoConfiguration(XssProperties xssProperties) {
		this.xssProperties = xssProperties;
	}

	@Bean
	public XssCleaner xssCleaner(XssProperties properties) {
		return new DefaultXssCleaner(properties);
	}

	@Bean
	public FormXssClean formXssClean(XssProperties properties, XssCleaner xssCleaner) {
		return new FormXssClean(properties, xssCleaner);
	}

	// 配置跨站攻击 反序列化处理器
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer xssJacksonCustomizer(XssProperties properties, XssCleaner xssCleaner) {
		JacksonXssClean xssClean = new JacksonXssClean(properties, xssCleaner);
		return builder -> {
			builder.deserializerByType(String.class, xssClean);
			builder.deserializerByType(String.class, new XssStringJsonDeserializer());
		};
	}

	@Bean
	public XssHttpServletFilter xssHttpServletFilter() {
		return new XssHttpServletFilter();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> patterns = xssProperties.getPathPatterns();
		if (patterns.isEmpty()) {
			patterns.add("/**");
		}

		XssCleanInterceptor interceptor = new XssCleanInterceptor(xssProperties);
		registry.addInterceptor(interceptor)
			.addPathPatterns(patterns)
			.excludePathPatterns(xssProperties.getPathExcludePatterns())
			.order(Ordered.LOWEST_PRECEDENCE);
	}

	/**
	 * 配置跨站攻击过滤器
	 */
	@Bean
	@ConditionalOnProperty(prefix = XssProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
	public FilterRegistrationBean<XssFilter> filterRegistrationBean() {
		FilterRegistrationBean<XssFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new XssFilter());
		filterRegistration.setEnabled(xssProperties.getEnabled());
		filterRegistration.addUrlPatterns(xssProperties.getPatterns().toArray(new String[0]));
		filterRegistration.setOrder(xssProperties.getOrder());

		Map<String, String> initParameters = new HashMap<>(4);
		initParameters.put(IGNORE_PATH, CollUtil.join(xssProperties.getIgnorePaths(), ","));
		initParameters.put(IGNORE_PARAM_VALUE, CollUtil.join(xssProperties.getIgnoreParamValues(), ","));
		filterRegistration.setInitParameters(initParameters);
		return filterRegistration;
	}

}
