package com.taotao.cloud.java.javaee.s2.c9_springcloud.p3_search.java.controller;


import com.taotao.cloud.java.javaee.s2.c9_springcloud.p3_search.java.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {


    @Value("${server.port}")
    private String port;

    @GetMapping("/search")
    public String search(){
        return "search：" + port;
    }


    @GetMapping("/search/{id}")
    public Customer findById(@PathVariable Integer id){
        return new Customer(1,"张三",(int)(Math.random() * 100000));
    }

    @GetMapping("/getCustomer")
    public Customer getCustomer(@RequestParam Integer id,@RequestParam String name){
        return new Customer(id,name,23);
    }

    @PostMapping("/save")
    public Customer save(@RequestBody Customer customer){
        return customer;
    }

}
