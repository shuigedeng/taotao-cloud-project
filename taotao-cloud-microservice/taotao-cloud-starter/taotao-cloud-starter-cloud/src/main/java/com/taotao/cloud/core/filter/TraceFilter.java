package com.taotao.cloud.core.filter;

import com.taotao.cloud.core.props.MateRequestProperties;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志链路追踪过滤器
 * @date 2020-9-8
 */
@ConditionalOnClass(Filter.class)
public class TraceFilter extends OncePerRequestFilter {

    @Autowired
    private MateRequestProperties mateRequestProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !mateRequestProperties.getTrace();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String traceId = TraceUtil.getTraceId(request);
            TraceUtil.mdcTraceId(traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }

    }
}
