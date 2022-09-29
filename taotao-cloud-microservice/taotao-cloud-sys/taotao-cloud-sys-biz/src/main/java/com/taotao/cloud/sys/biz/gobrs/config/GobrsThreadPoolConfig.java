package com.taotao.cloud.sys.biz.gobrs.config;

import com.gobrs.async.threadpool.GobrsAsyncThreadPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class GobrsThreadPoolConfig {

	@Autowired
	private GobrsAsyncThreadPoolFactory factory;

	@PostConstruct
	public void gobrsThreadPoolExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(300, 500, 30, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(), r -> {
			Thread t = new Thread(r);
			t.setName("taotao-cloud-gobrs-threadpool");
			return t;
		});
		factory.setThreadPoolExecutor(threadPoolExecutor);
	}

	//*******************************************实时更新线程池************************************
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
	// 			LogUtils.warn("监听到DUCC配置GobrsAsync 线程池配置变更，property：{}", JsonUtils.toJSONString(configInfo));
	// 			ThreadPool threadPool = JSONObject.parseObject(configInfo, ThreadPool.class);
	// 			ThreadPoolExecutor executor = ThreadPoolBuilder.buildByThreadPool(threadPool);
	// 			factory.setThreadPoolExecutor(executor);
	// 			// 线程池更新成功
	// 			LogUtils.warn("GobrsAsync thread pool update success");
	// 		}
	// 	});
	// }

}
