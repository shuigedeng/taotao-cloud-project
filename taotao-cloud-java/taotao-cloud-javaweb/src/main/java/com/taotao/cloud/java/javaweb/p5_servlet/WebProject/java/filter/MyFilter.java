package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
@WebFilter(value = "/t")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("--MyFilter--");
        //让请求继续
        filterChain.doFilter(servletRequest,servletResponse);

        System.out.println("--end--");
    }

    @Override
    public void destroy() {

    }
}
