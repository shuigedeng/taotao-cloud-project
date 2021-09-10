package com.taotao.cloud.health.filter;


import com.taotao.cloud.health.dump.DumpProvider;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author: chejiangyi
 * @version: 2019-09-07 13:31
 **/
public class DumpFilter implements Filter {

	@Autowired
	private DumpProvider dumpProvider;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//ServletContext servletContext = filterConfig.getServletContext();
		//WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(
		//	servletContext);
		//if (Objects.nonNull(webApplicationContext)) {
		//	dumpProvider = webApplicationContext.getBean(DumpProvider.class);
		//}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String conetextPath = org.springframework.util.StringUtils.trimTrailingCharacter(
			request.getContextPath(), '/');

		String uri = request.getRequestURI();
		if (uri.startsWith(conetextPath + "/taotao/cloud/health/dump")) {
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
