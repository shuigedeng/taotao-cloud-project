package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servlet;

import javax.servlet.*;
import java.io.IOException;

public class GenServlet extends GenericServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("genServlet init");
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("嘿嘿嘿");
    }

}
