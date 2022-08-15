package com.taotao.cloud.agent.demo.api;


import com.taotao.cloud.agent.demo.common.TimeDateUtil;

/**
 * Created by pphh on 2022/6/23.
 */
public class Greeting {

    public String sayHello() {
        String strTime = TimeDateUtil.getCurrentTimeString();
        String hello = "hello,world, time = " + strTime;
        System.out.println(hello);
        return hello;
    }

}
