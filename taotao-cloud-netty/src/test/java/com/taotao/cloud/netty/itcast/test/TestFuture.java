package com.taotao.cloud.netty.itcast.test;

import io.netty.channel.DefaultEventLoop;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TestFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        ExecutorService eventExecutors = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = new CompletableFuture<>();


        eventExecutors.execute(()->{
            log.debug("enter");
            future.thenAccept(x->{
                log.debug("{}", x);
            });
        });


        Thread.sleep(1000);
        System.out.println(future.complete(100));
    }
}
