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
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.sentinel.properties.SentinelProperties;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * SentinelAutoConfiguration
 *
 * <pre class="code">
 *  // 注解方式进行埋点，注解方式受 AOP 代理的诸多限制
 * @SentinelResource("com.alibabacloud.mse.demo.AApplication.AController:a")
 * private String a(HttpServletRequest request) {
 *     StringBuilder headerSb = new StringBuilder();
 *     Enumeration<String> enumeration = request.getHeaderNames();
 *     while (enumeration.hasMoreElements()) {
 *         String headerName = enumeration.nextElement();
 *         Enumeration<String> val = request.getHeaders(headerName);
 *         while (val.hasMoreElements()) {
 *             String headerVal = val.nextElement();
 *             headerSb.append(headerName + ":" + headerVal + ",");
 *         }
 *     }
 *     return "A"+SERVICE_TAG+"[" + inetUtils.findFirstNonLoopbackAddress().getHostAddress() + "]" + " -> " +
 *             restTemplate.getForObject("http://sc-B/b", String.class);
 * }
 *
 * // SDK 方式增加流控降级能力，需要侵入业务代码
 * private String helloWorld(HttpServletRequest request) {
 *     Entry entry = null;
 *     try {
 *         entry = SphU.entry("HelloWorld");
 *
 *         StringBuilder headerSb = new StringBuilder();
 *         Enumeration<String> enumeration = request.getHeaderNames();
 *         while (enumeration.hasMoreElements()) {
 *             String headerName = enumeration.nextElement();
 *             Enumeration<String> val = request.getHeaders(headerName);
 *             while (val.hasMoreElements()) {
 *                 String headerVal = val.nextElement();
 *                 headerSb.append(headerName + ":" + headerVal + ",");
 *             }
 *         }
 *         return "A"+SERVICE_TAG+"[" + inetUtils.findFirstNonLoopbackAddress().getHostAddress() + "]" + " -> " +
 *                 restTemplate.getForObject("http://sc-B/b", String.class);
 *     } catch (BlockException ex) {
 *       System.err.println("blocked!");
 *     } finally {
 *         if (entry != null) {
 *             entry.exit();
 *         }
 *     }
 * }
 * </pre>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:54:47
 */
@AutoConfiguration(before = SentinelFeignAutoConfiguration.class)
@EnableConfigurationProperties({SentinelProperties.class})
@ConditionalOnProperty(prefix = SentinelProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class SentinelAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(SentinelAutoConfiguration.class, StarterName.SENTINEL_STARTER);
	}

	/**
	 * 授权规则拦截器
	 */
	@Bean
	public RequestOriginParser requestOriginParser() {
		return new HeaderRequestOriginParser();
	}

	/**
	 * feignSentinel 适配器 (源码已经配置) 此处可以配置也可不配置
	 */
	//@Bean
	//@Scope("prototype")
	//@ConditionalOnMissingBean
	//@ConditionalOnProperty(name = "feign.sentinel.enabled")
	//public Feign.Builder feignSentinelBuilder() {
	//	return HerodotusSentinelFeign.builder();
	//}

	@Bean
	@ConditionalOnClass(HttpServletRequest.class)
	public BlockExceptionHandler blockExceptionHandler() {
		return (request, response, e) -> {
			LogUtils.error("WebmvcHandler sentinel 降级 资源名称{}", e, e.getRule().getResource());
			String errMsg = e.getMessage();
			if (e instanceof FlowException) {
				errMsg = "被限流了";
			}
			if (e instanceof DegradeException) {
				errMsg = "服务降级了";
			}
			if (e instanceof ParamFlowException) {
				errMsg = "服务热点降级了";
			}
			if (e instanceof SystemBlockException) {
				errMsg = "系统过载保护";
			}
			if (e instanceof AuthorityException) {
				errMsg = "限流权限控制异常";
			}
			ResponseUtils.fail(response, errMsg);
		};
	}

	@Bean
	@ConditionalOnClass(ServerResponse.class)
	public BlockRequestHandler blockRequestHandler() {
		return (exchange, e) -> {
			LogUtils.error("ServerResponse sentinel 降级 资源名称{}", e, e.getCause());
			return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(Result.fail(e.getMessage())));
		};
	}


	/**
	 * sentinel 请求头解析判断
	 *
	 * @author shuigedeng
	 * @version 2022.03
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
			////基于请求参数,origin对应授权规则中的流控应用名称,也可通过getHeader传参
			//String origin = request.getParameter("origin");

			////TODO 此处做个通过IP做白名单的例子
			//return origin;

			return request.getHeader(ALLOW);
		}

	}
}
