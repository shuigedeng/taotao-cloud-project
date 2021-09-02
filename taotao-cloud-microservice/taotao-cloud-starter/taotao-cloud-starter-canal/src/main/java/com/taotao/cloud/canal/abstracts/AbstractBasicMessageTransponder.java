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
package com.taotao.cloud.canal.abstracts;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.taotao.cloud.canal.annotation.ListenPoint;
import com.taotao.cloud.canal.model.CanalMsg;
import com.taotao.cloud.canal.model.ListenerPoint;
import com.taotao.cloud.canal.exception.CanalClientException;
import com.taotao.cloud.canal.interfaces.CanalEventListener;
import com.taotao.cloud.canal.properties.CanalProperties;
import com.taotao.cloud.common.utils.LogUtil;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.springframework.util.CollectionUtils;

/**
 * 消息处理转换类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 21:36
 */
public abstract class AbstractBasicMessageTransponder extends AbstractMessageTransponder {

	public AbstractBasicMessageTransponder(CanalConnector connector,
		Map.Entry<String, CanalProperties.Instance> config,
		List<CanalEventListener> listeners,
		List<ListenerPoint> annoListeners) {
		super(connector, config, listeners, annoListeners);
	}

	@Override
	protected void distributeEvent(Message message) {
		//获取操作实体
		List<CanalEntry.Entry> entries = message.getEntries();
		//遍历实体
		for (CanalEntry.Entry entry : entries) {
			//忽略实体类的类型
			List<CanalEntry.EntryType> ignoreEntryTypes = getIgnoreEntryTypes();
			if (ignoreEntryTypes != null && ignoreEntryTypes.stream()
				.anyMatch(t -> entry.getEntryType() == t)) {
				continue;
			}

			//canal 改变信息
			CanalEntry.RowChange rowChange;
			try {
				//获取信息改变
				rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

			} catch (Exception e) {
				throw new CanalClientException("错误 ##转换错误 , 数据信息:" + entry.toString(),
					e);
			}

			distributeByAnnotation(destination,
				entry.getHeader().getSchemaName(),
				entry.getHeader().getTableName(), rowChange);

			distributeByImpl(destination,
				entry.getHeader().getSchemaName(),
				entry.getHeader().getTableName(), rowChange);

		}
	}

	/**
	 * 处理注解方式的 canal 监听器
	 *
	 * @param destination canal 指令
	 * @param schemaName  实例名称
	 * @param tableName   表名称
	 * @param rowChange   数据
	 * @author shuigedeng
	 * @since 2021/8/30 21:39
	 */
	protected void distributeByAnnotation(String destination,
		String schemaName,
		String tableName,
		CanalEntry.RowChange rowChange) {

		//对注解的监听器进行事件委托
		if (!CollectionUtils.isEmpty(annotationListeners)) {
			annotationListeners
				.forEach(point -> point
					.getInvokeMap()
					.entrySet()
					.stream()
					.filter(getAnnotationFilter(destination, schemaName, tableName,
						rowChange.getEventType()))
					.forEach(entry -> {
						Method method = entry.getKey();
						method.setAccessible(true);
						try {
							CanalMsg canalMsg = new CanalMsg();
							canalMsg.setDestination(destination);
							canalMsg.setSchemaName(schemaName);
							canalMsg.setTableName(tableName);

							Object[] args = getInvokeArgs(method, canalMsg, rowChange);
							method.invoke(point.getTarget(), args);
						} catch (Exception e) {
							LogUtil.error("{}: 委托 canal 监听器发生错误! 错误类:{}, 方法名:{}",
								Thread.currentThread().getName(),
								point.getTarget().getClass().getName(), method.getName());
						}
					}));
		}
	}


	/**
	 * 处理监听信息
	 *
	 * @param destination 指令
	 * @param schemaName  库实例
	 * @param tableName   表名
	 * @param rowChange   參數
	 * @author shuigedeng
	 * @since 2021/8/30 21:39
	 */
	protected void distributeByImpl(String destination,
		String schemaName,
		String tableName,
		CanalEntry.RowChange rowChange) {
		if (implListeners != null) {
			for (CanalEventListener listener : implListeners) {
				listener.onEvent(destination, schemaName, tableName, rowChange);
			}
		}
	}


	/**
	 * 断言注解方式的监听过滤规则
	 *
	 * @param destination 指定
	 * @param schemaName  数据库实例
	 * @param tableName   表名称
	 * @param eventType   事件类型
	 * @author shuigedeng
	 * @since 2021/8/30 21:38
	 */
	protected abstract Predicate<Map.Entry<Method, ListenPoint>> getAnnotationFilter(
		String destination, String schemaName, String tableName, CanalEntry.EventType eventType);


	/**
	 * 获取参数
	 *
	 * @param method    委托处理的方法
	 * @param canalMsg  其他信息
	 * @param rowChange 处理的数据
	 * @author shuigedeng
	 * @since 2021/8/30 21:38
	 */
	protected abstract Object[] getInvokeArgs(Method method, CanalMsg canalMsg,
		CanalEntry.RowChange rowChange);


	/**
	 * 忽略实体类的类型
	 *
	 * @author shuigedeng
	 * @since 2021/8/30 21:37
	 */
	protected List<CanalEntry.EntryType> getIgnoreEntryTypes() {
		return Collections.emptyList();
	}

}
