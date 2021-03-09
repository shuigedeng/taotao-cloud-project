package com.taotao.cloud.java.javaee.s2.c7_springboot.search.java.controller;


import com.qf.openapi.search.entity.Customer;
import com.qf.openapi.search.service.CustomerService;
import org.elasticsearch.cluster.ClusterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/search/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/table",produces = "application/json;charset=utf-8")
    public String table(@RequestBody Map<String,Object> param) throws IOException {
        //1. 调用service查询数据
        String result = customerService.searchCustomerByQuery(param);
        //2. 直接返回
        return result;
    }


    @PostMapping(value = "/add")
    public void add(@RequestBody Customer customer) throws IOException {
        //1. 调用service添加数据
        customerService.saveCustomer(customer);
    }

}
