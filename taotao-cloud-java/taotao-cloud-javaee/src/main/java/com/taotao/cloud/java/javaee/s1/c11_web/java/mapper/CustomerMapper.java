package com.taotao.cloud.java.javaee.s1.c11_web.java.mapper;

import com.qianfeng.openapi.web.master.pojo.Customer;

import java.util.List;
/**
*  @author author
*/
public interface CustomerMapper {

    int insertCustomer(Customer object);

    int updateCustomer(Customer object);

    int update(Customer.UpdateBuilder object);

    List<Customer> queryCustomer(Customer object);

    Customer queryCustomerLimit1(Customer object);

    Customer getCustomerById(int id);

    List<Customer> getAllCustomer();
}
