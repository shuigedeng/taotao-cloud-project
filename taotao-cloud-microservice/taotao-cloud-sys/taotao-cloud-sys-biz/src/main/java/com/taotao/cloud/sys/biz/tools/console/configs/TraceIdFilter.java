package com.taotao.cloud.sys.biz.tools.console.configs;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

@WebFilter(urlPatterns = "/*", filterName = "traceIdFilter")
public class TraceIdFilter implements Filter {
    private static final String UNIQUE_ID = "traceId";

//    private static final IdGenerator idGenerator = new SimpleIdGenerator();
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        UUID uuid = idGenerator.generateId();
//        String uniqueId = uuid.toString().replace("-", "");
//        uniqueId = StringUtils.substring(uniqueId,-10);
//        MDC.put(UNIQUE_ID, uniqueId);
//        try {
//            HttpServletResponse response = (HttpServletResponse) servletResponse;
//            response.addHeader("traceId",uniqueId);
//            filterChain.doFilter(servletRequest, servletResponse);
//        }finally {
//            MDC.remove(UNIQUE_ID);
//        }
//    }

    /**
     * 优化 traceId 生成, 生成 100 万次 id 大概用时 60ms
     * 一个不断自增的值, 重启后不唯一
     */
    AtomicLong leastSigBits = new AtomicLong(0);

    static final long var3 = 1L << 10 * 4;       // 这里的 10 表示 10 位数,每一位是 16 进制, 能表示 16^10 个 id
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String uniqueId = Long.toHexString(var3 | leastSigBits.incrementAndGet() & var3 - 1L).substring(1);
        MDC.put(UNIQUE_ID, uniqueId);
        try {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.addHeader(UNIQUE_ID,uniqueId);
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            MDC.remove(UNIQUE_ID);
        }
    }
}
