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

import com.taotao.cloud.canal.annotation.ListenPoint;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听 canal 操作
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:51:45
 */
public class ListenerPoint {

	/**
	 * 目标
	 */
	private Object target;

	/**
	 * 监听的方法和节点
	 */
	private Map<Method, ListenPoint> invokeMap = new HashMap<>();

	ListenerPoint(Object target, Method method, ListenPoint anno) {
		this.target = target;
		this.invokeMap.put(method, anno);
	}

	/**
	 * 返回目标类
	 *
	 * @since 2021-09-03 20:51:57
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * 获取监听的操作方法和节点
	 *
	 * @since 2021-09-03 20:52:04
	 */
	public Map<Method, ListenPoint> getInvokeMap() {
		return invokeMap;
	}
}
