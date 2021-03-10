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

@WebServlet(name = "ShowEmpController",value = "/manager/safe/showEmp")
public class ShowEmpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = Integer.valueOf(request.getParameter("id"));

        EmpService empService = new EmpServiceImpl();
        Emp emp = empService.selectEmpById(id);

        request.setAttribute("emp",emp);

        request.getRequestDispatcher("/updateEmp.jsp").forward(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
