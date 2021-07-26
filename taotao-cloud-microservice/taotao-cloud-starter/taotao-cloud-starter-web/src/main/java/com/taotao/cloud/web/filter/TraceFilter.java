package com.taotao.cloud.web.filter;


import com.taotao.cloud.common.context.TraceContextHolder;
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
 * 日志链路追踪过滤器
 *
 * @date 2020-9-8
 */
@AllArgsConstructor
public class TraceFilter extends OncePerRequestFilter {

	private final FilterProperties filterProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		if (request.getRequestURI().startsWith("/actuator")) {
			return true;
		}
		return !filterProperties.getTrace();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			String traceId = TraceUtil.getTraceId(request);
			TraceContextHolder.setTraceId(traceId);
			TraceUtil.mdcTraceId(traceId);

			TraceUtil.mdcZipkinTraceId(request);
			TraceUtil.mdcZipkinSpanId(request);
			filterChain.doFilter(request, response);
		} finally {
			TraceContextHolder.clear();;
			MDC.clear();
		}
	}
}
