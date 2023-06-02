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

package com.taotao.cloud.message.biz.austin.support.config;

import org.dromara.hutoolcore.thread.ExecutorBuilder;
import com.taotao.cloud.message.biz.austin.common.constant.ThreadPoolConstant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 3y support 线程池配置类
 */
public class SupportThreadPoolConfig {

    /** 业务：实现pending队列的单线程池 配置：核心线程可以被回收，当线程池无被引用且无核心线程数，应当被回收 动态线程池且被Spring管理：false */
    public static ExecutorService getPendingSingleThreadPool() {
        return ExecutorBuilder.create()
                .setCorePoolSize(ThreadPoolConstant.SINGLE_CORE_POOL_SIZE)
                .setMaxPoolSize(ThreadPoolConstant.SINGLE_MAX_POOL_SIZE)
                .setWorkQueue(ThreadPoolConstant.BIG_BLOCKING_QUEUE)
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setAllowCoreThreadTimeOut(true)
                .setKeepAliveTime(ThreadPoolConstant.SMALL_KEEP_LIVE_TIME, TimeUnit.SECONDS)
                .build();
    }
}
