package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servlet3;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet(value = "/ls")
public class LifeServlet implements Servlet {
    public LifeServlet(){
        System.out.println("1、实例化");
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("2、初始化");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("3、接收请求，响应结果");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
        System.out.println("4、销毁");
    }
}
