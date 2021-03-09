package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/inter")
@SessionAttributes("state")
public class InterController {

    @RequestMapping("/login")
    public String login(Model model){
        // 登录后留取状态标记
        model.addAttribute("state","ok");
        return "index";
    }

    @RequestMapping("/a/test1")
    public String test1(Model model){
        // 判断登录状态
        System.out.println("执行service11");
        return "index";
    }

    @RequestMapping("/b/test2")
    public String test2(Model model){
        // 判断登录状态
        System.out.println("执行service22");
        return "index";
    }
}
