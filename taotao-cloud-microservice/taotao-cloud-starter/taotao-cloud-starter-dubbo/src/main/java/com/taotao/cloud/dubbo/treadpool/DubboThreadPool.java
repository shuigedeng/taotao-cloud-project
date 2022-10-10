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
package com.taotao.cloud.dubbo.treadpool;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.threadpool.ThreadPool;

/**
 * 自定义线程池
 * <p>
 * 如何估算线程数？
 * Dubbo的默认配置并不一定适合所有的业务场景，线程池的配置要根据具体的机器以及业务类型来决定。在电商业务类型下，任务大部分都是IO密集型的，而IO密集型的线程数可以根据如下公式计算得出：线程池线程数=CPU核数*(响应时间/（响应时间-调用第三方接口时间-访问数据库时间））。
 * <p>
 * 比如一个获取商品详细的接口平均响应时间为50ms，调用库存接口用了10ms，调用优惠接口用了10ms，调用数据库用来20ms，该服务所在机器CPU核数为10，则可以估算出线程数为:threads=10*(50/50-10-10-20)=50。
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-08 10:19:44
 */
public class DubboThreadPool implements ThreadPool {

	@Override
	public Executor getExecutor(URL url) {
		LogUtils.info("DubboThreadPool getExecutor activate ------------------------------");
		LogUtils.info(url.toFullString());

		AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor executor = new AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(10000);
		executor.setKeepAliveSeconds(300);
		executor.setThreadNamePrefix("taotao-cloud-dubbo-executor");

		executor.setTaskDecorator(new AsyncAutoConfiguration.AsyncTaskDecorator());

		/*
		 rejection-policy：当pool已经达到max size的时候，如何处理新任务
		 CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		 */
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;

	}
}
