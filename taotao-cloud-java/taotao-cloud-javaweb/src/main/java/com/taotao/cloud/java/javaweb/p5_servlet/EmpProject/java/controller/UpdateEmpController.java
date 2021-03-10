package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.controller;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.entity.Emp;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.EmpService;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.impl.EmpServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateEmpController",value = "/manager/safe/updateEmpController")
public class UpdateEmpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.收参
        Integer id = Integer.valueOf(request.getParameter("id"));
        String name = request.getParameter("name");
        Double salary = Double.valueOf(request.getParameter("salary"));
        Integer age = Integer.valueOf(request.getParameter("age"));

        Emp emp = new Emp(id,name,salary,age);

        EmpService empService = new EmpServiceImpl();
        empService.modify(emp);

        response.sendRedirect(request.getContextPath()+"/manager/safe/showAllEmpController");


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
