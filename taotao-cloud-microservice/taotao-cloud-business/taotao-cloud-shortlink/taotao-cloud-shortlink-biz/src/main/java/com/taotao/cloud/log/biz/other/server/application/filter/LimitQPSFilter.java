package com.taotao.cloud.log.biz.other.server.application.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.taotao.cloud.log.biz.other.server.application.exception.ErrorResponse;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LimitQPSFilter implements Filter {

	RateLimiter r = RateLimiter.create(10000);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		if (!r.tryAcquire(1)) {
			log.info("too many request");
			PrintWriter writer = response.getWriter();
			response.setCharacterEncoding("UTF-8");
			writer.write(ErrorResponse.serverError(""));
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
