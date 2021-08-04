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
package com.taotao.cloud.sentinel.configuration;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import com.taotao.cloud.sentinel.properties.SentinelProperties;
import feign.Feign;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 限流、熔断统一处理类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/13 17:32
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@ConditionalOnProperty(prefix = SentinelProperties.PREFIX, name = "enabled", havingValue = "true")
public class SentinelAutoConfiguration {


	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return SentinelFeign.builder();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(HttpServletRequest.class)
	public BlockExceptionHandler blockExceptionHandler() {
		return (request, response, e) -> {
			LogUtil.error("HttpServletRequest sentinel 降级 资源名称{0}", e.getRule().getResource(), e);
			LogUtil.error("WebmvcHandler Sentinel调用失败: {0}", e);
			Result<String> result = Result.fail(e.getMessage());
			ResponseUtil.fail(response, result);
		};
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(ServerResponse.class)
	public BlockRequestHandler blockRequestHandler() {
		return (exchange, e) -> {
			LogUtil.error("ServerResponse sentinel 降级 资源名称{0}", e.getCause(), e);
			LogUtil.error("WebfluxHandler Sentinel调用失败: {0}", e);
			return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail(e.getMessage())));
		};
	}
}
