package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/test1")
    public String test1(){
        System.out.println("test1");
        return "forward:test2.action";
    }

    @RequestMapping("/test2")
    public String test2(){
        System.out.println("test1");
        return "index";
    }
}
