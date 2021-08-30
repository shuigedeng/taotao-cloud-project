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
package com.taotao.cloud.web.filter;

import com.taotao.cloud.core.utils.RequestUtil;
import com.taotao.cloud.web.properties.FilterProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 上下文过滤器
 *
 * @version 1.0.0
 * @author shuigedeng
 * @since 2021/8/24 23:43
 */
public class WebContextFilter extends OncePerRequestFilter {

	private final FilterProperties filterProperties;

	public WebContextFilter(FilterProperties filterProperties) {
		this.filterProperties = filterProperties;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !filterProperties.getWebContext();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		RequestUtil.bindContext(request, response);
		try {
			filterChain.doFilter(request, response);
		} finally {
			RequestUtil.clearContext();
		}
	}

	@Override
	public void destroy() {

	}
}
