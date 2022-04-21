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
package com.taotao.cloud.canal.model;

import com.alibaba.otter.canal.client.CanalConnector;
import com.taotao.cloud.canal.abstracts.AbstractCanalClient;
import com.taotao.cloud.canal.annotation.ListenPoint;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.common.utils.context.ContextUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * SimpleCanalClient
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:52:12
 */
public class SimpleCanalClient extends AbstractCanalClient {

	/**
	 * 声明一个线程池
	 */
	private final ThreadPoolExecutor executor;

	/**
	 * 通过实现接口的监听器
	 */
	protected final List<CanalEventListener> listeners = new ArrayList<>();

	/**
	 * 通过注解的方式实现的监听器
	 */
	private final List<ListenerPoint> annoListeners = new ArrayList<>();

	public SimpleCanalClient(CanalProperties canalProperties) {
		super(canalProperties);

		// 可能需要调整，紧跟德叔脚步走，默认核心线程数5个，最大线程数20个，
		// 线程两分钟分钟不执行就。。。
		executor = new ThreadPoolExecutor(
			5,
			20,
			120L,
			TimeUnit.SECONDS,
			new SynchronousQueue<>(),
			new ThreadFactory() {
				private final ThreadFactory factory = Executors.defaultThreadFactory();

				@Override
				public Thread newThread(Runnable r) {
					Thread t = factory.newThread(r);
					t.setName("taotao-cloud-canal-thread-" + t.getName());

					//UncaughtExceptionHandler handler = t.getUncaughtExceptionHandler();
					//if (!(handler instanceof MonitorThreadPoolUncaughtExceptionHandler)) {
					//	t.setUncaughtExceptionHandler(
					//		new MonitorThreadPoolUncaughtExceptionHandler(handler));
					//}

					//后台线程模式
					t.setDaemon(true);
					return t;
				}
			});

		//初始化监听器
		initListeners();
	}

	@Override
	protected void process(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config) {
		executor.submit(factory.newTransponder(connector, config, listeners, annoListeners));
	}


	@Override
	public void stop() {
		//停止 canal 客户端
		super.stop();

		//线程池关闭
		executor.shutdown();
	}

	/**
	 * 初始化监听器
	 *
	 * @since 2021-09-03 20:52:24
	 */
	private void initListeners() {
		LogUtil.info("{}: 监听器正在初始化....", Thread.currentThread().getName());
		//获取监听器
		List<CanalEventListener> list = ContextUtil.getBeansOfType(CanalEventListener.class);
		//若没有任何监听的，直接返回
		if (list != null) {
			//若存在目标监听，放入 listenerMap
			listeners.addAll(list);
		}

		//通过注解的方式去监听的话。。
		Map<String, Object> listenerMap = ContextUtil.getBeansWithAnnotation(
			com.taotao.cloud.canal.annotation.CanalEventListener.class);

		//也放入 map
		if (listenerMap != null) {
			for (Object target : listenerMap.values()) {
				//方法获取
				Method[] methods = target.getClass().getDeclaredMethods();
				if (methods.length > 0) {
					for (Method method : methods) {
						ListenPoint lp = AnnotatedElementUtils.findMergedAnnotation(method,
							ListenPoint.class);
						if (lp != null) {
							annoListeners.add(new ListenerPoint(target, method, lp));
						}
					}
				}
			}
		}

		//初始化监听结束
		LogUtil.info("{}: 监听器初始化完成.", Thread.currentThread().getName());

		//整个项目上下文都没发现监听器。。。
		if (LogUtil.isWarnEnabled() && listeners.isEmpty() && annoListeners.isEmpty()) {
			LogUtil.warn("{}: 项目中没有任何监听的目标! ", Thread.currentThread().getName());
		}
	}
}
