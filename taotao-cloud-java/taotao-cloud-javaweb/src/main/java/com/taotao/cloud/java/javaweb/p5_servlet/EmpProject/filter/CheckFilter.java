package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.filter;

import com.qf.emp.entity.EmpManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebFilter(value = "/manager/safe/*")
public class CheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        HttpSession session = request.getSession();
        EmpManager empManager = (EmpManager)session.getAttribute("empManager");
        if(empManager!=null){//登录过
            filterChain.doFilter(request,response);
        }else{
            response.sendRedirect(request.getContextPath()+"/login.html");
        }
    }

    @Override
    public void destroy() {

    }
}
