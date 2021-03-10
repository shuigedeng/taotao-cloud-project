package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.java.cookies;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/cs")
public class CookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.服务端创建Cookie对象
        Cookie cookie = new Cookie("username","gavin");
        Cookie cookie2 = new Cookie("password","123456");
        //1.1设置Cookie的访问路径
        cookie.setPath("/WebProject_war_exploded/get");
        cookie2.setPath("/WebProject_war_exploded");
        //1.2 设置Cookie的有效期
        cookie.setMaxAge(60*60);
        cookie2.setMaxAge(60*60);
        //2.将Cookie响应给客户端
        resp.addCookie(cookie);
        resp.addCookie(cookie2);



    }
}
