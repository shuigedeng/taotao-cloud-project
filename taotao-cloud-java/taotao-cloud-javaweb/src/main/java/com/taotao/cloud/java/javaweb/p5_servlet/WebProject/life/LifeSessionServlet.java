package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.life;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "LifeSessionServlet",value = "/life")
public class LifeSessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

//        session.setMaxInactiveInterval(10);//设置session的有效期为10秒

        String newUrl = response.encodeRedirectURL("/WebProject_war_exploded/gets");

        System.out.println(newUrl);

        response.sendRedirect(newUrl);

        System.out.println(session.getId());


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
