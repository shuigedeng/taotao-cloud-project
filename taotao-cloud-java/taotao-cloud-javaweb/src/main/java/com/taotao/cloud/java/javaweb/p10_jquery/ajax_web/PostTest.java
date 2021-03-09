package com.taotao.cloud.java.javaweb.p10_jquery.ajax_web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/post")
public class PostTest extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        System.out.println("name="+name+";age = " + age);
        PrintWriter writer = resp.getWriter();
        //writer.write("hello ajax!!!");
        String json="{\"res\":\"success\"}";
        writer.write(json);
    }
}
