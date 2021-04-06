package com.taotao.cloud.web.filter;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.context.LbIsolationContextHolder;
import com.taotao.cloud.web.properties.FilterProperties;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LbIsolationFilter extends OncePerRequestFilter {

	@Autowired
	private FilterProperties filterProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !filterProperties.getLbIsolation();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws IOException, ServletException {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) Objects
				.requireNonNull(RequestContextHolder.getRequestAttributes());
			RequestContextHolder.setRequestAttributes(attributes, true);
			String version = request.getHeader(CommonConstant.TAOTAO_CLOUD_VERSION);
			if (StrUtil.isNotEmpty(version)) {
				LbIsolationContextHolder.setVersion(version);
			}

			filterChain.doFilter(request, response);
		} finally {
			LbIsolationContextHolder.clear();
		}
	}
}
