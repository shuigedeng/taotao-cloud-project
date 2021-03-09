package com.taotao.cloud.java.javaee.s1.c5_springmvc.p1.java.web;

import com.qf.entity.User;
import com.qf.entity.UserList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping("/param")
public class ParamController {


    // http://xxxx/param/test1?id=1&name=shine&gender=true&birth=2020/12/12 12:13:20
    @RequestMapping("/test1")
    public String test1(Integer id, String name, Boolean gender, Date birth){
        System.out.println("test1");
        System.out.println("id:"+id+" name:"+name+" gender:"+gender+" birth:"+birth);
        return "hello";
    }


    // http://xxxx/param/test1?id=1&name=shine&gender=true&birth=2020/12/12 12:13:20
    @RequestMapping("/test2")
    public String test2(User user){
        System.out.println("test2");
        System.out.println(user);
        return "hello";
    }

    // http://xxxx/param/test3?hobby=football&hobby=basketball&hobby=volleyball
    @RequestMapping("/test3")
//    public String test3(Stirng[] hobby){
    public String test3(User user){
        System.out.println("test3");
        System.out.println(user);
        return "hello";
    }

    // http://xxx/param/test4?users[0].id=1&users[0].name=shine&users[0].gender=true&users[1].id=2&users[1].name=zhangsan
    @RequestMapping("/test4")
    public String test4(UserList userList){
        System.out.println("test4");
        for (User user : userList.getUsers()) {
            System.out.println(user);
        }
        return "hello";
    }

    // {id} 命名路径
    // {id} 等价于*   /test5/1   test5/2   test5/xxxx
    @RequestMapping("/test5/{id}")
    public String test5(@PathVariable("id") Integer id){
        System.out.println("test5");
        System.out.println("id:"+id);
        return "hello";
    }

    @RequestMapping("/test6/{id}/{name}")
    public String test6(@PathVariable Integer id,@PathVariable("name") String name2){
        System.out.println("test6");
        System.out.println("id:"+id);
        System.out.println("name:"+name2);
        return "hello";
    }
}
