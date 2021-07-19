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
package com.taotao.cloud.common.base;

import static com.taotao.cloud.common.base.CoreProperties.SpringApplicationName;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.util.ArrayList;
import java.util.Comparator;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 全局进程关闭事件定义
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/6/22 17:09
 **/
public class ProcessExitEvent {

	private static final ArrayList<ExitCallback> CALL_BACK_LIST = new ArrayList<>();
	private static final Object LOCK = new Object();

	/**
	 * @param action0
	 * @param order   越大越晚 必须大于0
	 */
	public static void register(Callable.Action0 action0, int order, Boolean asynch) {
		synchronized (LOCK) {
			CALL_BACK_LIST.add(new ExitCallback(action0, Math.abs(order), asynch));
		}
	}

	static {
		//JVM 停止或重启时
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				synchronized (LOCK) {
					CALL_BACK_LIST.sort(Comparator.comparingInt(c -> c.order));
					for (ExitCallback exitCallback : CALL_BACK_LIST) {
						Callable.Action0 method = () -> {
							try {
								exitCallback.action0.invoke();
							} catch (Exception e2) {
								LogUtil.error("进程关闭事件回调处理出错", e2);
							}
						};
						if (exitCallback.async) {
							new Thread(() -> {
								method.invoke();
							}).start();
						} else {
							method.invoke();
						}
					}
				}
				LogUtil.info(PropertyUtil.getProperty(SpringApplicationName) + "--应用已正常退出！");
			} catch (Exception e) {
				LogUtil.error("进程关闭事件回调处理出错", e);
			}
		}));
	}

	@Data
	@AllArgsConstructor
	private static class ExitCallback {

		Callable.Action0 action0;
		/**
		 * 顺序
		 */
		Integer order;
		/**
		 * 异步支持
		 */
		Boolean async = false;
	}
}
