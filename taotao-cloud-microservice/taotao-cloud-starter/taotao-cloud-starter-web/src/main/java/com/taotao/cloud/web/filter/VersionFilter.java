/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.VersionContextHolder;
import com.taotao.cloud.common.utils.servlet.TraceUtil;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * 负载均衡隔离规则过滤器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:16:36
 */
public class VersionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes());
			RequestContextHolder.setRequestAttributes(attributes, true);
			String version = request.getHeader(CommonConstant.TAOTAO_CLOUD_REQUEST_VERSION_HEADER);
			if (StrUtil.isNotEmpty(version)) {
				VersionContextHolder.setVersion(version);
				TraceUtil.mdcVersion(version);
			}

			filterChain.doFilter(request, response);
		} finally {
			VersionContextHolder.clear();
			MDC.clear();
		}
	}
}
