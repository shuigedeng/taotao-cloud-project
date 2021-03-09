package com.taotao.cloud.java.javaweb.p12_myshop.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

         //1.设置编码格式（请求和响应）
           servletRequest.setCharacterEncoding("utf-8");
           servletResponse.setContentType("text/html;charset=utf-8");
         //2.放行
           filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
