package com.taotao.cloud.health.filter;


import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.health.dump.DumpProvider;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: chejiangyi
 * @version: 2019-09-07 13:31
 **/
public class DumpFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String conetextPath = org.springframework.util.StringUtils.trimTrailingCharacter(
			request.getContextPath(), '/');

		String uri = request.getRequestURI();
		if (uri.startsWith(conetextPath + "/taotao/cloud/health/dump/")) {
			DumpProvider dumpProvider = ContextUtil.getBean(DumpProvider.class, false);
			if (Objects.nonNull(dumpProvider)) {
				if (uri.startsWith(conetextPath + "/taotao/cloud/health/dump/zip/")) {
					dumpProvider.zip(request.getParameter("name"));
				} else if (uri.startsWith(conetextPath + "/taotao/cloud/health/dump/download/")) {
					dumpProvider.download(request.getParameter("name"));
				} else if (uri.startsWith(conetextPath + "/taotao/cloud/health/dump/do/")) {
					dumpProvider.dump();
				} else {
					dumpProvider.list();
				}
			}
		}
	}

	@Override
	public void destroy() {

	}
}
