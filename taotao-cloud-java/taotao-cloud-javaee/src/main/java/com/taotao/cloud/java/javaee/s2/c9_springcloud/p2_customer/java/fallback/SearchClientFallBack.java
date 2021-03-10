package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.fallback;

import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.client.SearchClient;
import com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class SearchClientFallBack implements SearchClient {
    @Override
    public String search() {
        return "出现问题啦！！！";
    }

    @Override
    public Customer findById(Integer id) {
        return null;
    }

    @Override
    public Customer getCustomer(Integer id, String name) {
        return null;
    }

    @Override
    public Customer save(Customer customer) {
        return null;
    }
}
