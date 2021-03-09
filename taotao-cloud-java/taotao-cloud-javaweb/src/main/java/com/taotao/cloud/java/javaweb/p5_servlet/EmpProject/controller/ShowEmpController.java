package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.controller;

import com.qf.emp.entity.Emp;
import com.qf.emp.service.EmpService;
import com.qf.emp.service.impl.EmpServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ShowEmpController",value = "/manager/safe/showEmpController")
public class ShowEmpController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("id"));

        EmpService empService = new EmpServiceImpl();
        Emp emp = empService.showEmp(id);

        request.setAttribute("emp",emp);

        request.getRequestDispatcher("/manager/safe/showUpdateEmpInfoJSP").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
