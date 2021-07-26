package com.taotao.cloud.web.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
import com.taotao.cloud.common.utils.TraceUtil;
import com.taotao.cloud.web.properties.FilterProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 租户过滤器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:30
 */
@AllArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

	private final FilterProperties filterProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !filterProperties.getTenant();
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
				TraceUtil.mdcTenantId(tenantId);
			} else {
				if (StringUtil.isBlank(TenantContextHolder.getTenant())) {
					TenantContextHolder.setTenant(CommonConstant.TAOTAO_CLOUD_TENANT_ID_DEFAULT);
					TraceUtil.mdcTenantId(CommonConstant.TAOTAO_CLOUD_TENANT_ID_DEFAULT);
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			TenantContextHolder.clear();
			MDC.clear();
		}
	}
}
