package com.taotao.cloud.java.javaee.s1.c5_springmvc.p4.java.web;

import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.User;
import com.qf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;
    @GetMapping("/users")
    public String queryUsers(Model model, Page page){
        System.out.println("queryUsers");
        PageInfo<User> pageInfo = userService.queryUsers(page);
        model.addAttribute("data",pageInfo);
        return "userPro";
    }

    @RequestMapping("/users/{id}")
    public String queryOne(@PathVariable Integer id){
        System.out.println("query user id:"+id);
        return "index";
    }

    @PostMapping("/users")
    public String updateUser(User user){
        System.out.println("update User:"+user);
        return "index";
    }
}
