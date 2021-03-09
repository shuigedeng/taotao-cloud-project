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
import java.util.List;

@WebServlet(value = "/showall")
public class ShowAllAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        AdminService adminService = new AdminServiceImpl();

        List<Admin> adminList = adminService.showAllAdmin();

        PrintWriter printWriter = resp.getWriter();

        if(adminList!=null){
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>显示所有</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<table border='1'>");
            printWriter.println("   <tr>");
            printWriter.println("       <td>username</td>");
            printWriter.println("       <td>password</td>");
            printWriter.println("       <td>phone</td>");
            printWriter.println("       <td>address</td>");
            printWriter.println("   </tr>");
            for(Admin admin : adminList){
                printWriter.println("   <tr>");
                printWriter.println("       <td>"+admin.getUsername()+"</td>");
                printWriter.println("       <td>"+admin.getPassword()+"</td>");
                printWriter.println("       <td>"+admin.getPhone()+"</td>");
                printWriter.println("       <td>"+admin.getAddress()+"</td>");
                printWriter.println("   </tr>");
            }
            printWriter.println("</table>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        }else{
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>显示所有</title>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<h3>当前没有用户！</h3>");
            printWriter.println("</body>");
            printWriter.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
