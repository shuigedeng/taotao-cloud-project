package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.service.impl;

import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.entity.Customer;
import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.mapper.CustomerMapper;
import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.service.CustomerService;
import com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.utils.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public String findCustomerByQuery(Map<String, Object> parameter) {
        //1. 准备请求参数和请求头信息
        String json = JSON.toJSON(parameter);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
        HttpEntity<String> entity = new HttpEntity<>(json,headers);

        //2. 使用RestTemplate调用搜索模块
        String result = restTemplate.postForObject("http://localhost:8080/search/customer/table", entity, String.class);
        return result;
    }

    @Override
    @Transactional
    public void addCustomer(Customer customer) {
        //1. 调用mapper添加数据到MySQL
        Integer count = customerMapper.save(customer);
        //2. 判断添加是否成功
        if(count != 1){
            log.error("【添加客户信息失败】 customer = {}",customer);
            throw new RuntimeException("【添加客户信息失败】");
        }
        //3. 调用搜索模块，添加数据到ES
        //1. 准备请求参数和请求头信息
        String json = JSON.toJSON(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=utf-8"));
        HttpEntity<String> entity = new HttpEntity<>(json,headers);

        //2. 使用RestTemplate调用搜索模块
        restTemplate.postForObject("http://localhost:8080/search/customer/add", entity, String.class);

    }
}
