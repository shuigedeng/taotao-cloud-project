package com.taotao.cloud.java.javaee.s2.c5_redis.redis_connect.test;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class Demo5 {

    @Test
    public void test(){
        // 创建Set<HostAndPort> nodes
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.199.109",7001));
        nodes.add(new HostAndPort("192.168.199.109",7002));
        nodes.add(new HostAndPort("192.168.199.109",7003));
        nodes.add(new HostAndPort("192.168.199.109",7004));
        nodes.add(new HostAndPort("192.168.199.109",7005));
        nodes.add(new HostAndPort("192.168.199.109",7006));

        // 创建JedisCluster对象
        JedisCluster jedisCluster = new JedisCluster(nodes);

        // 操作
        String value = jedisCluster.get("b");
        System.out.println(value);
    }
}
