package com.taotao.cloud.web.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.TenantContextHolder;
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
			String tenantId = request.getParameter(CommonConstant.TAOTAO_CLOUD_TENANT_ID);
			if (StrUtil.isEmpty(tenantId)) {
				tenantId = request.getHeader(CommonConstant.TAOTAO_CLOUD_TENANT_HEADER);
			}

			//保存租户id
//			LogUtil.info("获取到的租户ID为:{}", tenantId);
			if (StringUtil.isNotBlank(tenantId)) {
				TenantContextHolder.setTenant(tenantId);
			} else {
				if (StringUtil.isBlank(TenantContextHolder.getTenant())) {
					TenantContextHolder.setTenant(CommonConstant.TAOTAO_CLOUD_TENANT_ID_DEFAULT);
				}
			}

			filterChain.doFilter(request, response);
		} finally {
			TenantContextHolder.clear();
		}
	}
}
