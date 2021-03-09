package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.cookies;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(value = "/cs3")
public class CookieServlet3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie cookie =new Cookie(URLEncoder.encode("姓名","UTF-8"),URLEncoder.encode("张三","UTF-8"));

        cookie.setPath("/WebProject_war_exploded/get");

        cookie.setMaxAge(600);

        resp.addCookie(cookie);
    }
}
