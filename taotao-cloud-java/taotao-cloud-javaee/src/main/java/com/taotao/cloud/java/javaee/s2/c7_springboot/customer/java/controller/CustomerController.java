package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.controller;

import com.qf.openapi.customer.entity.Customer;
import com.qf.openapi.customer.service.CustomerService;
import com.qf.openapi.customer.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    @GetMapping(value = "/table",produces = "application/json;charset=utf-8")
    public String table(@RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "10") Integer limit,
                        String name,Integer state){
        //1. 封装参数
        Map<String,Object> parameter = new HashMap<>();
        parameter.put("page",page);
        parameter.put("limit",limit);
        parameter.put("name",name);
        parameter.put("state",state);

        //2. 调用service查询数据
        String result = customerService.findCustomerByQuery(parameter);

        //3. 返回
        return result;
    }


    @PostMapping("/add")
    public ResultVO add(Customer customer){
        try {
            //1. 调用service执行添加
            customerService.addCustomer(customer);
            //2. 返回json(成功)
            return new ResultVO(true,"添加成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            //3. 返回json(失败)
            return new ResultVO(false,e.getMessage());
        }

    }

}
