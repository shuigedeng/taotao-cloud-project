package com.taotao.cloud.java.javaweb.p12_myshop.controller;


import cn.dsna.util.images.ValidateCode;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 生成验证码的controller
 */
@WebServlet("/code")
public class CodeController extends BaseServlet {

    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.生成验证码对象
        //int width, int height, int codeCount, int lineCount
        ValidateCode validateCode = new ValidateCode(100,35,5,20);
        //2.将验证码放入到session
        String code = validateCode.getCode();
        request.getSession().setAttribute("code",code);
        //3.向页面写回验证码
        ServletOutputStream outputStream = response.getOutputStream();
        validateCode.write(outputStream);
    }
}
