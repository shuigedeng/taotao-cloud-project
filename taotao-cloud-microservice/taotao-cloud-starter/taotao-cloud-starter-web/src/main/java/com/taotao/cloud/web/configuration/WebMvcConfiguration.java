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

import static com.taotao.cloud.common.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.taotao.cloud.common.json.JacksonModule;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.web.filter.VersionFilter;
import com.taotao.cloud.web.filter.TenantFilter;
import com.taotao.cloud.web.filter.TraceFilter;
import com.taotao.cloud.web.filter.WebContextFilter;
import com.taotao.cloud.web.interceptor.HeaderThreadLocalInterceptor;
import com.taotao.cloud.web.interceptor.PrometheusMetricsInterceptor;
import com.taotao.cloud.web.mvc.converter.IntegerToEnumConverterFactory;
import com.taotao.cloud.web.mvc.converter.StringToEnumConverterFactory;
import com.taotao.cloud.web.properties.FilterProperties;
import com.taotao.cloud.web.resolver.LoginUserArgumentResolver;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义mvc配置
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/29 14:30
 */
@Configuration
@AutoConfigureBefore({PrometheusConfiguration.class})
@AllArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final RedisRepository redisRepository;
	private final FilterProperties filterProperties;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserArgumentResolver());
	}

	@Bean
	public HeaderThreadLocalInterceptor headerThreadLocalInterceptor() {
		return new HeaderThreadLocalInterceptor();
	}

	@Bean
	public PrometheusMetricsInterceptor prometheusMetricsInterceptor() {
		return new PrometheusMetricsInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(headerThreadLocalInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(prometheusMetricsInterceptor()).addPathPatterns("/**");
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

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		WebMvcConfigurer.super.configureAsyncSupport(configurer);
	}

//	@Bean
//	@ConditionalOnBean(value = {RedisRepository.class})
//	public RequestMappingScanListener resourceAnnotationScan() {
//		RequestMappingScanListener scan = new RequestMappingScanListener(redisRepository);
//		LogUtil.info("资源扫描类.[{}]", scan);
//		return scan;
//	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return customizer -> {
			ObjectMapper objectMapper = customizer.createXmlMapper(true).build();
			objectMapper
				.setLocale(Locale.CHINA)
				//去掉默认的时间戳格式
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				// 时区
				.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
				//Date参数日期格式
				.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT, Locale.CHINA))

				//该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
				.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
				// 忽略不能转义的字符
				.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(),
					true)
				//在使用spring boot + jpa/hibernate，如果实体字段上加有FetchType.LAZY，并使用jackson序列化为json串时，会遇到SerializationFeature.FAIL_ON_EMPTY_BEANS异常
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
				//忽略未知字段
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				//单引号处理
				.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

			// 注册自定义模块
			objectMapper.registerModule(new JacksonModule())
				.findAndRegisterModules();

			customizer.configure(objectMapper);
		};
	}

	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
			.configure()
			// 快速失败模式
			.failFast(true)
			.buildValidatorFactory();
		return validatorFactory.getValidator();
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public FilterRegistrationBean<VersionFilter> lbIsolationFilterFilterRegistrationBean() {
		FilterRegistrationBean<VersionFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new VersionFilter(filterProperties));
		registrationBean.addUrlPatterns("/**");
		registrationBean.setName(VersionFilter.class.getName());
		registrationBean.setOrder(1);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TenantFilter> tenantFilterFilterRegistrationBean() {
		FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantFilter(filterProperties));
		registrationBean.addUrlPatterns("/**");
		registrationBean.setName(TenantFilter.class.getName());
		registrationBean.setOrder(2);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<TraceFilter> traceFilterFilterRegistrationBean() {
		FilterRegistrationBean<TraceFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TraceFilter(filterProperties));
		registrationBean.addUrlPatterns("/**");
		registrationBean.setName(TraceFilter.class.getName());
		registrationBean.setOrder(3);
		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<WebContextFilter> webContextFilterFilterRegistrationBean() {
		FilterRegistrationBean<WebContextFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new WebContextFilter(filterProperties));
		registrationBean.addUrlPatterns("/**");
		registrationBean.setName(WebContextFilter.class.getName());
		registrationBean.setOrder(4);
		return registrationBean;
	}
}
