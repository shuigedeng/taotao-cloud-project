package com.taotao.cloud.java.javaee.s2.c10_zookeeper.java.test;

import com.taotao.cloud.java.javaee.s2.c10_zookeeper.java.ZkUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;

public class Demo3 {

    CuratorFramework cf = ZkUtil.cf();


    @Test
    public void listen() throws Exception {
        //1. 创建NodeCache对象，指定要监听的znode
        NodeCache nodeCache = new NodeCache(cf,"/qf");
        nodeCache.start();

        //2. 添加一个监听器
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                byte[] data = nodeCache.getCurrentData().getData();
                Stat stat = nodeCache.getCurrentData().getStat();
                String path = nodeCache.getCurrentData().getPath();

                System.out.println("监听的节点是：" + path);
                System.out.println("节点现在的数据是：" + new String(data,"UTF-8"));
                System.out.println("节点状态是：" + stat);

            }
        });

        System.out.println("开始监听！！");
        //3. System.in.read();
        System.in.read();
    }

}
