package com.taotao.cloud.java.javaee.s1.c5_springmvc.p1.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller  // 声明后端控制器
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/test1")
    public String hello1(){// service doGet doPost
        System.out.println("hello1");
        return "hello"; // /hello.jsp
    }

    @RequestMapping("/test2")
    public String hello2(){// service doGet doPost
        System.out.println("hello2");
        return "hello2";
    }

}
