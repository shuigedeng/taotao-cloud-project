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

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

import cn.hutool.core.lang.ParameterizedTypeImpl;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.FeignErrorException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.annotation.Inner;
import com.taotao.cloud.feign.endpoint.FeignClientEndpoint;
import com.taotao.cloud.feign.formatter.DateFormatRegister;
import com.taotao.cloud.feign.model.FeignExceptionResult;
import com.taotao.cloud.feign.properties.FeignInterceptorProperties;
import com.taotao.cloud.feign.properties.FeignProperties;
import com.taotao.cloud.feign.properties.LoadbalancerProperties;
import feign.Contract;
import feign.FeignException;
import feign.Logger;
import feign.MethodMetadata;
import feign.Response;
import feign.Retryer;
import feign.Util;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.optionals.OptionalDecoder;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;

/**
 * FeignAutoConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/6/15 11:31
 */
@AutoConfiguration(before = SentinelFeignAutoConfiguration.class)
@EnableConfigurationProperties({LoadbalancerProperties.class, FeignProperties.class, FeignInterceptorProperties.class})
@ConditionalOnProperty(prefix = FeignProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class CustomFeignAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(CustomFeignAutoConfiguration.class, StarterName.FEIGN_STARTER);
	}

	@Bean
	@ConditionalOnMissingBean
	public Contract contract() {
		return new FeignInnerContract();
	}

	/**
	 * Feign 客户端的日志记录，默认级别为NONE Logger.Level 的具体级别如下： NONE：不记录任何信息 BASIC：仅记录请求方法、URL以及响应状态码和执行时间
	 * HEADERS：除了记录 BASIC级别的信息外，还会记录请求和响应的头信息 FULL：记录所有请求与响应的明细，包括头信息、请求体、元数据
	 */
	@Bean
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	//@Bean
	//@Profile({"dev", "test"})
	//public Logger.Level devFeignLoggerLevel() {
	//	return Logger.Level.FULL;
	//}
	//
	//@Bean
	//@Profile({"docker", "pre", "prod"})
	//public Logger.Level prodFeignLoggerLevel() {
	//	return Logger.Level.BASIC;
	//}

	@Bean
	public DateFormatRegister dateFormatRegister() {
		return new DateFormatRegister();
	}

	@Bean
	public Retryer retryer() {
		return new Retryer.Default();
	}

	/**
	 * Feign支持文件上传
	 */
	@Bean
	@Primary
	@Scope("prototype")
	public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
		return new SpringFormEncoder(new SpringEncoder(messageConverters));
	}

	//@Bean
	//public Encoder feignEncoder() {
	//	return new SpringEncoder(feignHttpMessageConverter());
	//}

	//@Bean("starterFeignDecoder")
	//public Decoder feignDecoder(ObjectProvider<HttpMessageConverterCustomizer> customizers) {
	//	return new OptionalDecoder(
	//		new ResultDecode(new SpringDecoder(feignHttpMessageConverter(), customizers)));
	//}

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
				// 适配 feign 接口返回 Result 结果类型
				if (type instanceof ParameterizedType parameterizedType) {
					if (parameterizedType.getRawType() != Result.class) {
						type = new ParameterizedTypeImpl(new Type[]{type}, null, Result.class);
						Object object = super.decode(response, type);
						if (object instanceof Result<?> result) {
							if (result.code() != 200) {
								LogUtil.error("调用Feign接口出现异常，接口:{}, 异常: {}",
									response.request().url(),
									result.errorMsg());
								throw new FeignErrorException(result.code(), result.errorMsg());
							}
							return result;
						}
					}
				}
			}

			return super.decode(response, type);
		}
	}

	public static class FeignClientErrorDecoder implements feign.codec.ErrorDecoder {

		@Override
		public Exception decode(String methodKey, Response response) {
			String errorContent = "内部服务调用错误";

			try {
				String res = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
				LogUtil.error("feign调用异常{}", res);
				FeignExceptionResult feignExceptionResult = JsonUtil.toObject(res, FeignExceptionResult.class);
				errorContent = feignExceptionResult.getMsg();
			} catch (Exception e) {
				LogUtil.error(e);
			}
			return new FeignErrorException(500, errorContent);
		}
	}

	/**
	 * <p>Description: 自定义 Inner 处理器 </p>
	 *
	 * @author : gengwei.zheng
	 * @date : 2022/5/31 11:28
	 */
	public static class FeignInnerContract extends SpringMvcContract {

		@Override
		protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation,
			Method method) {
			if (Inner.class.isInstance(methodAnnotation)) {
				Inner inner = findMergedAnnotation(method, Inner.class);
				if (ObjectUtils.isNotEmpty(inner)) {
					LogUtil.debug(
						"[Herodotus] |- Found inner annotation on Feign interface, add header!");
					data.template().header(CommonConstant.TAOTAO_CLOUD_FROM_INNER, "true");
				}
			}

			super.processAnnotationOnMethod(data, methodAnnotation, method);
		}
	}

	/**
	 * 设置解码器为fastjson
	 */
	private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
		final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(
			this.getFastJsonConverter());
		return () -> httpMessageConverters;
	}

	private FastJsonHttpMessageConverter getFastJsonConverter() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

		List<MediaType> supportedMediaTypes = new ArrayList<>();
		MediaType mediaTypeJson = MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE);
		supportedMediaTypes.add(mediaTypeJson);
		converter.setSupportedMediaTypes(supportedMediaTypes);
		FastJsonConfig config = new FastJsonConfig();
		config.getSerializeConfig().put(JSON.class, new SwaggerJsonSerializer());
		config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
		converter.setFastJsonConfig(config);

		return converter;
	}

	///**
	// * RestTemplate 相关的配置
	// */
	//@Configuration
	//@ConditionalOnClass(okhttp3.OkHttpClient.class)
	//public static class RestTemplateConfiguration implements InitializingBean {
	//	@Override
	//	public void afterPropertiesSet() throws Exception {
	//		LogUtil.started(RestTemplateConfiguration.class, StarterName.FEIGN_STARTER);
	//	}
	//
	//	private static final Charset UTF_8 = StandardCharsets.UTF_8;
	//	private final ObjectMapper objectMapper = JsonUtil.MAPPER;
	//
	//	@Bean
	//	@ConditionalOnMissingBean(FeignLoggerFactory.class)
	//	public FeignLoggerFactory getInfoFeignLoggerFactory() {
	//		return new InfoFeignLoggerFactory();
	//	}
	//
	//
	//	/**
	//	 * 配置OkHttpClient
	//	 *
	//	 * @param httpClientFactory    httpClient 工厂
	//	 * @param connectionPool       链接池配置
	//	 * @param httpClientProperties httpClient配置
	//	 * @return OkHttpClient
	//	 */
	//	@Bean
	//	@ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
	//	public okhttp3.OkHttpClient okHttp3Client(
	//		OkHttpClientFactory httpClientFactory,
	//		okhttp3.ConnectionPool connectionPool,
	//		FeignHttpClientProperties httpClientProperties) {
	//		return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
	//			.followRedirects(httpClientProperties.isFollowRedirects())
	//			.writeTimeout(Duration.ofSeconds(30))
	//			.readTimeout(Duration.ofSeconds(30))
	//			.connectTimeout(Duration.ofMillis(httpClientProperties.getConnectionTimeout()))
	//			.connectionPool(connectionPool)
	//			.build();
	//	}
	//
	//	/**
	//	 * okhttp3 链接池配置
	//	 *
	//	 * @param connectionPoolFactory 链接池配置
	//	 * @param hcp                   httpClient配置
	//	 * @return okhttp3.ConnectionPool
	//	 */
	//	@Bean
	//	@ConditionalOnMissingBean(okhttp3.ConnectionPool.class)
	//	public okhttp3.ConnectionPool okHttp3ConnectionPool(FeignHttpClientProperties hcp,
	//		OkHttpClientConnectionPoolFactory connectionPoolFactory) {
	//		return connectionPoolFactory.create(hcp.getMaxConnections(), hcp.getTimeToLive(),
	//			hcp.getTimeToLiveUnit());
	//	}
	//
	//	/**
	//	 * 解决 RestTemplate 传递Request header
	//	 */
	//	@Bean
	//	public RestTemplateHeaderInterceptor requestHeaderInterceptor() {
	//		return new RestTemplateHeaderInterceptor();
	//	}
	//
	//	/**
	//	 * 支持负载均衡的 LbRestTemplate, 传递请求头，一般用于内部 http 调用
	//	 *
	//	 * @param httpClient  OkHttpClient
	//	 * @param interceptor RestTemplateHeaderInterceptor
	//	 * @return LbRestTemplate
	//	 */
	//	@Bean("lbRestTemplate")
	//	@LoadBalanced
	//	@SentinelRestTemplate
	//	@ConditionalOnMissingBean(RestTemplate.class)
	//	public RestTemplate lbRestTemplate(okhttp3.OkHttpClient httpClient,
	//		RestTemplateHeaderInterceptor interceptor) {
	//		RestTemplate lbRestTemplate = new RestTemplate(
	//			new OkHttp3ClientHttpRequestFactory(httpClient));
	//		lbRestTemplate.setInterceptors(Collections.singletonList(interceptor));
	//		this.configMessageConverters(lbRestTemplate.getMessageConverters());
	//		return lbRestTemplate;
	//	}
	//
	//	/**
	//	 * 普通的 RestTemplate，不透传请求头，一般只做外部 http 调用
	//	 *
	//	 * @param httpClient OkHttpClient
	//	 * @return RestTemplate
	//	 */
	//	@Bean
	//	@SentinelRestTemplate
	//	public RestTemplate restTemplate(okhttp3.OkHttpClient httpClient) {
	//		RestTemplate restTemplate = new RestTemplate(
	//			new OkHttp3ClientHttpRequestFactory(httpClient));
	//		this.configMessageConverters(restTemplate.getMessageConverters());
	//		return restTemplate;
	//	}
	//
	//	private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
	//		converters.removeIf(c -> c instanceof StringHttpMessageConverter
	//			|| c instanceof MappingJackson2HttpMessageConverter);
	//		converters.add(new StringHttpMessageConverter(UTF_8));
	//		converters.add(new MappingJackson2HttpMessageConverter(this.objectMapper));
	//	}
	//}
}
