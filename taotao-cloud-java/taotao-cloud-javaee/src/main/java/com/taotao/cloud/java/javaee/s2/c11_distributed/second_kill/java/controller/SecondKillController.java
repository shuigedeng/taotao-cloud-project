package com.taotao.cloud.java.javaee.s2.c11_distributed.second_kill.java.controller;

import com.taotao.cloud.java.javaee.s2.c11_distributed.second_kill.java.util.RedisLockUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class SecondKillController {

    @Autowired
    private CuratorFramework cf;

    @Autowired
    private RedisLockUtil lock;

    //1. 准备商品的库存
    public static Map<String,Integer> itemStock = new HashMap<>();

    //2. 准备商品的订单
    public static Map<String,Integer> itemOrder = new HashMap<>();

    static{
        itemStock.put("牙刷",10000);
        itemOrder.put("牙刷",0);
    }

    @GetMapping("/redis/kill")
    public String redisKill(String item) throws Exception {

    //...加锁
    if(lock.lock(item,System.currentTimeMillis() + "",1)){
        //1. 减库存
        Integer stock = itemStock.get(item);
        if (stock <= 0) {
            return "商品库存数不足！！！";
        }

        Thread.sleep(100);
        itemStock.put(item, stock - 1);


        //2. 创建订单
        Thread.sleep(100);
        itemOrder.put(item, itemOrder.get(item) + 1);

        // 释放锁
        lock.unlock(item);

        //3. 返回信息
        return "抢购成功！！！" + item + "： 剩余库存数为 - " + itemStock.get(item) + "，订单数为 - " + itemOrder.get(item);
    }else{
        return "没有抢到商品";
    }
    }

    @GetMapping("/zk/kill")
    public String zkKill(String item) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(cf,"/lock");

        //...加锁
//        lock.acquire();
        if(lock.acquire(1,TimeUnit.SECONDS)) {
            //1. 减库存
            Integer stock = itemStock.get(item);
            if (stock <= 0) {
                return "商品库存数不足！！！";
            }

            Thread.sleep(100);
            itemStock.put(item, stock - 1);


            //2. 创建订单
            Thread.sleep(100);
            itemOrder.put(item, itemOrder.get(item) + 1);

            // 释放锁
            lock.release();

            //3. 返回信息
            return "抢购成功！！！" + item + "： 剩余库存数为 - " + itemStock.get(item) + "，订单数为 - " + itemOrder.get(item);
        }else{
            return "没有抢到商品";
        }
    }

}
