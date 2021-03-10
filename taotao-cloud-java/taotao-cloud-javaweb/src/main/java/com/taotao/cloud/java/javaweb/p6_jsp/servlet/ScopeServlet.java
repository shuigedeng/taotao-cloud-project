package com.taotao.cloud.java.javaweb.p6_jsp.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ScopeServlet",value = "/scope")
public class ScopeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("requestScope","bbb");
//        转发
//        request.getRequestDispatcher("/inner/innerObj.jsp").forward(request,response);
        request.getSession().setAttribute("sessionScope","ccc");
        request.getServletContext().setAttribute("servletContextScope","ddd");
        response.sendRedirect(request.getContextPath()+"/inner/innerObj.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
