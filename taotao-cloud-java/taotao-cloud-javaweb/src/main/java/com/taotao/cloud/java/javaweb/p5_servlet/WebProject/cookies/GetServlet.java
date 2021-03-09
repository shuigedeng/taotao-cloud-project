package com.taotao.cloud.java.javaweb.p5_servlet.WebProject.cookies;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet(value = "/get")
public class GetServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.通过request对象获取所有的cookie
        Cookie[] cookies = req.getCookies();
        //2.通过循环遍历Cookie
        if(cookies!=null){
            for(Cookie cookie : cookies){
                System.out.println(URLDecoder.decode(cookie.getName(),"UTF-8") +":"+URLDecoder.decode(cookie.getValue(),"UTF-8"));
            }
        }
        HttpSession session = req.getSession();
        session.invalidate();
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
