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
package com.taotao.cloud.canal.transfer;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.taotao.cloud.canal.abstracts.AbstractBasicMessageTransponder;
import com.taotao.cloud.canal.annotation.ListenPoint;
import com.taotao.cloud.canal.core.CanalMsg;
import com.taotao.cloud.canal.core.ListenerPoint;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import com.taotao.cloud.canal.properties.CanalProperties;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.springframework.util.StringUtils;

/**
 * 默认信息转换器
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:30
 */
public class DefaultMessageTransponder extends AbstractBasicMessageTransponder {

	public DefaultMessageTransponder(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config,
		List<CanalEventListener> listeners,
		List<ListenerPoint> annoListeners) {
		super(connector, config, listeners, annoListeners);
	}

	@Override
	protected Predicate<Map.Entry<Method, ListenPoint>> getAnnotationFilter(String destination,
		String schemaName, String tableName, CanalEntry.EventType eventType) {
		//看看指令是否正确
		Predicate<Map.Entry<Method, ListenPoint>> df = e ->
			StringUtils.isEmpty(e.getValue().destination())
				|| e.getValue().destination().equals(destination) || destination == null;

		//看看数据库实例名是否一样
		Predicate<Map.Entry<Method, ListenPoint>> sf = e -> e.getValue().schema().length == 0
			|| Arrays.asList(e.getValue().schema()).contains(schemaName)
			|| schemaName == null;

		//看看表名是否一样
		Predicate<Map.Entry<Method, ListenPoint>> tf = e -> e.getValue().table().length == 0
			|| Arrays.asList(e.getValue().table()).contains(tableName)
			|| tableName == null;

		//类型一致？
		Predicate<Map.Entry<Method, ListenPoint>> ef = e -> e.getValue().eventType().length == 0
			|| Arrays.stream(e.getValue().eventType()).anyMatch(ev -> ev == eventType)
			|| eventType == null;

		return df.and(sf).and(tf).and(ef);
	}

	@Override
	protected Object[] getInvokeArgs(Method method, CanalMsg canalMsg,
		CanalEntry.RowChange rowChange) {
		return Arrays.stream(method.getParameterTypes())
			.map(p -> p == CanalMsg.class ? canalMsg
				: p == CanalEntry.RowChange.class ? rowChange : null)
			.toArray();
	}

	@Override
	protected List<CanalEntry.EntryType> getIgnoreEntryTypes() {
		return Arrays.asList(CanalEntry.EntryType.TRANSACTIONBEGIN,
			CanalEntry.EntryType.TRANSACTIONEND, CanalEntry.EntryType.HEARTBEAT);
	}
}
