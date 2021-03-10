package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.ex.MyException1;
import com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.ex.MyException2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ex")
public class ExController {

    @RequestMapping("/test1")
    public String test1(Integer id){
        System.out.println("test1");
        // 调用Serivce时，可能会抛出 MyException1  MyException3
        if(id==1){
            throw new MyException1("test ex1");
        }
        return "succees";
    }
    @RequestMapping("/test2")
    public String test2(Integer id){
        System.out.println("test1");
        // 调用Serivce时，可能会抛出 MyException1  MyException2
        if(id==1){
            throw new MyException2("test ex2");
        }
        return "succees";
    }
}
