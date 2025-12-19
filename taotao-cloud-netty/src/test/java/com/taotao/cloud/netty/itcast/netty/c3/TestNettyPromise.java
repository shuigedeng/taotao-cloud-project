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
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * TestNettyPromise
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestNettyPromise {

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        // 1. 准备 EventLoop 对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        // 2. 可以主动创建 promise, 结果容器
        DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(
                () -> {
                    // 3. 任意一个线程执行计算，计算完毕后向 promise 填充结果
                    log.debug("开始计算...");
                    try {
                        int i = 1 / 0;
                        Thread.sleep(1000);
                        promise.setSuccess(80);
                    } catch (Exception e) {
                        e.printStackTrace();
                        promise.setFailure(e);
                    }
                })
                .start();
        // 4. 接收结果的线程
        log.debug("等待结果...");
        log.debug("结果是: {}", promise.get());
    }
}
