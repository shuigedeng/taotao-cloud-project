package com.taotao.cloud.health.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

/**
 * @author: chejiangyi
 * @version: 2019-09-24 20:59
 **/
public class PingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String conetextPath = StringUtils.trimTrailingCharacter(
			request.getContextPath(), '/');

		String uri = request.getRequestURI();
		if (uri.startsWith(conetextPath + "/taotao/cloud/health/ping/")) {
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("ok");
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	@Override
	public void destroy() {

	}
}
