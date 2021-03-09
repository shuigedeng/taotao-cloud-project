package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.servlet.controller;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ServletContextController",value = "/ctxController")
public class ServletContextController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.通过this.getServletContext();
        ServletContext servletContext = this.getServletContext();
        //2.通过request对象获取
        ServletContext servletContext1 = request.getServletContext();
//        //3.通过session对象获取
//        HttpSession session = request.getSession();
//        ServletContext servletContext2 = session.getServletContext();
        System.out.println(servletContext.getRealPath("/"));
        System.out.println(servletContext.getContextPath());
        servletContext.setAttribute("context","info");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
