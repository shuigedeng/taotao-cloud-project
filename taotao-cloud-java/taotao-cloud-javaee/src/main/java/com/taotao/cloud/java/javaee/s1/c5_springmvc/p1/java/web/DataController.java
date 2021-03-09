package com.taotao.cloud.java.javaee.s1.c5_springmvc.p1.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/data")
@SessionAttributes(names = {"city","street"})
public class DataController {

    @RequestMapping("/test1")
    public String test1(HttpServletRequest request, HttpSession session){
        System.out.println("test1");
        request.setAttribute("name","张三");
        session.setAttribute("age",18);
        return "data";
    }

    @RequestMapping("/test2")
    public String test2(Model model){
        //model.addAttribute("gender",true);
        model.addAttribute("city","北京");
        model.addAttribute("street","长安街");
        return "data2";
    }

    @RequestMapping("/test3")
    public String test3(SessionStatus status){
        // 清空所有 通过model存入session
        status.setComplete();
        return "data2";
    }

    @RequestMapping("/test4")
    public ModelAndView test4(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/hello.jsp");
        modelAndView.addObject("claz","001");
        return modelAndView;
    }
}
