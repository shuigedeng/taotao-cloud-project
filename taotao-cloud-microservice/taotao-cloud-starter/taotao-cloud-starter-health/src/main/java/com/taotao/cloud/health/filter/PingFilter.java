package com.taotao.cloud.health.filter;

import lombok.val;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: chejiangyi
 * @version: 2019-09-24 20:59
 **/
public class PingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        val request = (HttpServletRequest) servletRequest;
        val response = (HttpServletResponse) servletResponse;
        val conetextPath = org.springframework.util.StringUtils.trimTrailingCharacter(request.getContextPath(),'/');
        val uri = request.getRequestURI();
        if(uri.startsWith(conetextPath+"/bsf/health/ping/")){
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().append("ok");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    @Override
    public void destroy() {

    }
}
