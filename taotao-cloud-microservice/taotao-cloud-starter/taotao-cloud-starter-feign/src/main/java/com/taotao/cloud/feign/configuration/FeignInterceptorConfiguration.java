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

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.constant.ContextConstant;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.common.IdGeneratorUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.feign.properties.FeignInterceptorProperties;
import feign.RequestInterceptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * feign拦截器，只包含http相关数据
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/5 13:33
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = FeignInterceptorProperties.PREFIX, name = "enabled", havingValue = "true")
public class FeignInterceptorConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(FeignInterceptorConfiguration.class, StarterName.FEIGN_STARTER);
	}

	protected List<String> requestHeaders = new ArrayList<>();

	public static final List<String> HEADER_NAME_LIST = Arrays.asList(
		ContextConstant.JWT_KEY_TENANT,
		ContextConstant.JWT_KEY_SUB_TENANT,
		ContextConstant.JWT_KEY_USER_ID,
		ContextConstant.JWT_KEY_ACCOUNT,
		ContextConstant.JWT_KEY_NAME,
		ContextConstant.GRAY_VERSION,
		ContextConstant.TRACE_ID_HEADER,
		"X-Real-IP",
		"x-forwarded-for"
	);

	@PostConstruct
	public void initialize() {
		requestHeaders.add(CommonConstant.TAOTAO_CLOUD_USER_ID_HEADER);
		requestHeaders.add(CommonConstant.TAOTAO_CLOUD_USER_NAME_HEADER);
		requestHeaders.add(CommonConstant.TAOTAO_CLOUD_USER_ROLE_HEADER);
		requestHeaders.add(CommonConstant.TAOTAO_CLOUD_VERSION);
	}

	/**
	 * 使用feign client访问别的微服务时，将上游传过来的access_token、username、roles等信息放入header传递给下一个服务
	 */
	@Bean
	public RequestInterceptor httpFeignInterceptor() {
		LogUtil.started(RequestInterceptor.class, StarterName.FEIGN_STARTER);

		return template -> {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			// job 类型的任务，可能没有Request
			if (requestAttributes != null) {
				ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
				//RequestContextHolder.setRequestAttributes(attributes, true);
				HttpServletRequest request = attributes.getRequest();
				Enumeration<String> headerNames = request.getHeaderNames();
				if (headerNames != null) {
					String headerName;
					String headerValue;

					while (headerNames.hasMoreElements()) {
						headerName = headerNames.nextElement();
						if (requestHeaders.contains(headerName)) {
							headerValue = request.getHeader(headerName);
							template.header(headerName, headerValue);
						}
					}
				}

				String token = extractHeaderToken(request);
				if (StrUtil.isEmpty(token)) {
					token = request.getParameter(CommonConstant.TAOTAO_CLOUD_ACCESS_TOKEN);
				}
				if (StrUtil.isNotEmpty(token)) {
					template.header(CommonConstant.TAOTAO_CLOUD_TOKEN_HEADER,
						CommonConstant.BEARER_TYPE + " " + token);
				}
			}

			//传递client
			//传递access_token，无网络隔离时需要传递
			String tenant = TenantContextHolder.getTenant();
			if (StrUtil.isNotEmpty(tenant)) {
				template.header(CommonConstant.TAOTAO_CLOUD_TENANT_HEADER, tenant);
			}

			//传递日志traceId
			String traceId = MDC.get(CommonConstant.TAOTAO_CLOUD_TRACE_ID);
			template.header(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER,
				StrUtil.isNotEmpty(traceId) ? traceId : IdGeneratorUtil.getIdStr());

			template.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		};
	}

	/**
	 * 解析head中的token
	 *
	 * @param request request
	 */
	private String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders(CommonConstant.TAOTAO_CLOUD_TOKEN_HEADER);
		while (headers.hasMoreElements()) {
			String value = headers.nextElement();
			if (value.startsWith(CommonConstant.BEARER_TYPE)) {
				String authHeaderValue = value.substring(CommonConstant.BEARER_TYPE.length())
					.trim();
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}
		return null;
	}
}
