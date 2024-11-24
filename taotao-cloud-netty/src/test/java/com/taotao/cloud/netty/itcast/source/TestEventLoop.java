package com.taotao.cloud.netty.itcast.source;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

public class TestEventLoop {
    public static void main(String[] args) {
        EventLoop eventLoop = new NioEventLoopGroup().next();
        eventLoop.execute(()->{
            System.out.println("hello");
        });
    }
}
