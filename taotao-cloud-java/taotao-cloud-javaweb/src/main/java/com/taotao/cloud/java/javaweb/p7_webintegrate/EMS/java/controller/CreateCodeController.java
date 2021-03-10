package com.taotao.cloud.java.javaweb.p7_webintegrate.EMS.java.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CreateCodeController",value = "/createCode")
public class CreateCodeController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	//import cn.dsna.util.images.ValidateCode;
        //ValidateCode validateCode = new ValidateCode(200,30,4,10);
        //String codes = validateCode.getCode();
        HttpSession session = request.getSession();
        session.setAttribute("codes","12");

        //validateCode.write(response.getOutputStream());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
