package com.taotao.cloud.java.javaee.s2.c11_distributed.second_kill.java.config;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZkConfig {


    @Bean
    public CuratorFramework cf(){

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,2);

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("192.168.199.109:2181,192.168.199.109:2182,192.168.199.109:2183")
                .retryPolicy(retryPolicy)
                .build();

        curatorFramework.start();

        return curatorFramework;
    }

}
