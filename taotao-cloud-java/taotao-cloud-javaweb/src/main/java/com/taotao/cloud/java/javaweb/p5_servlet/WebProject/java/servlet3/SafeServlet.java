package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servlet3;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SafeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = "";
            //假设1、接收参数
            //2、调用业务逻辑 得到登录结果
            message = "登录成功";//登录失败！
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(message);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
