/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.core.model;


import com.taotao.cloud.common.model.Callable;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.model.Callable.Action0;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 全局进程关闭事件定义
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:36:39
 */
public class ProcessExitEvent {

	/**
	 * callBackList
	 */
	private static final ArrayList<ExitCallback> callBackList = new ArrayList<>();
	/**
	 * lock
	 */
	private static final Object lock = new Object();

	/**
	 * 越大越晚 必须大于0
	 *
	 * @param action0 action0
	 * @param order   order
	 * @param async  async
	 * @author shuigedeng
	 * @since 2021-09-02 20:37:02
	 */
	public static void register(Callable.Action0 action0, int order, Boolean async) {
		synchronized (lock) {
			callBackList.add(new ExitCallback(action0, Math.abs(order), async));
		}
	}

	static {
		//JVM 停止或重启时
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				synchronized (lock) {
					callBackList.sort(Comparator.comparingInt(c -> c.order));

					for (ExitCallback a : callBackList) {
						Callable.Action0 method = () -> {
							try {
								a.action0.invoke();
							} catch (Exception e2) {
								LogUtil.error(e2,
										"进程关闭事件回调处理出错");
							}
						};

						if (a.async) {
							new Thread(method::invoke).start();
						} else {
							method.invoke();
						}
					}
				}
				LogUtil.info(
						PropertyUtil.getProperty(CoreProperties.SpringApplicationName)
								+ " 应用已正常退出！");
			} catch (Exception e) {
				LogUtil.error(
						PropertyUtil.getProperty(CoreProperties.SpringApplicationName)
								+ " 进程关闭事件回调处理出错", e);
			}
		}));
	}

	/**
	 * ExitCallback
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:37:07
	 */
	private static class ExitCallback {

		/**
		 * action0
		 */
		Callable.Action0 action0;
		/**
		 * 顺序
		 */
		Integer order;
		/**
		 * 异步支持
		 */
		Boolean async;

		public ExitCallback() {
		}

		public ExitCallback(Action0 action0, Integer order, Boolean async) {
			this.action0 = action0;
			this.order = order;
			this.async = async;
		}

		public Action0 getAction0() {
			return action0;
		}

		public void setAction0(Action0 action0) {
			this.action0 = action0;
		}

		public Integer getOrder() {
			return order;
		}

		public void setOrder(Integer order) {
			this.order = order;
		}

		public Boolean getAsync() {
			return async;
		}

		public void setAsync(Boolean async) {
			this.async = async;
		}
	}
}
