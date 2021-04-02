package com.taotao.cloud.web.filter;
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

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.properties.FilterProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 租户过滤器
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/6/15 11:30
 */
public class TenantFilter extends OncePerRequestFilter {
	@Autowired
	private FilterProperties filterProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !filterProperties.getTenant();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			//优先获取请求参数中的tenantId值
			String tenantId = request.getParameter(CommonConstant.TENANT_ID);
			if (StrUtil.isEmpty(tenantId)) {
				tenantId = request.getHeader(CommonConstant.TENANT_HEADER);
			}

			//保存租户id
			LogUtil.info("获取到的租户ID为:{}", tenantId);
			if (StringUtil.isNotBlank(tenantId)) {
				TenantContextHolder.setTenant(tenantId);
			} else {
				if (StringUtil.isBlank(TenantContextHolder.getTenant())) {
					TenantContextHolder.setTenant(CommonConstant.TENANT_ID_DEFAULT);
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			TenantContextHolder.clear();
		}
	}
}
