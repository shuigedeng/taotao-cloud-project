package com.taotao.cloud.java.javaee.s2.c10_zookeeper.java.test;

import com.taotao.cloud.java.javaee.s2.c10_zookeeper.java.ZkUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

public class Demo2 {

    CuratorFramework cf = ZkUtil.cf();

    @Test
    public void stat() throws Exception {
        Stat stat = cf.checkExists().forPath("/qf");
        System.out.println(stat);
    }

    @Test
    public void delete() throws Exception {
        cf.delete().deletingChildrenIfNeeded().forPath("/qf2");
    }

    @Test
    public void update() throws Exception {
        cf.setData().forPath("/qf2","oooo".getBytes());
    }

    @Test
    public void create() throws Exception {
        cf.create().withMode(CreateMode.PERSISTENT).forPath("/qf2","uuuu".getBytes());
    }


    @Test
    public void getChildren() throws Exception {
        List<String> strings = cf.getChildren().forPath("/");

        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void getData() throws Exception {
        byte[] bytes = cf.getData().forPath("/qf");
        System.out.println(new String(bytes,"UTF-8"));
    }

}
