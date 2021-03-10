package com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.controller;


import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.EmpService;
import com.taotao.cloud.java.javaweb.p5_servlet.EmpProject.java.service.impl.EmpServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RemoveEmpController",value = "/manager/safe/removeEmpController")
public class RemoveEmpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("id"));

        EmpService empService = new EmpServiceImpl();

        empService.removeEmp(id);

        response.sendRedirect(request.getContextPath()+"/manager/safe/showAllEmpController");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
