package com.taotao.cloud.java.javaee.s2.c7_springboot.springboot.java.controller;

import com.qf.firstspringboot.entity.User;
import com.qf.firstspringboot.properties.AliyunProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource(name = "user1")
    private User user;

    @GetMapping("/test")
    public String test(){
        return "Hello SpringBoot!";
    }


    @GetMapping("/user")
    public User user(){
        return user;
    }

    @Value("${picPath}")
    private String picPath;

    @Autowired
    private AliyunProperties properties;

    @GetMapping("/picPath")
    public String picPath(){
        return picPath;
    }


    @GetMapping("/aliyun")
    public AliyunProperties aliyun(){
        return properties;
    }

}
