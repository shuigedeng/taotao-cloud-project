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

package com.taotao.cloud.netty.itcast.test;

import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;

/**
 * TestFuture
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class TestFuture {

    public static void main( String[] args ) throws ExecutionException, InterruptedException {
        //        DefaultEventLoop eventExecutors = new DefaultEventLoop();
        ExecutorService eventExecutors = Executors.newFixedThreadPool(2);
        CompletableFuture<Integer> future = new CompletableFuture<>();

        eventExecutors.execute(
                () -> {
                    log.debug("enter");
                    future.thenAccept(
                            x -> {
                                log.debug("{}", x);
                            });
                });

        Thread.sleep(1000);
        System.out.println(future.complete(100));
    }
}
