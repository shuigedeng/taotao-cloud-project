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

package com.taotao.cloud.sys.biz.gobrs.config;

import com.gobrs.async.core.threadpool.GobrsAsyncThreadPoolFactory;
import com.gobrs.async.core.threadpool.GobrsThreadPoolConfiguration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GobrsThreadPoolConfig extends GobrsThreadPoolConfiguration {

    @Override
    protected void doInitialize(GobrsAsyncThreadPoolFactory factory) {
        /** 自定义线程池 */
        //        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(300, 500, 30,
        // TimeUnit.SECONDS,
        //                new LinkedBlockingQueue());

        ExecutorService executorService = Executors.newCachedThreadPool();
        factory.setThreadPoolExecutor("http", executorService);
    }

    // *******************************************实时更新线程池************************************
    // @Resource
    // private NacosConfigService nacosConfigService;
    //
    // @PostConstruct
    // public void gobrsThreadPoolExecutor() throws NacosException {
    // 	// 从配置中心拿到 线程池配置规则 DuccConstant.GOBRS_ASYNC_THREAD_POOL 为线程池配置在配置中心的key
    // 	String config = nacosConfigService.getConfig("gobrs-dataId", "gobrs-group", 1000);
    // 	ThreadPool threadPool = JSONObject.parseObject(config, ThreadPool.class);
    //
    // 	// 通过gobrs-async 提供的构造器进行构造线程池
    // 	ThreadPoolExecutor executor = ThreadPoolBuilder.buildByThreadPool(threadPool);
    // 	factory.setThreadPoolExecutor(executor);
    // 	listenerDucc();
    // }
    //
    // /**
    //  * {
    //  * corePoolSize: 210,
    //  * maxPoolSize: 600,
    //  * keepAliveTime: 30,
    //  * capacity: 10000,
    //  * threadNamePrefix: "m-detail"
    //  * rejectedExecutionHandler： "CallerRunsPolicy"
    //  * }
    //  */
    // // 监听配置中心 线程池改动
    // private void listenerDucc() throws NacosException {
    // 	nacosConfigService.addListener("gobrs-dataId", "gobrs-group", new Listener() {
    // 		@Override
    // 		public Executor getExecutor() {
    // 			return null;
    // 		}
    //
    // 		@Override
    // 		public void receiveConfigInfo(String configInfo) {
    // 			LogUtils.warn("监听到DUCC配置GobrsAsync 线程池配置变更，property：{}",
    // JsonUtils.toJSONString(configInfo));
    // 			ThreadPool threadPool = JSONObject.parseObject(configInfo, ThreadPool.class);
    // 			ThreadPoolExecutor executor = ThreadPoolBuilder.buildByThreadPool(threadPool);
    // 			factory.setThreadPoolExecutor(executor);
    // 			// 线程池更新成功
    // 			LogUtils.warn("GobrsAsync thread pool update success");
    // 		}
    // 	});
    // }

}
