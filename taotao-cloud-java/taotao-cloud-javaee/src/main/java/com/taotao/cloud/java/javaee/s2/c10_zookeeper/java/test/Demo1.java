package com.taotao.cloud.java.javaee.s2.c10_zookeeper.java.test;

import com.qf.ZkUtil;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;

public class Demo1 {

    @Test
    public void connect(){
        CuratorFramework cf = ZkUtil.cf();
    }

}
