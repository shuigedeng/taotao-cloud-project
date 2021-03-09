package com.taotao.cloud.java.javaee.s2.c7_springboot.search.java.service;

import com.qf.openapi.search.entity.Customer;

import java.io.IOException;
import java.util.Map;

public interface CustomerService {


    String searchCustomerByQuery(Map<String,Object> param) throws IOException;


    void saveCustomer(Customer customer) throws IOException;


}
