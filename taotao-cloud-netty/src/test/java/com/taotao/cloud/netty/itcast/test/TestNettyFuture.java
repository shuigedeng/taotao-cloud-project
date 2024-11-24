package com.taotao.cloud.netty.itcast.test;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws InterruptedException {
        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);
            log.debug("enter...");
            promise.addListener(future -> {
                log.debug("{}",future.getNow());
            });

        Thread.sleep(1000);
        promise.setSuccess(100);
    }
}
