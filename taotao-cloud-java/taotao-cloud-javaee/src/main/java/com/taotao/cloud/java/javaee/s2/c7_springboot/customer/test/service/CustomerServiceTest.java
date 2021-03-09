package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.test.service;

import com.qf.openapi.customer.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @Test
    void findCustomerByQuery() {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("page",1);
        parameter.put("limit",10);
        parameter.put("name","haier");
        String result = service.findCustomerByQuery(parameter);
        System.out.println(result);
    }

    @Test
    void save(){
        Customer customer = new Customer();
        customer.setAddress("test");
        customer.setUsername("test");
        customer.setPassword("test");
        customer.setNickname("test");
        customer.setState(1);

        service.addCustomer(customer);

    }
}
