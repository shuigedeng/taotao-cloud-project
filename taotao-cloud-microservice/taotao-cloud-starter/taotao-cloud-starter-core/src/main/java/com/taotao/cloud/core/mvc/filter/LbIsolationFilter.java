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
package com.taotao.cloud.core.mvc.filter;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.LbIsolationContextHolder;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 负载均衡隔离规则过滤器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2019/9/15
 */
@ConditionalOnClass(Filter.class)
public class LbIsolationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes());
			RequestContextHolder.setRequestAttributes(attributes, true);
			String version = request.getHeader(CommonConstant.T_VERSION);
			if (StrUtil.isNotEmpty(version)) {
				LbIsolationContextHolder.setVersion(version);
			}

			filterChain.doFilter(request, response);
		} finally {
			LbIsolationContextHolder.clear();
		}
	}
}
