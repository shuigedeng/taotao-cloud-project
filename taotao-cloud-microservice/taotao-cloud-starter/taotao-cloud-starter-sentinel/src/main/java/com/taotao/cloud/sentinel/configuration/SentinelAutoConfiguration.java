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
package com.taotao.cloud.sentinel.configuration;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.common.utils.servlet.ResponseUtil;
import com.taotao.cloud.sentinel.model.SentinelFeign;
import com.taotao.cloud.sentinel.properties.SentinelProperties;
import feign.Feign;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * SentinelAutoConfiguration
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({SentinelProperties.class})
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
@ConditionalOnProperty(prefix = SentinelProperties.PREFIX, name = "enabled", havingValue = "true")
public class SentinelAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(SentinelAutoConfiguration.class, StarterName.SENTINEL_STARTER);
	}

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
			LogUtil.error("WebmvcHandler sentinel 降级 资源名称{}", e, e.getRule().getResource());
			String errMsg = e.getMessage();
			if (e instanceof FlowException) {
				errMsg = "被限流了";
			}
			if (e instanceof DegradeException) {
				errMsg = "服务降级了";
			}
			if (e instanceof SystemBlockException) {
				errMsg = "系统过载保护";
			}
			if (e instanceof AuthorityException) {
				errMsg = "限流权限控制异常";
			}
			ResponseUtil.fail(response, errMsg);
		};
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(ServerResponse.class)
	public BlockRequestHandler blockRequestHandler() {
		return (exchange, e) -> {
			LogUtil.error("ServerResponse sentinel 降级 资源名称{}",e, e.getCause());
			return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail(e.getMessage())));
		};
	}
}
