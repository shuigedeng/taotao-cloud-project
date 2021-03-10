package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.servlet.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShowContextController",value = "/showController")
public class ShowContextController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.先获取servletContext对象
        ServletContext servletContext = this.getServletContext();

        String s  = (String)servletContext.getAttribute("context");

        System.out.println(s);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
