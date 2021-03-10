package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.servletProject.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/a")
public class AServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //通过request作用域做数据的存储
//        req.setAttribute("username","gavin");
//        req.getRequestDispatcher("/b").forward(req,resp);


        resp.sendRedirect("/WebProject_war_exploded/b?username=tom");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
