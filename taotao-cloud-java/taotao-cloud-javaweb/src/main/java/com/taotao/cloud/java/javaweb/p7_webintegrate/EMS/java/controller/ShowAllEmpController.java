package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.controller;


import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Emp;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.entity.Page;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.service.EmpService;
import com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.service.impl.EmpServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ShowAllEmpController",value = "/manager/safe/showAllEmp")
public class ShowAllEmpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageIndex = request.getParameter("pageIndex");
        if(pageIndex == null){
            pageIndex="1";
        }

        Page page = new Page(Integer.valueOf(pageIndex));

        EmpService empService = new EmpServiceImpl();
        List<Emp> emps = empService.showAllEmpByPage(page);

        request.setAttribute("emps",emps);
        request.setAttribute("page",page);

        request.getRequestDispatcher("/emplist.jsp").forward(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
