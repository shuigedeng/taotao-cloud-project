package com.taotao.cloud.java.javaee.s1.c5_springmvc.p3.java;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/origin")
@CrossOrigin("http://localhost:8989")
public class OriginController {

    @RequestMapping("/test1")
    public String test1(HttpSession session){
        System.out.println("test1~~");
        session.setAttribute("name","shine");
        return "ok";
    }

    @RequestMapping("/test2")
    public String test2(HttpSession session){
        System.out.println("test2~~");
        String name = (String)session.getAttribute("name");
        System.out.println("name:"+name);
        return "ok";
    }
}
