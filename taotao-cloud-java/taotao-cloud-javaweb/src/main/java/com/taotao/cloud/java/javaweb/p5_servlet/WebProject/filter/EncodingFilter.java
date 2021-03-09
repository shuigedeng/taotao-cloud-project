package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@WebFilter(value = "/login2")
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //统一处理请求和响应的乱码
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html;charset=utf-8");

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
