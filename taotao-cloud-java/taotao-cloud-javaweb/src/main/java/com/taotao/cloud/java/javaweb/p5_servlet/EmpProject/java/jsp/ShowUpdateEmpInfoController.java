package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.jsp;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.Emp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ShowUpdateEmpInfoController",value = "/manager/safe/showUpdateEmpInfoJSP")
public class ShowUpdateEmpInfoController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Emp emp = (Emp)request.getAttribute("emp");

        PrintWriter printWriter = response.getWriter();

        printWriter.println("<html>");
        printWriter.println("   <head>");
        printWriter.println("       <meta charset='UTF-8'>");
        printWriter.println("       <title>修改员工信息页面</title>");
        printWriter.println("   </head>");
        printWriter.println("   <body>");
        printWriter.println("       <form action='/empproject/manager/safe/updateEmpController' method='post'>");
        printWriter.println("       编号：<input type='text' name='id' value='"+emp.getId()+"' readonly/><br/>");
        printWriter.println("       姓名：<input type='text' name='name' value='"+emp.getName()+"'/><br/>");
        printWriter.println("       工资：<input type='text' name='salary' value='"+emp.getSalary()+"'/><br/>");
        printWriter.println("       年龄：<input type='text' name='age' value='"+emp.getAge()+"'/><br/>");
        printWriter.println("       <input type='submit'  value='修改'/><br/>");
        printWriter.println("       </form>");
        printWriter.println("   </body>");
        printWriter.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
