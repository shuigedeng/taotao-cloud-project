package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper;


import com.taotao.cloud.java.javaee.s2.c5_redis.web.java.pojo.Customer;
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
