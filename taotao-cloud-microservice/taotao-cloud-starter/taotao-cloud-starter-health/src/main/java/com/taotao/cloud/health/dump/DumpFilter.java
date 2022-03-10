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
package com.taotao.cloud.health.dump;


import com.taotao.cloud.common.utils.context.ContextUtil;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DumpFilter
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:11:05
 */
public class DumpFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String contextPath = org.springframework.util.StringUtils.trimTrailingCharacter(
			request.getContextPath(), '/');

		String uri = request.getRequestURI();
		DumpProvider dumpProvider = ContextUtil.getBean(DumpProvider.class, true);

		if (uri.startsWith(contextPath + "/health/dump")) {
			if (Objects.nonNull(dumpProvider)) {
				if (uri.startsWith(contextPath + "/health/dump/zip/")) {
					dumpProvider.zip(request.getParameter("name"));
				} else if (uri.startsWith(contextPath + "/health/dump/download/")) {
					dumpProvider.download(request.getParameter("name"));
				} else if (uri.startsWith(contextPath + "/health/dump/do/")) {
					dumpProvider.dump();
				} else {
					dumpProvider.list();
				}
			}
		}
	}

	@Override
	public void destroy() {

	}
}
