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
package com.taotao.cloud.web.configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.exception.DefaultExceptionAdvice;
import com.taotao.cloud.web.filter.LbIsolationFilter;
import com.taotao.cloud.web.filter.TenantFilter;
import com.taotao.cloud.web.filter.TraceFilter;
import com.taotao.cloud.web.interceptor.MyInterceptor;
import com.taotao.cloud.web.listener.RequestMappingScanListener;
import com.taotao.cloud.web.mvc.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.web.mvc.converter.StringToEnumConverterFactory;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.support.LoginUserArgumentResolver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义mvc配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/9/29 14:30
 */
@AllArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final RedisRepository redisRepository;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(new IntegerToEnumConverterFactory());
		registry.addConverterFactory(new StringToEnumConverterFactory());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/**").
			addResourceLocations("classpath:/imgs/",
				"classpath:/mystatic/",
				"classpath:/static/",
				"classpath:/public/",
				"classpath:/META-INF/resources",
				"classpath:/resources");
	}

//	@Bean
//	@ConditionalOnMissingBean(RequestMappingScanListener.class)
//	public RequestMappingScanListener resourceAnnotationScan() {
//		RequestMappingScanListener scan = new RequestMappingScanListener(redisRepository);
//		LogUtil.info("资源扫描类.[{}]", scan);
//		return scan;
//	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return customizer -> {
			customizer.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(
				DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT)));
			customizer.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(
				DateTimeFormatter.ofPattern(CommonConstant.DATETIME_FORMAT)));
		};
	}

//	@Override
//	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//		Oauth2HttpMessageConverter oauth2HttpMessageConverter = new Oauth2HttpMessageConverter();
//		converters.add(0, oauth2HttpMessageConverter);
//	}
}
