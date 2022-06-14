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
package com.taotao.cloud.feign.configuration;

import cn.hutool.core.lang.ParameterizedTypeImpl;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.endpoint.FeignClientEndpoint;
import com.taotao.cloud.feign.execption.FeignDecodeException;
import com.taotao.cloud.feign.formatter.DateFormatRegister;
import com.taotao.cloud.feign.http.InfoFeignLoggerFactory;
import com.taotao.cloud.feign.http.RestTemplateHeaderInterceptor;
import com.taotao.cloud.feign.properties.FeignInterceptorProperties;
import com.taotao.cloud.feign.properties.FeignProperties;
import com.taotao.cloud.feign.properties.LoadbalancerProperties;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Response;
import feign.Retryer;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
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
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * FeignAutoConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/15 11:31
 */
@AutoConfiguration(before = SentinelFeignAutoConfiguration.class)
@EnableConfigurationProperties({LoadbalancerProperties.class, FeignProperties.class, FeignInterceptorProperties.class})
@EnableAutoConfiguration(excludeName = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CustomFeignConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CustomFeignConfiguration.class, StarterName.FEIGN_STARTER);
	}


	/**
	 * Feign 客户端的日志记录，默认级别为NONE
	 * Logger.Level 的具体级别如下：
	 * NONE：不记录任何信息
	 * BASIC：仅记录请求方法、URL以及响应状态码和执行时间
	 * HEADERS：除了记录 BASIC级别的信息外，还会记录请求和响应的头信息
	 * FULL：记录所有请求与响应的明细，包括头信息、请求体、元数据
	 */
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
	// @Bean
	// public Encoder feignFormEncoder() {
	// 	List<HttpMessageConverter<?>> converters = new RestTemplate().getMessageConverters();
	// 	ObjectFactory<HttpMessageConverters> factory = () -> new HttpMessageConverters(converters);
	// 	return new SpringFormEncoder(new SpringEncoder(factory));
	// }

	/**
	 * Feign支持文件上传
	 */
	@Bean
	@Primary
	@Scope("prototype")
	public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}

	@Bean
	public Decoder feignDecoder(ObjectFactory<HttpMessageConverters> messageConverters,
		ObjectProvider<HttpMessageConverterCustomizer> customizers) {
		return new OptionalDecoder(
			new ResultDecode(new SpringDecoder(messageConverters, customizers)));
	}

	@Bean
	public FeignClientErrorDecoder feignClientErrorDecoder() {
		return new FeignClientErrorDecoder();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint
	public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
		return new FeignClientEndpoint(context);
	}

	/**
	 * 结果解码
	 *
	 * @author shuigedeng
	 * @version 2022.06
	 * @since 2022-06-09 10:29:26
	 */
	public static class ResultDecode extends ResponseEntityDecoder {

		public ResultDecode(Decoder decoder) {
			super(decoder);
		}

		@Override
		public Object decode(Response response, Type type) throws IOException, FeignException {
			if (type != null) {
				if (((ParameterizedType) type).getRawType() != Result.class) {
					type = new ParameterizedTypeImpl(new Type[]{type}, null, Result.class);
					Object object = super.decode(response, type);
					if (object instanceof Result<?> result) {
						if (result.code() != 200) {
							LogUtil.error("调用Feign接口出现异常，接口:{}, 异常: {}", response.request().url(),
								result.errorMsg());
							throw new FeignDecodeException(result.code(), result.errorMsg());
						}
						return result;
					}
				}
			}

			return super.decode(response, type);
		}
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
	 * RestTemplate 相关的配置
	 */
	@Configuration
	@ConditionalOnClass(okhttp3.OkHttpClient.class)
	public static class RestTemplateConfiguration implements InitializingBean {
		@Override
		public void afterPropertiesSet() throws Exception {
			LogUtil.started(RestTemplateConfiguration.class, StarterName.FEIGN_STARTER);
		}

		private static final Charset UTF_8 = StandardCharsets.UTF_8;
		private final ObjectMapper objectMapper = JsonUtil.MAPPER;

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
		@Profile({"docker", "pre", "prod"})
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
			return connectionPoolFactory.create(hcp.getMaxConnections(), hcp.getTimeToLive(),
				hcp.getTimeToLiveUnit());
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
		public RestTemplate lbRestTemplate(okhttp3.OkHttpClient httpClient,
			RestTemplateHeaderInterceptor interceptor) {
			RestTemplate lbRestTemplate = new RestTemplate(
				new OkHttp3ClientHttpRequestFactory(httpClient));
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
			RestTemplate restTemplate = new RestTemplate(
				new OkHttp3ClientHttpRequestFactory(httpClient));
			this.configMessageConverters(restTemplate.getMessageConverters());
			return restTemplate;
		}

		private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
			converters.removeIf(c -> c instanceof StringHttpMessageConverter
				|| c instanceof MappingJackson2HttpMessageConverter);
			converters.add(new StringHttpMessageConverter(UTF_8));
			converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
		}
	}
}
