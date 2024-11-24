package com.taotao.cloud.netty.itcast.test;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestEvent {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        group.next().execute(()->{
            log.debug("1");
        });
    }
}
