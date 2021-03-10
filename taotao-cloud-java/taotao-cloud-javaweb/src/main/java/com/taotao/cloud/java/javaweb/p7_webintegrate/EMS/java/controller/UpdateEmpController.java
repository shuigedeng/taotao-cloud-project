package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.controller;


import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Emp;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.service.EmpService;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.service.impl.EmpServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateEmpController",value = "/manager/safe/updateEmp")
public class UpdateEmpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("name");
        Double salary = Double.valueOf(request.getParameter("salary"));
        Integer age = Integer.valueOf(request.getParameter("age"));

        Emp emp = new Emp(id,name,salary,age);

        EmpService empService = new EmpServiceImpl();
        empService.modifyEmp(emp);

        response.sendRedirect(request.getContextPath()+"/manager/safe/showAllEmp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
