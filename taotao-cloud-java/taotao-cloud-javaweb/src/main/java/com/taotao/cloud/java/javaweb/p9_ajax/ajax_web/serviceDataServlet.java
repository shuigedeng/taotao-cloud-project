package com.taotao.cloud.java.javaweb.p9_ajax.ajax_web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/data")
public class serviceDataServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name=req.getParameter("name");
        String age=req.getParameter("age");
        System.out.println("name === " + name);
        System.out.println("age === " + age);
        String mess="hello ajax!";
        PrintWriter writer = resp.getWriter();
        writer.write(mess);
        writer.flush();
        writer.close();
    }
}
