package com.taotao.cloud.java.javaee.s2.c9_springcloud.p2_customer.java.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("OTHER-SERVICE")
public interface OtherServiceClient {

    @RequestMapping("/list")
    String list();

}
