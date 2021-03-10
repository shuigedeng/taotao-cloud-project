package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.client.SearchClient;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CustomerService {

    @Autowired
    private SearchClient searchClient;


    @CacheResult
    @HystrixCommand(commandKey = "findById")
    public Customer findById(@CacheKey Integer id) throws InterruptedException {
        return searchClient.findById(id);
    }

    @CacheRemove(commandKey = "findById")
    @HystrixCommand
    public void clearFindById(@CacheKey Integer id){
        System.out.println("findById被清空");
    }

}
