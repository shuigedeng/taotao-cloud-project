package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.entity.User;
import com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.entity.User2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

//@Controller
@RequestMapping("/json")
//@RestController
public class JsonController {

    @RequestMapping("/test1")
//    @ResponseBody
    public User test1(){
        System.out.println("test1");
        User user = new User(1,"张三",new Date());
//        List<String> hobby = Arrays.asList("football","basketball");
        List<String> hobby = new ArrayList();
        user.setHobby(hobby);
        return user;
    }

    @RequestMapping("/test11")
//    @ResponseBody
    public User2 test11(){
        System.out.println("test11");
        User2 user2 = new User2(1, "shine", "beijing", new Date(), 1000.5);
        return user2;
    }

    @RequestMapping("/test2")
//    @ResponseBody
    public List<User> test2(){
        System.out.println("test1");
        User user = new User(1,"张三");
        User user2 = new User(1,"李四");
        List<User> users = Arrays.asList(user,user2);
        return users;
    }

    @RequestMapping(value="/test3",produces = "text/html;charset=utf-8")
//    @ResponseBody
    public String test3(){
        System.out.println("test3");
        return "你好";
    }

    @RequestMapping("/test4")
    public String test4(@RequestBody User user){
        System.out.println(user);
        return "ok";
    }
}
