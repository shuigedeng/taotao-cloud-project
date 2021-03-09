package com.taotao.cloud.java.javaee.s2.c10_zookeeper.java;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkUtil {

    public static CuratorFramework cf(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,2);
        CuratorFramework cf = CuratorFrameworkFactory.builder()
                .connectString("192.168.199.109:2181,192.168.199.109:2182,192.168.199.109:2183")
                .retryPolicy(retryPolicy)
                .build();

        cf.start();

        return cf;
    }

}
