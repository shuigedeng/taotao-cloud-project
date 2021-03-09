package com.taotao.cloud.java.javaee.s2.c7_springboot.customer.java.service;

import com.qf.openapi.customer.entity.Customer;

import java.util.Map;

public interface CustomerService {


    String findCustomerByQuery(Map<String,Object> parameter);


    void addCustomer(Customer customer);


}
