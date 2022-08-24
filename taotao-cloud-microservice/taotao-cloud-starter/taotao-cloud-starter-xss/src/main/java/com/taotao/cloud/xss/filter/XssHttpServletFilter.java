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


package com.taotao.cloud.xss.filter;

import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *  Xss 过滤器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 09:00:36
 */
public class XssHttpServletFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);
		LogUtil.info("XssHttpServletFilter wrapper request for [{}].", request.getRequestURI());
		filterChain.doFilter(xssRequest, servletResponse);
	}
}
