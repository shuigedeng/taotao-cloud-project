package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 *  查询： 所有用户
 *        查询id=xx 某一个用户
 *  删除： id=xx 某一个用户
 *  增加： 在所有用户中 增加一个
 *  修改： 在所有用户中 修改一个
 *
 *  资源： 所有用户    /users
 *        id=xx 某个用户   /users/{id}
 */
@RestController
public class MyRestController {

    @GetMapping("/users")
    public List<User> queryUsers(){
        System.out.println("query users with get");
        User user1 = new User(1, "张三");
        User user2 = new User(1, "李四");
        return Arrays.asList(user1,user2);
    }

    @GetMapping("/users/{id}")
    public User queryOne(@PathVariable Integer id){
        System.out.println("query one user with get:"+id);
        return new User(1, "张三");
    }

    @DeleteMapping("/users/{id}")
    public String deleteOne(@PathVariable Integer id){
        System.out.println("delete one user with delete :"+id);
        return "ok";
    }

    @PostMapping("/users")
    public String saveUser(@RequestBody User user){
        System.out.println("save user with post:"+user);
        return "ok";
    }

    @PutMapping("/users")
    public String updateUser(@RequestBody User user){
        System.out.println("update user with put:"+user);
        return "ok";
    }
}
