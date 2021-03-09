package com.taotao.cloud.java.javaee.s1.c3_mybatis.mybatispage.java.controller;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.UserService;
import com.qf.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 分页查询
 */

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 接收参数  pageNum pageSize
        String pageNum = req.getParameter("pageNum");
        String pageSize = req.getParameter("pageSize");
        Page page = new Page();
        if(pageNum!=null){
            page.setPageNum(Integer.parseInt(pageNum));
        }
        if(pageSize!=null){
            page.setPageSize(Integer.parseInt(pageSize));
        }
        // 调用service
        PageInfo<User> pageData = userService.queryUsers(page);
        // 存入作用域
        req.setAttribute("pageData",pageData);
        // 跳转页面
        req.getRequestDispatcher("/users.jsp").forward(req,resp);
    }
}
