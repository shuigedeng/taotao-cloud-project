package com.taotao.cloud.java.javaee.s2.c9_springcloud.other_service.java.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/list")
    public String list(){
        return "other-service: list";
    }

}
