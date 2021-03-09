package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.servletProject.servlet.controller;

import com.qf.servletProject.entity.Admin;
import com.qf.servletProject.entity.Manager;
import com.qf.servletProject.service.AdminService;
import com.qf.servletProject.service.impl.AdminServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(value = "/showallcontroller")
public class ShowAllAdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //通过HttpSession完成权限控制
//        HttpSession session = req.getSession();
//        Manager mgr  =(Manager)session.getAttribute("mgr");
//        if(mgr !=null){
            //只负责调用业务逻辑功能
            AdminService adminService = new AdminServiceImpl();

            List<Admin> adminList = adminService.showAllAdmin();

            //request作用域存储数据
            req.setAttribute("admins",adminList);
            //通过转发 跳转到显示结果servlet
            req.getRequestDispatcher("/showalljsp").forward(req,resp);
//        }else{
//            resp.sendRedirect("/WebProject_war_exploded/loginMgr.html");
//        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
