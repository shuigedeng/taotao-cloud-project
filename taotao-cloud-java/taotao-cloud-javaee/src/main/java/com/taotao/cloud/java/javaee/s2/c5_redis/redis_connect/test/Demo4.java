package com.taotao.cloud.java.javaee.s2.c5_redis.redis_connect.test;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

public class Demo4 {

    @Test
    public void pool2(){
        //1. 创建连接池配置信息
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(100);  // 连接池中最大的活跃数
        poolConfig.setMaxIdle(10);   // 最大空闲数
        poolConfig.setMinIdle(5);   // 最小空闲数
        poolConfig.setMaxWaitMillis(3000);  // 当连接池空了之后,多久没获取到Jedis对象,就超时

        //2. 创建连接池
        JedisPool pool = new JedisPool(poolConfig,"192.168.199.109",6379);

        //3. 通过连接池获取jedis对象
        Jedis jedis = pool.getResource();

        //4. 操作
        String value = jedis.get("stringUser");
        System.out.println("user:" + value);

        //5. 释放资源
        jedis.close();
    }

    //  Redis管道的操作
    @Test
    public void pipeline(){
        //1. 创建连接池
        JedisPool pool = new JedisPool("192.168.199.109",6379);
        long l = System.currentTimeMillis();

        /*//2. 获取一个连接对象
        Jedis jedis = pool.getResource();

        //3. 执行incr - 100000次
        for (int i = 0; i < 100000; i++) {
            jedis.incr("pp");
        }

        //4. 释放资源
        jedis.close();*/

        //================================
        //2. 获取一个连接对象
        Jedis jedis = pool.getResource();
        //3. 创建管道
        Pipeline pipelined = jedis.pipelined();
        //3. 执行incr - 100000次放到管道中
        for (int i = 0; i < 100000; i++) {
            pipelined.incr("qq");
        }
        //4. 执行命令
        pipelined.syncAndReturnAll();
        //5. 释放资源
        jedis.close();

        System.out.println(System.currentTimeMillis() - l);
    }

}
