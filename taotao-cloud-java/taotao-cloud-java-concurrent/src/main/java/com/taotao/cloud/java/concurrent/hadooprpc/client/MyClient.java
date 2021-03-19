package com.taotao.cloud.java.concurrent.hadooprpc.client;

import com.taotao.cloud.java.concurrent.hadooprpc.protocol.SomeService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.net.InetSocketAddress;

public class MyClient {
    public static void main(String[] args) throws Exception {
        SomeService someService = RPC.getProxy(
                SomeService.class, Long.MAX_VALUE, new InetSocketAddress(
                        "localhost", 5555), new Configuration());

        String ret = someService.heartBeat("wilson");
        System.out.println(ret);
    }
}
