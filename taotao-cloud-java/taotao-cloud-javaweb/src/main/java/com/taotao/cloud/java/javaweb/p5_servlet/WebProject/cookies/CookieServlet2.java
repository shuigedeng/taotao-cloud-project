package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.cookies;

import com.qf.servlet.HttpsServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/cs2")
public class CookieServlet2 extends HttpsServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie cookie = new Cookie("username","aaron");

        cookie.setPath("/WebProject_war_exploded/get");

        cookie.setMaxAge(60*60*24*7);

        resp.addCookie(cookie);
    }
}
