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
package com.taotao.cloud.loadbalancer.fegin;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.Retryer;
import feign.Util;
import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * FeignAutoConfiguration
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:31
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class FeignAutoConfiguration {

	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return SentinelFeign.builder();
	}

	@Bean
	@ConditionalOnMissingBean
	public BlockExceptionHandler blockExceptionHandler() {
		return new UrlBlockHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestOriginParser requestOriginParser() {
		return new HeaderRequestOriginParser();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	Retryer retryer() {
		return new Retryer.Default();
	}

	@Bean
	FeignClientErrorDecoder feignClientErrorDecoder() {
		return new FeignClientErrorDecoder();
	}

	public static class FeignClientErrorDecoder implements feign.codec.ErrorDecoder {

		@Override
		public Exception decode(String methodKey, Response response) {
			String errorContent;
			try {
				errorContent = Util.toString(response.body().asReader(Charset.defaultCharset()));
				LogUtil.error("feign调用异常{0}", errorContent);
				return JsonUtil.toObject(errorContent, BaseException.class);
			} catch (IOException e) {
				e.printStackTrace();
				return new BaseException("500", e);
			}
		}
	}
}
