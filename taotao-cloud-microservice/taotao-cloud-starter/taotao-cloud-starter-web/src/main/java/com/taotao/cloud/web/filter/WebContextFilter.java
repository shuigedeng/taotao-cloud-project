package com.taotao.cloud.web.filter;

import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.web.properties.FilterProperties;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author: chejiangyi
 * @version: 2019-07-01 17:55 上下文添加过滤器
 **/
@AllArgsConstructor
public class WebContextFilter extends OncePerRequestFilter {

	private final FilterProperties filterProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !filterProperties.getWebContext();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		RequestUtil.bindContext(request, response);
		try {
			filterChain.doFilter(request, response);
		} finally {
			RequestUtil.clearContext();
		}
	}

	@Override
	public void destroy() {

	}
}
