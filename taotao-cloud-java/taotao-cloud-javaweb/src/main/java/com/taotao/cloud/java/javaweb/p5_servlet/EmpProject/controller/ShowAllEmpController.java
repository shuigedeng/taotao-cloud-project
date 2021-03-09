package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.controller;

import com.qf.emp.entity.Emp;
import com.qf.emp.service.EmpService;
import com.qf.emp.service.impl.EmpServiceImpl;
import sun.security.util.AuthResources_it;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShowAllEmpController",value = "/manager/safe/showAllEmpController")
public class ShowAllEmpController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        EmpService empService = new EmpServiceImpl();
        List<Emp> emps = empService.showAllEmp();

        request.setAttribute("emps",emps);

        request.getRequestDispatcher("/manager/safe/showAllEmpJSP").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
