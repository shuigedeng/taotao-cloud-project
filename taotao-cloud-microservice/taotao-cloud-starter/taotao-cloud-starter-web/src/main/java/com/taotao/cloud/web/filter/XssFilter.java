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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.xss.XssRequestWrapper;
import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

/**
 * 跨站工具 过滤器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:43
 */
public class XssFilter implements Filter {

	/**
	 * 可放行的请求路径
	 */

	public static final String IGNORE_PATH = "ignorePath";
	/**
	 * 可放行的参数值
	 */
	public static final String IGNORE_PARAM_VALUE = "ignoreParamValue";

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	/**
	 * 可放行的请求路径列表
	 */
	private List<String> ignorePathList;
	/**
	 * 可放行的参数值列表
	 */
	private List<String> ignoreParamValueList;

	@Override
	public void init(FilterConfig fc) {
		this.ignorePathList = StrUtil.split(fc.getInitParameter(IGNORE_PATH), CharUtil.COMMA);
		this.ignoreParamValueList = StrUtil.split(fc.getInitParameter(IGNORE_PARAM_VALUE),
			CharUtil.COMMA);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		// 判断uri是否包含项目名称
		String uriPath = ((HttpServletRequest) request).getRequestURI();
		if (isIgnorePath(uriPath)) {
			LogUtil.debug("忽略过滤路径=[{}]", uriPath);
			chain.doFilter(request, response);
			return;
		}
		LogUtil.debug("过滤器包装请求路径=[{}]", uriPath);
		chain.doFilter(new XssRequestWrapper((HttpServletRequest) request, ignoreParamValueList),
			response);
	}

	private boolean isIgnorePath(String uriPath) {
		if (StrUtil.isBlank(uriPath)) {
			return true;
		}
		if (CollUtil.isEmpty(ignorePathList)) {
			return false;
		}
		return ignorePathList.stream()
			.anyMatch(url -> uriPath.startsWith(url) || ANT_PATH_MATCHER.match(url, uriPath));
	}
}
