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
package com.taotao.cloud.web.servlet.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import com.taotao.cloud.common.utils.servlet.TraceUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 租户过滤器
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:15:01
 */
@WebFilter(filterName = "TenantFilter", urlPatterns = "/*", asyncSupported = true)
public class TenantFilter extends OncePerRequestFilter {
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return RequestUtils.excludeActuator(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			//优先获取请求参数中的tenantId值
			String tenantId = request.getParameter(CommonConstant.TAOTAO_CLOUD_TENANT_ID);
			if (StrUtil.isEmpty(tenantId)) {
				tenantId = request.getHeader(CommonConstant.TAOTAO_CLOUD_TENANT_HEADER);
			}

			//保存租户id
			if (StringUtil.isNotBlank(tenantId)) {
				TenantContextHolder.setTenant(tenantId);
				TraceUtils.mdcTenantId(tenantId);
			} else {
				if (StringUtil.isBlank(TenantContextHolder.getTenant())) {
					TenantContextHolder.setTenant(CommonConstant.TAOTAO_CLOUD_TENANT_ID_DEFAULT);
					TraceUtils.mdcTenantId(CommonConstant.TAOTAO_CLOUD_TENANT_ID_DEFAULT);
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			TenantContextHolder.clear();
			TraceUtils.mdcRemoveTenantId();
		}
	}
}
