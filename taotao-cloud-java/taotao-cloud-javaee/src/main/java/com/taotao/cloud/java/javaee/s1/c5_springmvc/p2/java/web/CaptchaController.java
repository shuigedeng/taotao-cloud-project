package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @RequestMapping("/test1")
    public String test1(String captcha, HttpSession session){
        // 比对
        String realCap = (String)session.getAttribute("captcha");
        if(realCap.equalsIgnoreCase(captcha)){
            return "index";
        }
        return "error1";
    }
}
