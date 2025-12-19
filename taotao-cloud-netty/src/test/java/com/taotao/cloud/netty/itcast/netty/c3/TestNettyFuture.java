/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.netty.itcast.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * TestNettyFuture
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestNettyFuture {

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<Integer> future =
                eventLoop.submit(
                        new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                log.debug("执行计算");
                                Thread.sleep(1000);
                                return 70;
                            }
                        });
        //        log.debug("等待结果");
        //        log.debug("结果是 {}", future.get());
        future.addListener(
                new GenericFutureListener<Future<? super Integer>>() {
                    @Override
                    public void operationComplete( Future<? super Integer> future ) throws Exception {
                        log.debug("接收结果:{}", future.getNow());
                    }
                });
    }
}
