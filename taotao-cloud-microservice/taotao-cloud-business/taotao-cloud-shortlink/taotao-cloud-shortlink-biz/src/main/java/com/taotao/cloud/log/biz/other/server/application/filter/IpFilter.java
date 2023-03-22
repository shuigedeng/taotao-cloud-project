package com.taotao.cloud.log.biz.other.server.application.filter;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IpFilter implements Filter {

	@Value("${black.ip.list}")
	private Set<String> blackIpList;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String ip = IPUtil.getRemoteIpAddr(req);

		if (blackIpList.contains(ip)) {
			log.info("{} 命中ip黑名单", ip);
			PrintWriter writer = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			writer.write(ErrorResponse.notFound(""));
			writer.flush();
			writer.close();
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
// TODO Auto-generated method stub

	}

}
