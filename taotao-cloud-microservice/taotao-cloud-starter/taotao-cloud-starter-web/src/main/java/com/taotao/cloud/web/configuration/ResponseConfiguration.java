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

import com.taotao.cloud.common.constant.StarterNameConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.annotation.IgnoreResponseBodyAdvice;
import javax.servlet.Servlet;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一返回值 包装器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:28:49
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"}, annotations = {
//	RestController.class, Controller.class})
@RestControllerAdvice(basePackages = {"com.taotao.cloud.*.biz.controller"})
public class ResponseConfiguration implements ResponseBodyAdvice<Object>, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(ResponseConfiguration.class, StarterNameConstant.WEB_STARTER);
	}

	@Override
	public boolean supports(MethodParameter methodParameter, @NotNull Class aClass) {
		// 类上如果被 IgnoreResponseBodyAdvice 标识就不拦截
		if (methodParameter.getDeclaringClass()
			.isAnnotationPresent(IgnoreResponseBodyAdvice.class)) {
			return false;
		}

		// 方法上被标注也不拦截
		return !methodParameter.getMethod().isAnnotationPresent(IgnoreResponseBodyAdvice.class);
	}

	@Override
	public Object beforeBodyWrite(Object o,
		@NotNull MethodParameter methodParameter,
		@NotNull MediaType mediaType,
		@NotNull Class aClass,
		@NotNull ServerHttpRequest serverHttpRequest,
		@NotNull ServerHttpResponse serverHttpResponse) {
		if (o == null) {
			return null;
		}
		if (o instanceof Result) {
			return o;
		}

		return Result.success(o);
	}
}
