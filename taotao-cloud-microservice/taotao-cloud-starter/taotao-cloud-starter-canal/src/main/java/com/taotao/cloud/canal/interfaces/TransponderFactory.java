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
package com.taotao.cloud.canal.interfaces;

import com.alibaba.otter.canal.client.CanalConnector;
import com.taotao.cloud.canal.model.ListenerPoint;
import com.taotao.cloud.canal.properties.CanalProperties;
import java.util.List;
import java.util.Map;

/**
 * 信息转换工厂类接口层
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:51:05
 */
@FunctionalInterface
public interface TransponderFactory {

	/**
	 * newTransponder
	 *
	 * @param connector     canal 连接工具
	 * @param config        canal 链接信息
	 * @param listeners     实现接口的监听器
	 * @param annoListeners 注解监听拦截
	 * @return {@link com.taotao.cloud.canal.interfaces.MessageTransponder }
	 * @author shuigedeng
	 * @since 2021-09-03 20:51:12
	 */
	MessageTransponder newTransponder(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config, List<CanalEventListener> listeners,
		List<ListenerPoint> annoListeners);
}
