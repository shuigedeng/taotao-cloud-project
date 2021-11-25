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

package com.taotao.cloud.xss.model;

import com.taotao.cloud.common.utils.ClassUtil;
import com.taotao.cloud.xss.properties.XssProperties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * xss 处理拦截器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class XssCleanInterceptor implements AsyncHandlerInterceptor {

	private final XssProperties xssProperties;

	public XssCleanInterceptor(XssProperties xssProperties) {
		this.xssProperties = xssProperties;
	}

	@Override
	public boolean preHandle(@NotNull HttpServletRequest request,
		@NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
		// 1. 非控制器请求直接跳出
		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		// 2. 没有开启
		if (!xssProperties.getEnabled()) {
			return true;
		}

		// 3. 处理 XssIgnore 注解
		XssCleanIgnore xssCleanIgnore = ClassUtil.getAnnotation(handlerMethod, XssCleanIgnore.class);
		if (xssCleanIgnore == null) {
			XssHolder.setEnable();
		}

		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
		Object handler, Exception ex) throws Exception {
		XssHolder.remove();
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
		HttpServletResponse response, Object handler) throws Exception {
		XssHolder.remove();
	}
}
