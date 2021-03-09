package com.taotao.cloud.java.javaee.s1.c5_springmvc.p1.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jump")
public class ForwardController {

    @RequestMapping("/test1")
    public String test1(){
        System.out.println("test1");
        return "hello"; // 转发
        //return "forward:/hello.jsp";
    }

    @RequestMapping("/test2")
    public String test2(){
        System.out.println("test2");
        //return "forward:/jump/test1";//转发
        return "forward:test1";//相对路径
    }

    @RequestMapping("/test3")
    public String test3(){
        System.out.println("test3");
        return "redirect:/hello.jsp"; // 冲顶向到hello.jsp
    }

    @RequestMapping("/test4")
    public String test4(){
        System.out.println("test4");
        return "redirect:test3";
        //return "redirect:/jump/test3"
    }

    @RequestMapping("/query")
    public String test5(){
        System.out.println("querr数据1");
        return "forward:test1";
    }
}
