package com.taotao.cloud.netty.itcast.test;

import com.taotao.cloud.netty.itcast.server.service.HelloService;
import com.taotao.cloud.netty.itcast.server.service.ServicesFactory;

public class TestServicesFactory {
    public static void main(String[] args) {
        HelloService service = ServicesFactory.getService(HelloService.class);
        System.out.println(service.sayHello("hi"));
    }
}
