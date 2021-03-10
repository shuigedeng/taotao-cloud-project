package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.filter;

import javax.servlet.*;
import java.io.IOException;

public class XmlFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("Web.Xml配置过滤器");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
