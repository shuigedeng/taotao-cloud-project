package com.taotao.cloud.java.concurrent.springannotation.userdefinedannotation.service.impl;

import com.taotao.cloud.java.concurrent.springannotation.userdefinedannotation.annotation.RpcService;
import com.taotao.cloud.java.concurrent.springannotation.userdefinedannotation.service.HelloService;

@RpcService("HelloServicebb")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Index! " + name;
    }

    public void test() {
        System.out.println("test");
    }
}
