package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpsServlet  extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("Httpsservlet init");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("这是get请求过来的内容");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("这是post请求过来的内容");
    }
}
