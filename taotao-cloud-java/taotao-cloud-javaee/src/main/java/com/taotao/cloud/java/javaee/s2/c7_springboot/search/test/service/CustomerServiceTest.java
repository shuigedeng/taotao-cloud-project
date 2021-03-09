package com.taotao.cloud.java.javaee.s2.c7_springboot.search.test.service;

import com.qf.openapi.search.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest{

    @Autowired
    private CustomerService service;

    @Test
    void searchCustomerByQuery() throws IOException {

        Map<String, Object> param = new HashMap<>();
        param.put("page",1);
        param.put("limit",10);
        String s = service.searchCustomerByQuery(param);
        System.out.println(s);
    }


    @Test
    void save() throws IOException {
        Customer customer = new Customer();
        customer.setId(4);
        customer.setAddress("北京");
        customer.setUsername("kuaishou");
        customer.setPassword("xxxxx");
        customer.setNickname("快手");
        customer.setState(1);

        service.saveCustomer(customer);
    }
}
