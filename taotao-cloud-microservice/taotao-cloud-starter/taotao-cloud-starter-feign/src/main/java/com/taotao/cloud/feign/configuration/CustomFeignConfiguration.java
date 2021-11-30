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
package com.taotao.cloud.feign.configuration;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.feign.formatter.DateFormatRegister;
import com.taotao.cloud.feign.http.InfoFeignLoggerFactory;
import com.taotao.cloud.feign.http.RestTemplateHeaderInterceptor;
import com.taotao.cloud.feign.properties.FeignInterceptorProperties;
import com.taotao.cloud.feign.properties.FeignProperties;
import com.taotao.cloud.feign.properties.LbIsolationProperties;
import feign.Logger;
import feign.Response;
import feign.Retryer;
import feign.Util;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * FeignAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:31
 */
@Configuration
@EnableConfigurationProperties({
	LbIsolationProperties.class,
	FeignProperties.class,
	FeignInterceptorProperties.class})
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "enabled", havingValue = "true")
public class CustomFeignConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CustomFeignConfiguration.class, StarterNameConstant.FEIGN_STARTER);
	}

	@Bean
	public RequestOriginParser requestOriginParser() {
		return new HeaderRequestOriginParser();
	}

	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public DateFormatRegister dateFormatRegister() {
		return new DateFormatRegister();
	}

	@Bean
	public Retryer retryer() {
		return new Retryer.Default();
	}

	/**
	 * feign 支持MultipartFile上传文件
	 */
	@Bean
	public Encoder feignFormEncoder() {
		List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
		ObjectFactory<HttpMessageConverters> factory = () -> new HttpMessageConverters(converters);
		return new SpringFormEncoder(new SpringEncoder(factory));
	}

	@Bean
	public FeignClientErrorDecoder feignClientErrorDecoder() {
		return new FeignClientErrorDecoder();
	}

	public static class FeignClientErrorDecoder implements feign.codec.ErrorDecoder {

		@Override
		public Exception decode(String methodKey, Response response) {
			String errorContent;
			try {
				errorContent = Util.toString(response.body().asReader(Charset.defaultCharset()));
				LogUtil.error("feign调用异常{}", errorContent);
				return JsonUtil.toObject(errorContent, BaseException.class);
			} catch (IOException e) {
				e.printStackTrace();
				return new BaseException("500", e);
			}
		}
	}


	/**
	 * sentinel 请求头解析判断
	 *
	 * @author shuigedeng
	 * @version 1.0.0
	 * @since 2020/6/15 11:31
	 */
	public static class HeaderRequestOriginParser implements RequestOriginParser {

		/**
		 * 请求头获取allow
		 */
		private static final String ALLOW = "Allow";

		/**
		 * Parse the origin from given HTTP request.
		 *
		 * @param request HTTP request
		 * @return parsed origin
		 */
		@Override
		public String parseOrigin(HttpServletRequest request) {
			return request.getHeader(ALLOW);
		}

	}

	/**
	 * RestTemplate 相关的配置
	 *
	 */
	@ConditionalOnClass(okhttp3.OkHttpClient.class)
	public static class RestTemplateConfiguration {

		private static final Charset UTF_8 = StandardCharsets.UTF_8;
		private final ObjectMapper objectMapper;

		public RestTemplateConfiguration(ObjectMapper objectMapper) {
			this.objectMapper = objectMapper;
		}

		@Bean
		@ConditionalOnMissingBean(FeignLoggerFactory.class)
		public FeignLoggerFactory getInfoFeignLoggerFactory() {
			return new InfoFeignLoggerFactory();
		}

		@Bean
		@Profile({"dev", "test"})
		Logger.Level devFeignLoggerLevel() {
			return Logger.Level.FULL;
		}

		@Bean
		@Profile({"docker", "uat", "prod"})
		Logger.Level prodFeignLoggerLevel() {
			return Logger.Level.BASIC;
		}

		/**
		 * 配置OkHttpClient
		 *
		 * @param httpClientFactory    httpClient 工厂
		 * @param connectionPool       链接池配置
		 * @param httpClientProperties httpClient配置
		 * @return OkHttpClient
		 */
		@Bean
		@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
		public okhttp3.OkHttpClient okHttp3Client(
			OkHttpClientFactory httpClientFactory,
			okhttp3.ConnectionPool connectionPool,
			FeignHttpClientProperties httpClientProperties) {
			return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
				.followRedirects(httpClientProperties.isFollowRedirects())
				.writeTimeout(Duration.ofSeconds(30))
				.readTimeout(Duration.ofSeconds(30))
				.connectTimeout(Duration.ofMillis(httpClientProperties.getConnectionTimeout()))
				.connectionPool(connectionPool)
				.build();
		}

		/**
		 * okhttp3 链接池配置
		 *
		 * @param connectionPoolFactory 链接池配置
		 * @param hcp                   httpClient配置
		 * @return okhttp3.ConnectionPool
		 */
		@Bean
		@ConditionalOnMissingBean(okhttp3.ConnectionPool.class)
		public okhttp3.ConnectionPool okHttp3ConnectionPool(FeignHttpClientProperties hcp,
			OkHttpClientConnectionPoolFactory connectionPoolFactory) {
			return connectionPoolFactory.create(hcp.getMaxConnections(), hcp.getTimeToLive(), hcp.getTimeToLiveUnit());
		}


		/**
		 * 解决 RestTemplate 传递Request header
		 */
		@Bean
		public RestTemplateHeaderInterceptor requestHeaderInterceptor() {
			return new RestTemplateHeaderInterceptor();
		}

		/**
		 * 支持负载均衡的 LbRestTemplate, 传递请求头，一般用于内部 http 调用
		 *
		 * @param httpClient  OkHttpClient
		 * @param interceptor RestTemplateHeaderInterceptor
		 * @return LbRestTemplate
		 */
		@Bean("lbRestTemplate")
		@LoadBalanced
		@SentinelRestTemplate
		@ConditionalOnMissingBean(RestTemplate.class)
		public RestTemplate lbRestTemplate(okhttp3.OkHttpClient httpClient, RestTemplateHeaderInterceptor interceptor) {
			RestTemplate lbRestTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(httpClient));
			lbRestTemplate.setInterceptors(Collections.singletonList(interceptor));
			this.configMessageConverters(lbRestTemplate.getMessageConverters());
			return lbRestTemplate;
		}

		/**
		 * 普通的 RestTemplate，不透传请求头，一般只做外部 http 调用
		 *
		 * @param httpClient OkHttpClient
		 * @return RestTemplate
		 */
		@Bean
		@SentinelRestTemplate
		public RestTemplate restTemplate(okhttp3.OkHttpClient httpClient) {
			RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(httpClient));
			this.configMessageConverters(restTemplate.getMessageConverters());
			return restTemplate;
		}

		private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
			converters.removeIf(c -> c instanceof StringHttpMessageConverter || c instanceof MappingJackson2HttpMessageConverter);
			converters.add(new StringHttpMessageConverter(UTF_8));
			converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
		}
	}
}
