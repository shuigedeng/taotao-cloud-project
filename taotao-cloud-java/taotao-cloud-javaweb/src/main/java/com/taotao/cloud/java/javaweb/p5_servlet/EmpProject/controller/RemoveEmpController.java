package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.controller;

import com.qf.emp.service.EmpService;
import com.qf.emp.service.impl.EmpServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveEmpController",value = "/manager/safe/removeEmpController")
public class RemoveEmpController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("id"));

        EmpService empService = new EmpServiceImpl();

        empService.removeEmp(id);

        response.sendRedirect(request.getContextPath()+"/manager/safe/showAllEmpController");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
