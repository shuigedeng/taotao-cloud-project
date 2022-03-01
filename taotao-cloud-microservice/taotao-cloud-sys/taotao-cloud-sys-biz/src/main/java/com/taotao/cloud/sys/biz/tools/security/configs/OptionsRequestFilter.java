package com.taotao.cloud.sys.biz.tools.security.configs;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class OptionsRequestFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if("OPTIONS".equals(request.getMethod())) {
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
			response.setHeader("Access-Control-Allow-Headers", response.getHeader("Access-Control-Request-Headers"));
			response.setHeader("Access-Control-Allow-Credentials","true");
			return;
		}
		filterChain.doFilter(request, response);
	}

}
