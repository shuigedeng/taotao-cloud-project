package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.counter;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShowCounterController",value = "/showCounterController")
public class ShowCounterController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取servletContext对象
        ServletContext servletContext = request.getServletContext();

        //2.获取计数器
        Integer counter = (Integer)servletContext.getAttribute("counter");

        if(counter == null){
            counter=1;
            servletContext.setAttribute("counter",counter);
        }else{
            counter++;
            servletContext.setAttribute("counter",counter);
        }
        System.out.println("show:"+counter);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
