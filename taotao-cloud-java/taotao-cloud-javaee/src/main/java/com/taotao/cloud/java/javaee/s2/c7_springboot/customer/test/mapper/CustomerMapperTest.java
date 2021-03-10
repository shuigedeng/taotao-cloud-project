package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.test.mapper;

import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.entity.Customer;
import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerMapperTest {

    @Autowired
    private CustomerMapper mapper;

    @Test
    void save() {
        Customer customer = new Customer();
        customer.setAddress("上海");
        customer.setUsername("bilibili");
        customer.setPassword("xxxxx");
        customer.setNickname("哔哩哔哩");
        customer.setState(1);

        Integer count = mapper.save(customer);

        System.out.println(customer);
        System.out.println(count);
    }
}
