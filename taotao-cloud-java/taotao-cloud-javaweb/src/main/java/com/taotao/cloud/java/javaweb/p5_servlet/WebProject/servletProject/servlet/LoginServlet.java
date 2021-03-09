package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.servlet;

import com.qf.servletProject.entity.Admin;
import com.qf.servletProject.service.AdminService;
import com.qf.servletProject.service.impl.AdminServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        //1.收参
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //2.调用业务逻辑
        AdminService adminService = new AdminServiceImpl();
        Admin admin = adminService.login(username,password);
        //3.处理结果
        PrintWriter printWriter = resp.getWriter();
        if(admin!=null){
            //响应给客户端一个结果页面，显示登录成功
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>结果页面</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<h1>登录成功！</h1>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        }else{
            //响应给客户端一个结果页面，显示登录失败！
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>结果页面</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<h1>登录失败！</h1>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
